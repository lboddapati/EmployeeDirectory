package com.interview.employeedirectory.ui.employeelist

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interview.employeedirectory.R
import com.interview.employeedirectory.base.BaseActivity
import com.interview.employeedirectory.models.Employee
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class EmployeeListActivity: BaseActivity(), EmployeeListContract.View {

    private val presenter: EmployeeListContract.Presenter = get {
        parametersOf(this, lifecycle)
    }
    private val adapter: EmployeeListAdapter by lazy { EmployeeListAdapter() }

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