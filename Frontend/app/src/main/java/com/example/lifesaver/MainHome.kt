package com.example.lifesaver

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_main)

        val navView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commit()
        }

        navView.setOnItemSelectedListener { item ->
            val menuView = navView.getChildAt(0) as ViewGroup
            for (i in 0 until menuView.childCount) {
                val iconView = menuView.getChildAt(i)
                iconView.alpha = if (item.itemId == iconView.id) 0f else 1f
                iconView.animate().alpha(1f).setDuration(400).start()
            }

            val selectedFragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_news -> NewsFragment()
                R.id.nav_history -> HistoryFragment()
                else -> null
            }

            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, it)
                    .commit()
            }

            true
        }
    }
}