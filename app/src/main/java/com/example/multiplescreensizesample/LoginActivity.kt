package com.example.multiplescreensizesample

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPassword: EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvRegister.setOnClickListener(View.OnClickListener {
            finish()
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        })

        if (SharedPrefManager.getInstance(this).isLoggedIn) {
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        etName = findViewById(R.id.etUserName)
        etPassword = findViewById(R.id.etPassword)
        progressBar = findViewById(R.id.progressBar)


        btnLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            userLogin()
        }

    }

    private fun userLogin() {

        var userName = etUserName.text.toString()
        var password = etPassword.text.toString()

        if (TextUtils.isEmpty(userName)) {
            etName.error = "Please enter username"
            etName.requestFocus()
            return
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.error = "Please enter Password"
            etPassword.error
            return
        }

        val stringRequest = object : StringRequest(Request.Method.POST,
            URLs.URL_LOGIN,
            Response.Listener { response ->
                progressBar.visibility = View.GONE
                try {
                    val obj = JSONObject(response)
                    Log.i("loginResponse", obj.toString())

                    if (!obj.getBoolean("error")) {
                        Toast.makeText(
                            applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_LONG
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
                progressBar.visibility = View.GONE
                Log.i("Login", "params:${error.message}")
                Toast.makeText(
                    applicationContext,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["email"] = userName
                params["password"] = password
                Log.i("Login", "params:$params")
                return params
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

}