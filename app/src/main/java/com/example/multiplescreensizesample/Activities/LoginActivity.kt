package com.example.multiplescreensizesample.Activities

import LoginResponse
import VolleySingleton
import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
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
import com.example.multiplescreensizesample.Api.RetrofitClient
import com.example.multiplescreensizesample.Api.URLs
import com.example.multiplescreensizesample.Models.SharedPrefManager
import com.example.multiplescreensizesample.Models.User
import com.example.multiplescreensizesample.R
import com.squareup.okhttp.FormEncodingBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.RequestBody
import kotlinx.android.synthetic.main.activity_login.*
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException
import java.util.concurrent.TimeUnit

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
            startActivity(Intent(this, MyIntentService::class.java))
        }

        etName = findViewById(R.id.etUserName)
        etPassword = findViewById(R.id.etPassword)
        progressBar = findViewById(R.id.progressBar)


        btnLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            //userLoginUsingVolley()
            //userLoginUsingRetrofit()
            //CallLoginUsingAsyncTask().execute()
            OpenHomeScreen()

        }

    }

    private fun OpenHomeScreen(){
        intent = Intent(this, MyIntentService::class.java)
        startService(intent)
    }

    private fun userLoginUsingVolley() {

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

                        val user =
                            User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email")
                            )
                        SharedPrefManager.getInstance(applicationContext).userLogin(user)
                        finish()
                        startActivity(Intent(applicationContext, MyIntentService::class.java))
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


    private fun userLoginUsingRetrofit() {
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


        RetrofitClient.instance.userLogin(userName, password)

            .enqueue(object : retrofit2.Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: retrofit2.Response<LoginResponse>
                ) {
                    progressBar.visibility = View.GONE
                    if (!response.body()?.status!!) {
                        val message = response.body()?.message
                        Toast.makeText(
                            applicationContext,
                            message,
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            })

    }


    @SuppressLint("StaticFieldLeak")
    inner class CallLoginUsingAsyncTask() : AsyncTask<Void, Void, String>() {
        var email = "testintellore@yopmmail.com"
        var password = "apple123"
        var response = "-0"

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!response.equals("-0", ignoreCase = true)) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.has("status")) {
                        val status = jsonObject.getBoolean("status")
                        if (status) {
                            val message = jsonObject.getString("message")
                            Toast.makeText(
                                applicationContext,
                                message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }


        override fun doInBackground(vararg params: Void?): String? {
            val email = "testintellore@yopmmail.com"
            val password = "apple123"
            response = registerUser(email, password).toString()
            return ""
        }

    }

    fun registerUser(name: String?, mobile: String?): String? {
        val formBody: com.squareup.okhttp.RequestBody? = FormEncodingBuilder()
            .add("name", name)
            .add("mobile", mobile)
            .build()
        return formBody?.let {
            postServerCall(
                "https://192.168.1.35/androidphpmysql/registrationapi.php?apicall=login",
                it
            )
        }
    }

}

fun postServerCall(
    url: String,
    formBody: RequestBody
): String {
    val TIMEOUT: Long = 600
    try {
        val buffer = Buffer()
        formBody.writeTo(buffer)
    } catch (e: IOException) {
    }
    return try {
        val client = OkHttpClient()
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS)
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS)
        client.setWriteTimeout(TIMEOUT, TimeUnit.SECONDS)
        val request = com.squareup.okhttp.Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        val response = client.newCall(request).execute()
        val statusCode = response.code()
        Log.i("LoginActivity","StatusCode:$statusCode")
        val status = response.isSuccessful
        val res = response.body().string()
        return res
    } catch (e: Exception) {
        "-0"
    }
}




