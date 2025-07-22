package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userPhone: TextView
    private lateinit var userNik: TextView
    private lateinit var userCreated: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        val logout = view.findViewById<Button>(R.id.logoutBtn)
        val editUserBtn = view.findViewById<Button>(R.id.editUserBtn)

        userName = view.findViewById(R.id.userName)
        userEmail = view.findViewById(R.id.userEmail)
        userPhone = view.findViewById(R.id.userPhone)
        userNik = view.findViewById(R.id.userNik)
        userCreated = view.findViewById(R.id.userCreated)

        logout.setOnClickListener() {
            handleLogout()
        }

        editUserBtn.setOnClickListener() {
            val intent = Intent(requireContext(), EditUserMain::class.java)
            startActivity(intent)
        }

        getUser()
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

    fun getUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val token = MySharedPreference.getToken(requireContext())
                val response = HttpHandler().request("/auth/me", "GET", token)

                val body = JSONObject(response.body).getJSONObject("user")
                withContext(Dispatchers.Main) {
                    userName.text = body.getString("username")
                    userEmail.text = body.getString("email")
                    userPhone.text = body.optString("phone", "-")
                    userNik.text = body.optString("nik", "-")
                    userCreated.text = body.getString("created_at").substring(0, 10)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}