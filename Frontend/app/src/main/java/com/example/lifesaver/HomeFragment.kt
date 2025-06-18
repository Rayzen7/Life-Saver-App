package com.example.lifesaver

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import java.util.logging.Handler

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewPager: ViewPager2
    private val handler = android.os.Handler(Looper.getMainLooper())
    private var currentPage = 0
    private lateinit var imageList: List<Int>

    private val sliderRunnable = object : Runnable {
        override fun run() {
            if (::imageList.isInitialized && imageList.isNotEmpty()) {
                currentPage = (currentPage + 1) % imageList.size
                viewPager.setCurrentItem(currentPage, true)
                handler.postDelayed(this, 3000)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.imageSLider)
        imageList = listOf(R.drawable.about_image1, R.drawable.about_image2, R.drawable.about_image3)
        viewPager.adapter = HomeSliderImageItem(imageList)

        handler.post(sliderRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(sliderRunnable)
    }
}