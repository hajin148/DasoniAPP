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
        val btnDashboard: Button = findViewById(R.id.btn_admin_dashboard)
        val indicatorUserManagement: View = findViewById(R.id.indicator_user_management)
        val indicatorDashboard: View = findViewById(R.id.indicator_dasboard)

        indicatorUserManagement.visibility = View.VISIBLE

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AdminUserManagement.newInstance())
                .commitNow()
        }

        btnDashboard.setOnClickListener {
            indicatorUserManagement.visibility = View.GONE
            indicatorDashboard.visibility = View.VISIBLE

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AdminDashboard.newInstance())
                .commitNow()
        }

        btnUserManagement.setOnClickListener {
            indicatorUserManagement.visibility = View.VISIBLE
            indicatorDashboard.visibility = View.GONE

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AdminUserManagement.newInstance())
                .commitNow()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}
