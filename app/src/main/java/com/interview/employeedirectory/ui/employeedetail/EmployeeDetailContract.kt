package com.interview.employeedirectory.ui.employeedetail

import com.interview.employeedirectory.base.BasePresenter
import com.interview.employeedirectory.models.Employee

interface EmployeeDetailContract {

    interface View {
        fun displayError()
        fun displayEmployee(employee: Employee)
        fun startPhoneCall(phoneNumber: String)
        fun startEmail(email: String)
        fun updateFavoriteState(isFavorite: Boolean)
    }

    interface Presenter: BasePresenter {
        fun onPhoneClicked()
        fun onEmailClicked()
        fun onAddFavoriteClicked()
        fun onRemoveFavoriteClicked()
    }
}