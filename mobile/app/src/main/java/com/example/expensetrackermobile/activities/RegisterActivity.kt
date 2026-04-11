package com.example.expensetrackermobile.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.expensetrackermobile.R
import com.example.expensetrackermobile.api.ApiClient
import com.example.expensetrackermobile.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val firstname = findViewById<EditText>(R.id.firstname)
        val lastname = findViewById<EditText>(R.id.lastname)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val btn = findViewById<Button>(R.id.registerBtn)

        btn.setOnClickListener {

            val user = User(
                firstname.text.toString(),
                lastname.text.toString(),
                email.text.toString(),
                password.text.toString()
            )

            ApiClient.instance.register(user)
                .enqueue(object : Callback<User> {

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {

                            Toast.makeText(
                                this@RegisterActivity,
                                "Registered Successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            // ✅ ADD THIS PART (NAVIGATION)
                            startActivity(
                                Intent(this@RegisterActivity, LoginActivity::class.java)
                            )
                            finish() // optional: prevents going back

                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registration Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }
}