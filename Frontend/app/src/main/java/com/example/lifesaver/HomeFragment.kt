package com.example.lifesaver

import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.adapter.ProductAdapter
import com.example.lifesaver.api.local.MySharedPreference
import com.example.lifesaver.api.model.Product
import org.json.JSONObject

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewPager: ViewPager2
    private val handler = android.os.Handler(Looper.getMainLooper())
    private var currentPage = 0
    private lateinit var imageList: List<Int>

    private lateinit var productRecyclerView: RecyclerView
    private val productList = mutableListOf<Product>()
    private lateinit var homeName: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        productRecyclerView = view.findViewById(R.id.productAll)
        productRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        homeName = view.findViewById(R.id.homeName)
        fetchProduct()
        fetchMe()

        return view
    }

    private val sliderRunnable = object : Runnable {
        override fun run() {
            if (::imageList.isInitialized && imageList.isNotEmpty()) {
                currentPage = (currentPage + 1) % imageList.size
                viewPager.setCurrentItem(currentPage, true)
                handler.postDelayed(this, 3000)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val bottomNav = requireActivity().findViewById<View>(R.id.bottomNavigationView)

        searchEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                bottomNav.visibility = View.GONE
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                bottomNav.visibility = View.GONE
            }

            override fun afterTextChanged(p0: Editable?) {
                bottomNav.visibility = View.GONE
            }
        })

        searchEditText.setOnFocusChangeListener {_, hasFocus ->
            if (hasFocus) {
                bottomNav.visibility = View.GONE
            } else {
                bottomNav.visibility = View.VISIBLE
            }
        }

        viewPager = view.findViewById(R.id.imageSLider)
        imageList = listOf(R.drawable.about_image1, R.drawable.about_image2, R.drawable.about_image3)
        viewPager.adapter = HomeSliderImageItem(imageList)

        handler.post(sliderRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(sliderRunnable)
    }

    fun fetchProduct() {
        Thread {
            try {
                val response = HttpHandler().request("/product", "GET")
                val jsonObject = JSONObject(response.body)
                val jsonArray = jsonObject.getJSONArray("product")

                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    productList.add(
                        Product(
                            id = item.getInt("id"),
                            name = item.getString("name"),
                            desc = item.getString("desc"),
                            image = item.getString("image"),
                            category = item.getJSONObject("category").getString("name"),
                            price = item.getInt("price"),
                            dosis = item.getString("dosis"),
                            quantity = item.getInt("quantity"),

                        )
                    )
                }

                requireActivity().runOnUiThread {
                    productRecyclerView.adapter = ProductAdapter(productList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun fetchMe() {
        Thread {
            try {
                val username = MySharedPreference.getUser(requireContext())
                requireActivity().runOnUiThread {
                    homeName.text = "Hi, ${username}"
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}