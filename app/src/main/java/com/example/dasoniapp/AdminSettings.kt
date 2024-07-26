package com.example.dasoniapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminSettings : Fragment() {

    private lateinit var adapter: AdminSettingsAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("DasoniAPP/users")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_settings, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewAdminSettings)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val userSettings = mutableListOf<UserSettings>()
        adapter = AdminSettingsAdapter(userSettings)
        recyclerView.adapter = adapter

        fetchUserData()

        return view
    }

    private fun fetchUserData() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserUid == null) {
            return
        }

        database.child(currentUserUid).child("managedUsers").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val managedUsers = dataSnapshot.children.map { it.value.toString() }.toSet()
                val users = mutableListOf<UserSettings>()

                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (userSnapshot in snapshot.children) {
                            val uid = userSnapshot.key
                            val name = userSnapshot.child("name").getValue(String::class.java)
                            val email = userSnapshot.child("email").getValue(String::class.java)
                            val accountStatus = userSnapshot.child("accountStatus").getValue(Boolean::class.java)
                            if (uid != null && name != null && email != null && accountStatus != null && managedUsers.contains(uid)) {
                                users.add(UserSettings(uid, name, email, accountStatus))
                            }
                        }
                        adapter.updateData(users)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("AdminSettings", "Failed to read value.", error.toException())
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("AdminSettings", "Failed to read managed users.", databaseError.toException())
            }
        })
    }


//    override fun onUserClick(user: UserSettings) {
//        Toast.makeText(context, "User clicked: ${user.text}", Toast.LENGTH_SHORT).show()
//    }

    companion object {
        @JvmStatic
        fun newInstance() = AdminSettings()
    }
}
