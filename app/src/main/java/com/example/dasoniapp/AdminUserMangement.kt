package com.example.dasoniapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminUserManagement : Fragment(), AdminUserManagementAdapter.OnUserClickListener {

    private lateinit var adapter: AdminUserManagementAdapter
    private lateinit var database: DatabaseReference
    private lateinit var countTextView: TextView
    private lateinit var selectTextView: TextView
    private val selectedUsers = mutableListOf<UserName>()
    private val allUsers = mutableListOf<UserName>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("DasoniAPP/users")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_user_management, container, false)

        countTextView = view.findViewById(R.id.textView_countPeople)
        selectTextView = view.findViewById(R.id.textView_select)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewUserManagement)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val usernames = mutableListOf<UserName>()
        adapter = AdminUserManagementAdapter(usernames, this)
        recyclerView.adapter = adapter

        fetchUserData()

        selectTextView.setOnClickListener {
            addManagedUsers()
        }

        return view
    }

    private fun fetchUserData() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        database.child(currentUserUid).child("managedUsers").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val managedUsers = dataSnapshot.children.map { it.value.toString() }.toSet()
                val users = mutableListOf<UserName>()

                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (userSnapshot in snapshot.children) {
                            val uid = userSnapshot.key
                            val name = userSnapshot.child("name").getValue(String::class.java)
                            val email = userSnapshot.child("email").getValue(String::class.java)
                            if (uid != null && name != null && email != null && !managedUsers.contains(uid)) {
                                users.add(UserName(uid, name, email))
                            }
                        }
                        allUsers.clear()
                        allUsers.addAll(users)
                        adapter.updateData(users)
                        countTextView.text = "총 ${users.size}명"
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("AdminUserManagement", "Failed to read value.", error.toException())
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("AdminUserManagement", "Failed to read managed users.", databaseError.toException())
            }
        })
    }

    override fun onUserClick(user: UserName) {
//        Toast.makeText(context, "User clicked: ${user.text}", Toast.LENGTH_SHORT).show()
        adapter.toggleSelection(user)
        if (user.isSelected) {
            selectedUsers.add(user)
        } else {
            selectedUsers.remove(user)
        }
    }

    private fun addManagedUsers() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val managedUsersRef = database.child(currentUserUid).child("managedUsers")

        managedUsersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val managedUsers = mutableListOf<String>()
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        managedUsers.add(userSnapshot.getValue(String::class.java)!!)
                    }
                }

                for (user in selectedUsers) {
                    managedUsers.add(user.uid)
                }

                managedUsersRef.setValue(managedUsers).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Managed users updated", Toast.LENGTH_SHORT).show()
                        allUsers.removeAll(selectedUsers)
                        selectedUsers.clear()
                        adapter.updateData(allUsers)
                        countTextView.text = "총 ${allUsers.size}명"
                    } else {
                        Toast.makeText(context, "Failed to update managed users", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("AdminUserManagement", "Failed to update managed users", error.toException())
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance() = AdminUserManagement()
    }
}

