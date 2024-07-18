package com.example.dasoniapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminUserManagementAdapter(private var dataList: List<UserName>) :
    RecyclerView.Adapter<AdminUserManagementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_management, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.textView.text = data.text
        holder.emailTextView.text = data.email
    }

    override fun getItemCount(): Int = dataList.size

    fun updateData(newDataList: List<UserName>) {
        dataList = newDataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView16)
        val emailTextView: TextView = itemView.findViewById(R.id.textView69)
    }
}

data class UserName(val text: String, val email: String)
