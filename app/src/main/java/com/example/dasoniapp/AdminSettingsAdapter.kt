package com.example.dasoniapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class AdminSettingsAdapter(private var dataList: List<UserSettings>) :
    RecyclerView.Adapter<AdminSettingsAdapter.ViewHolder>() {

//    interface OnUserClickListener {
//        fun onUserClick(user: UserSettings)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_activation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
//        holder.itemView.setOnClickListener {
//            listener.onUserClick(data)
//        }
        if (data.accountStatus) {
            holder.constraintLayout6.visibility = View.VISIBLE
            holder.constraintLayout7.visibility = View.GONE
            holder.textView6.text = data.text
            holder.emailTextView6.text = data.email
        } else {
            holder.constraintLayout6.visibility = View.GONE
            holder.constraintLayout7.visibility = View.VISIBLE
            holder.textView7.text = data.text
            holder.emailTextView7.text = data.email
        }

//        holder.itemView.setOnClickListener {
//            listener.onUserClick(data)
//        }

        holder.toggleActiveButton.setOnClickListener {
            toggleAccountStatus(data)
        }

        holder.toggleInactiveButton.setOnClickListener {
            toggleAccountStatus(data)
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun updateData(newDataList: List<UserSettings>) {
        dataList = newDataList
        notifyDataSetChanged()
    }

    private fun toggleAccountStatus(user: UserSettings) {
        val database = FirebaseDatabase.getInstance().getReference("DasoniAPP/users")
        val newStatus = !user.accountStatus
        database.child(user.uid).child("accountStatus").setValue(newStatus)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val constraintLayout6: View = itemView.findViewById(R.id.constraintLayout6)
        val constraintLayout7: View = itemView.findViewById(R.id.constraintLayout7)
        val textView6: TextView = itemView.findViewById(R.id.title_useritem)
        val emailTextView6: TextView = itemView.findViewById(R.id.text_idValue)
        val textView7: TextView = itemView.findViewById(R.id.title_useritem2)
        val emailTextView7: TextView = itemView.findViewById(R.id.text_idValue2)
        val toggleActiveButton: ImageView = itemView.findViewById(R.id.btn_toggle_active)
        val toggleInactiveButton: ImageView = itemView.findViewById(R.id.btn_toggle_inactive)
    }
}


data class UserSettings(val uid: String,val text: String, val email: String, val accountStatus: Boolean)
