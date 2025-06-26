package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.local.Helper
import com.example.lifesaver.api.local.MySharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainLogin : AppCompatActivity() {

    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login_main)

        val registerBtn = findViewById<Button>(R.id.registerBtn)
        val loginBtn = findViewById<Button>(R.id.loginSuccess)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)

        registerBtn.setOnClickListener({
            val intent = Intent(this, MainRegister::class.java)
            startActivity(intent)
        })

        loginBtn.setOnClickListener({
            loginPost()
        })
    }

    fun loginPost() {
        if (emailInput.text.toString() == "" || passwordInput.text.toString() == "") {
            Toast.makeText(this, "Username / Password must be Required", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            lifecycleScope.launch(Dispatchers.IO) {
                val json = JSONObject().apply {
                    put("email", emailInput.text.toString())
                    put("password", passwordInput.text.toString())
                }

                val response = HttpHandler().request("/auth/login", "POST", null, json.toString())
                val jsonBody = JSONObject(response.body)
                val message = jsonBody.getString("message")

                if (response.code in 200..300) {
                    val token = jsonBody.getString("access_token")

                    val response = HttpHandler().request("/auth/me", "GET", token)
                    val jsonObject = JSONObject(response.body)
                    val user = jsonObject.getJSONObject("user")
                    val username = user.getString("username")

                    withContext(Dispatchers.Main) {
                        MySharedPreference.saveToken(this@MainLogin, token)
                        MySharedPreference.saveUser(this@MainLogin, username)
                        Toast.makeText(this@MainLogin, message.toString(), Toast.LENGTH_SHORT).show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@MainLogin, MainOnboarding::class.java)
                            startActivity(intent)
                            finish()
                        }, 2000)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Helper.showToast(this@MainLogin, message)
                    }
                }
            }
        } catch (e: Exception) {
            Helper.log(e.message!!)
            Helper.showToast(this@MainLogin, "Error : ${e.message}")
        }
    }
}