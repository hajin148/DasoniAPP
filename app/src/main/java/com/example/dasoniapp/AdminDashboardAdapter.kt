package com.example.dasoniapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminDashboardAdapter(
    private var users: MutableList<UserDashboard>,
    private val listener: OnUserClickListener
) : RecyclerView.Adapter<AdminDashboardAdapter.UserViewHolder>() {

    interface OnUserClickListener {
        fun onUserClick(user: UserDashboard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.itemView.setOnClickListener { listener.onUserClick(user) }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateData(newUsers: MutableList<UserDashboard>) {
        users = newUsers
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)

        fun bind(user: UserDashboard) {
            nameTextView.text = user.text
            emailTextView.text = user.email
        }
    }
}

data class UserDashboard(val uid: String, val text: String, val email: String)
