package com.example.dasoniapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class AdminUserManagementAdapter(private var dataList: List<UserName>, private val listener: OnUserClickListener) :
    RecyclerView.Adapter<AdminUserManagementAdapter.ViewHolder>() {

    interface OnUserClickListener {
        fun onUserClick(user: UserName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_management, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            listener.onUserClick(data)
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun updateData(newDataList: List<UserName>) {
        dataList = newDataList
        notifyDataSetChanged()
    }


    fun toggleSelection(user: UserName) {
        val index = dataList.indexOf(user)
        if (index != -1) {
            dataList[index].isSelected = !dataList[index].isSelected
            notifyItemChanged(index)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView16)
        val emailTextView: TextView = itemView.findViewById(R.id.textView69)
        private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout6)

        fun bind(user: UserName) {
            textView.text = user.text
            emailTextView.text = user.email
            constraintLayout.setBackgroundColor(
                if (user.isSelected) Color.LTGRAY else Color.WHITE
            )
        }
    }
}


data class UserName(val uid: String,val text: String, val email: String, var isSelected: Boolean = false)
