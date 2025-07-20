package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.local.MySharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class UserFragment : Fragment(R.layout.fragment_user) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        val logout = view?.findViewById<Button>(R.id.logoutBtn)

        logout?.setOnClickListener() {
            handleLogout()
        }

        return  view
    }

    fun handleLogout() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val token = MySharedPreference.getToken(requireContext())
                val body = JSONObject().apply {  }

                val response = HttpHandler().request("/auth/logout", "POST", token, body.toString())
                MySharedPreference.deleteToken(requireContext())
                val message = JSONObject(response.body).getString("message")

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), message.toString(), Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(requireContext(), MainLogin::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }, 2000)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}