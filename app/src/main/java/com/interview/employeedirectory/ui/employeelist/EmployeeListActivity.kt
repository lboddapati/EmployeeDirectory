package com.interview.employeedirectory.ui.employeelist

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interview.employeedirectory.R
import com.interview.employeedirectory.base.BaseActivity
import com.interview.employeedirectory.models.Employee
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class EmployeeListActivity: BaseActivity(), EmployeeListContract.View {

    private lateinit var presenter: EmployeeListContract.Presenter
    private val adapter: EmployeeListAdapter by lazy { EmployeeListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = get {
            val viewModel = if (savedInstanceState != null) {
                EmployeeListViewModel.fromBundle(savedInstanceState)
            } else {
                EmployeeListViewModel()
            }
            parametersOf(this, viewModel, lifecycle)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        presenter.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun displayError() {
        displayError { presenter.onRetryClicked() }
    }

    override fun displayEmptyState() {
        setContentView(R.layout.employee_list_empty_state)
    }

    override fun displayEmployeeList(employees: List<Employee>) {
        setContentView(R.layout.recyclerview)
        setupRecyclerView()
        adapter.addEmployees(employees)
    }

    private fun setupRecyclerView() {
        findViewById<RecyclerView>(R.id.recyclerview).apply {
            layoutManager = LinearLayoutManager(this@EmployeeListActivity)
            adapter = this@EmployeeListActivity.adapter
            addItemDecoration(DividerItemDecoration(
                this@EmployeeListActivity,
                RecyclerView.VERTICAL
            ))
        }
    }
}