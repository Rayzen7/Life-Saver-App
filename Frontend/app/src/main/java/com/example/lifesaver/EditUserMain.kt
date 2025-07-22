package com.example.lifesaver

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lifesaver.api.HttpHandler
import com.example.lifesaver.api.local.MySharedPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class EditUserMain : AppCompatActivity() {
    private lateinit var editUserBack: ImageView
    private lateinit var nameEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var emailEdit: EditText
    private lateinit var phoneEdit: EditText
    private lateinit var nikEdit: EditText
    private lateinit var editBtn: Button
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.edituser_main)

        editUserBack = findViewById(R.id.editUserBack)
        editBtn = findViewById(R.id.editBtn)

        nameEdit = findViewById(R.id.nameEdit)
        passwordEdit = findViewById(R.id.passwordEdit)
        emailEdit = findViewById(R.id.emailEdit)
        phoneEdit = findViewById(R.id.phoneEdit)
        nikEdit = findViewById(R.id.nikEdit)

        editUserBack.setOnClickListener() {
            val intent = Intent(this@EditUserMain, UserFragment::class.java)
            startActivity(intent)
        }

        editBtn.setOnClickListener() {
            editSubmit()
        }

        getMe()
    }

    fun getMe() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val token = MySharedPreference.getToken(this@EditUserMain)
                val response = HttpHandler().request("/auth/me", "GET", token)
                val body = JSONObject(response.body).getJSONObject("user")

                withContext(Dispatchers.Main) {
                    id = body.getInt("id")
                    nameEdit.setText(body.getString("username"))
                    emailEdit.setText(body.getString("email"))
                    phoneEdit.setText(body.getString("phone"))
                    nikEdit.setText(body.getString("nik"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun editSubmit() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("username", nameEdit.text.toString())
                    put("password", passwordEdit.text.toString())
                    put("email", emailEdit.text.toString())
                    put("phone", phoneEdit.text.toString())
                    put("nik", nikEdit.text.toString())
                }

                val token = MySharedPreference.getToken(this@EditUserMain)
                val response = HttpHandler().request("/users/${id}", "PUT", token, json.toString())
                val message = JSONObject(response.body).getString("message")

                if (response.code in 200..300) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditUserMain, "loading", Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            Toast.makeText(this@EditUserMain, message, Toast.LENGTH_SHORT).show()
                        }, 2000)

                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@EditUserMain, UserFragment::class.java)
                            startActivity(intent)
                        }, 3000)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@EditUserMain, "Nik & Phone Required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}