package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.local.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainRegister : AppCompatActivity() {
    lateinit var usernameInput: EditText
    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register_main)

        val registerBtn = findViewById<Button>(R.id.registerBtn)
        usernameInput = findViewById(R.id.usernameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)

        registerBtn.setOnClickListener({
            registerPost()
        })
    }

    fun registerPost() {
        if (usernameInput.text.toString() == "" || emailInput.text.toString() == "" || passwordInput.text.toString() == "") {
            Helper.showToast(this@MainRegister, "The Field Must Be Required")
            return
        }

        try {
            lifecycleScope.launch(Dispatchers.IO) {
                val json = JSONObject().apply {
                    put("username", usernameInput.text.toString())
                    put("email", emailInput.text.toString())
                    put("password", passwordInput.text.toString())
                }

                val response = HttpHandler().request("/users", "POST", null, json.toString())
                val jsonBody = JSONObject(response.body)
                val message = jsonBody.getString("message")


                withContext(Dispatchers.Main) {
                    if (response.code in 200.. 300) {
                        Helper.showToast(this@MainRegister, message)

                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@MainRegister, MainLogin::class.java)
                            startActivity(intent)
                            finish()
                        }, 2000)
                    } else {
                        Helper.showToast(this@MainRegister, message)
                    }
                }
            }
        } catch (e: Exception) {
            Helper.log(e.message!!)
            Helper.showToast(this@MainRegister, "Error ${e.message}")
        }
    }
}