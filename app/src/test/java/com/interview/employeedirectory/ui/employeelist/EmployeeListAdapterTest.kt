package com.interview.employeedirectory.ui.employeelist

import com.interview.employeedirectory.models.Employee
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EmployeeListAdapterTest {

    @Test
    fun getItemCount_returnsCorrectValue() {
        val adapter = EmployeeListAdapter()
        Assert.assertEquals(0, adapter.itemCount)

        adapter.addEmployees(listOf(mock()))
        Assert.assertEquals(1, adapter.itemCount)

        adapter.addEmployees(listOf(mock(), mock()))
        Assert.assertEquals(3, adapter.itemCount)
    }

    @Test
    fun onBindViewHolder_delegatesToViewHolder() {
        val viewHolder = mock<EmployeeListItemViewHolder>()
        val employee = mock<Employee>()
        EmployeeListAdapter().apply {
            addEmployees(listOf(employee))
            onBindViewHolder(viewHolder, 0)
        }

        verify(viewHolder).bind(employee)
    }
}