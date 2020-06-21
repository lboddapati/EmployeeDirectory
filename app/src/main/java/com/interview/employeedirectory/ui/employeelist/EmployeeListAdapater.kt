package com.interview.employeedirectory.ui.employeelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.interview.employeedirectory.R
import com.interview.employeedirectory.models.Employee
import org.koin.core.KoinComponent

class EmployeeListAdapter: RecyclerView.Adapter<EmployeeListItemViewHolder>() {
    private val employees = arrayListOf<Employee>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = EmployeeListItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.employee_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: EmployeeListItemViewHolder, position: Int) {
        holder.bind(employees[position])
    }

    override fun getItemCount() = employees.size

    fun addEmployees(newEmployees: List<Employee>) {
        val positionStart = this.employees.size
        employees.addAll(newEmployees)
        notifyItemRangeChanged(positionStart, newEmployees.size)
    }
}

class EmployeeListItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), KoinComponent {

    private val thumbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
    private val name = itemView.findViewById<TextView>(R.id.name)
    private val team = itemView.findViewById<TextView>(R.id.team)

    fun bind(employee: Employee) {
        Glide.with(itemView.context)
            .load(employee.smallImageUrl ?: "")
            .thumbnail(0.25f)
            .circleCrop()
            .placeholder(R.drawable.default_profile_photo)
            .into(thumbnail)
        name.text = employee.fullName
        team.text = employee.team
    }
}