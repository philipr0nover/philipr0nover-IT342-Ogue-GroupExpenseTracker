package com.example.expensetrackermobile.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.expensetrackermobile.R
import com.example.expensetrackermobile.api.ApiClient
import com.example.expensetrackermobile.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val btn = findViewById<Button>(R.id.loginBtn)

        btn.setOnClickListener {

            val user = User("", "", email.text.toString(), password.text.toString())

            ApiClient.instance.login(user)
                .enqueue(object : Callback<User> {

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {

                            Toast.makeText(
                                this@LoginActivity,
                                "Login Successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            //startActivity(
                                //Intent(this@LoginActivity, MainActivity::class.java)
                            //)

                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Invalid Credentials",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }
}