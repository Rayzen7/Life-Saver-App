package com.example.lifesaver

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import setCurrentItemWithDuration

class MainOnboarding : AppCompatActivity() {
    private lateinit var boardingPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.onboarding_main)

        boardingPager = findViewById<ViewPager2>(R.id.boardingPager)
        val btnNext = findViewById<Button>(R.id.boardingNext)

        val adapter = OnboardingPagerAdapter(this)
        boardingPager.adapter = adapter

        // Scroll Nonaktif
        boardingPager.isUserInputEnabled = false

        btnNext.setOnClickListener({
            if (boardingPager.currentItem < adapter.itemCount - 2) {
                boardingPager.setCurrentItemWithDuration(boardingPager.currentItem + 1, 200)
            } else {
                boardingPager.setCurrentItemWithDuration(boardingPager.currentItem + 1, 200)
                btnNext.visibility = View.GONE
            }
        })
    }
}
