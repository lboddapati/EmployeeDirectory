package com.interview.employeedirectory.ui.employeedetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.interview.employeedirectory.R
import com.interview.employeedirectory.base.BaseActivity
import com.interview.employeedirectory.models.Employee
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class EmployeeDetailActivity: BaseActivity(), EmployeeDetailContract.View {

    companion object {
        private const val EMPLOYEE_KEY = "employee_key"

        fun intentFor(context: Context, employee: Employee)
                = Intent(context, EmployeeDetailActivity::class.java)
            .putExtra(EMPLOYEE_KEY, employee)
    }

    private lateinit var presenter: EmployeeDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = get { parametersOf(intent.getParcelableExtra(EMPLOYEE_KEY), this, lifecycle) }
    }

    override fun displayEmployee(employee: Employee) {
        setContentView(R.layout.employee_detail)
        findViewById<ImageView>(R.id.image).apply {
            Glide.with(context)
                .load(employee.largeImageUrl ?: "")
                .centerCrop()
                .placeholder(R.drawable.default_profile_photo)
                .into(this)
        }
        findViewById<TextView>(R.id.name).text = employee.fullName
        findViewById<TextView>(R.id.biography).apply {
            if (employee.biography.isNullOrBlank()) {
                visibility = GONE
            } else {
                text = employee.biography
            }
        }
        findViewById<TextView>(R.id.phone_number).apply {
            if (employee.phoneNumber.isNullOrBlank()) {
                visibility = GONE
            } else {
                text = employee.phoneNumber
                setOnClickListener { presenter.onPhoneClicked() }
            }
        }
        findViewById<TextView>(R.id.email).apply {
            text = employee.email
            setOnClickListener { presenter.onEmailClicked() }
        }
    }

    override fun displayError() = displayErrorWithoutRetry()

    override fun startPhoneCall(phoneNumber: String) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
    }

    override fun startEmail(email: String) {
        startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")))
    }
}