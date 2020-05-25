package com.example.multiplescreensizesample

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        progressBar = findViewById(R.id.progressBar)
        editTextUsername = findViewById(R.id.etUserName)
        editTextEmail = findViewById(R.id.etEmail)
        editTextPassword = findViewById(R.id.etPassword)
        radioGroupGender = findViewById(R.id.radioGender)

        textViewLogin.setOnClickListener(View.OnClickListener {
            finish()
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        })

        btnRegister.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE
            registerUser()
        })
    }

    private fun registerUser() {
        val username = editTextUsername.text.toString().trim { it <= ' ' }
        val email = editTextEmail.text.toString().trim { it <= ' ' }
        val password = editTextPassword.text.toString().trim { it <= ' ' }

        val gender =
            (findViewById<View>(radioGroupGender.checkedRadioButtonId) as RadioButton).text.toString()

        if (TextUtils.isEmpty(username)) {
            editTextUsername.error = "Please enter username"
            editTextUsername.requestFocus()
            progressBar.visibility = View.INVISIBLE
            return
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.error = "Please enter your email"
            editTextEmail.requestFocus()
            progressBar.visibility = View.INVISIBLE
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Enter a valid email"
            editTextEmail.requestFocus()
            progressBar.visibility = View.INVISIBLE
            return
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.error = "Enter a password"
            editTextPassword.requestFocus()
            progressBar.visibility = View.INVISIBLE
            return
        }

        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_REGISTER,
            Response.Listener { response ->
                progressBar.visibility = View.INVISIBLE

                try {
                    val obj = JSONObject(response)
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(
                            applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()

                        val userJson = obj.getJSONObject("user")

                        val user = User(
                            userJson.getInt("id"),
                            userJson.getString("username"),
                            userJson.getString("email"),
                            userJson.getString("gender")
                        )

                        SharedPrefManager.getInstance(applicationContext).userLogin(user)

                        finish()
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                    } else {
                        Toast.makeText(
                            applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(
                    applicationContext,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["email"] = email
                params["password"] = password
                params["gender"] = gender
                return params
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }
}
