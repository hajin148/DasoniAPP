package com.example.dasoniapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_main)

        val btnUserManagement: Button = findViewById(R.id.btn_admin_user_management)
        val indicatorUserManagement: View = findViewById(R.id.indicator_user_management)

        // Show the indicator when the button is selected
        indicatorUserManagement.visibility = View.VISIBLE

        // Add the fragment if it's not already added
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AdminUserManagement.newInstance())
                .commitNow()
        }
    }
}
