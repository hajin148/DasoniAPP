package com.example.dasoniapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AdminUserManagement : Fragment() {

    private lateinit var adapter: AdminUserManagementAdapter
    private lateinit var database: DatabaseReference
    private lateinit var countTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("DasoniAPP/users")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_user_management, container, false)

        countTextView = view.findViewById(R.id.textView_countPeople)


        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewUserManagement)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val usernames = mutableListOf<UserName>()
        adapter = AdminUserManagementAdapter(usernames)
        recyclerView.adapter = adapter

        fetchUserData()

        return view
    }

    private fun fetchUserData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<UserName>()
                for (userSnapshot in snapshot.children) {
                    val name = userSnapshot.child("name").getValue(String::class.java)
                    val email = userSnapshot.child("email").getValue(String::class.java)
                    if (name != null && email != null) {
                        users.add(UserName(name, email))
                        Log.d("AdminUserManagement", "Fetched user: $name, $email")
                    } else {
                        Log.d("AdminUserManagement", "Missing data for user: ${userSnapshot.key}")
                    }
                }
                adapter.updateData(users)
                countTextView.text = "총 ${users.size}명"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AdminUserManagement", "Failed to read value.", error.toException())
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = AdminUserManagement()
    }
}
