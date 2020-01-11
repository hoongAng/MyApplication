package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        buttonSave.setOnClickListener {
            saveUser()
        }
    }

    private fun saveUser(){

        if(TextUtils.isEmpty(editTextName.text)){
            editTextName.setError(getString(R.string.error_value_required))
            return
        }
        if(TextUtils.isEmpty(editTextContact.text)){
            editTextContact.setError(getString(R.string.error_value_required))
            return
        }
        if(TextUtils.isEmpty(editTextPassword.text)){
            editTextContact.setError(getString(R.string.error_value_required))
            return
        }
        val name = editTextName.text.toString()
        val contact = editTextContact.text.toString()
        val password = editTextPassword.text.toString()
        val conPassword = editTextConPassword.text.toString()
        val intent = Intent()

        fun createUser(user: User) {
            val url =
                getString(R.string.url_server) + getString(R.string.url_user_create) + "?name=" + user.name +
                        "&contact=" + user.contact + "&password=" + user.password


            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener { response ->
                    // Process the JSON
                    try {
                        if (response != null) {
                            val success: String = response.get("success").toString()

                            if (success.equals("1")) {
                                Toast.makeText(
                                    applicationContext,
                                    "Record saved",
                                    Toast.LENGTH_LONG
                                ).show()
                                //Add record to user list
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Record not saved",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.d("Main", "Response: %s".format(e.message.toString()))

                    }
                },
                Response.ErrorListener { error ->
                    Log.d("Main", "Response: %s".format(error.message.toString()))
                }
            )

            //Volley request policy, only one time request
            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                0, //no retry
                1f
            )

            // Access the RequestQueue through your singleton class.
            jsonObjectRequest.tag = MainActivity.TAG
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

        }
        if(contact!=null&&name!=null&&password!=null) {
            if (password==conPassword) {
                createUser(User(contact, name, password))
            }
            else
            {
                Intent(this, AddActivity::class.java)
                startActivity(intent)
            }
        }
        else
        {
            Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

    }

}
