package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 1
    lateinit var progress: ProgressBar
    lateinit var postList: ArrayList<Post>
    lateinit var adapter: PostListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        //Initialise variables and UI
        postList = ArrayList()
        setContentView(R.layout.activity_main)
        adapter = PostListAdapter(this)
        adapter.setPost(postList)

        progress = findViewById(R.id.progress_bar)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        syncContact()

    }

    private fun syncContact() {
        val url = getString(R.string.url_server) + getString(R.string.url_user_read)

        //Display progress bar

        //Delete all user records
        postList.clear()
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Process the JSON
                try{
                    if(response != null){
                        val strResponse = response.toString()
                        val jsonResponse  = JSONObject(strResponse)
                        val jsonArray: JSONArray = jsonResponse.getJSONArray("records")

                        val size: Int = jsonArray.length()

                        for(i in 0..size-1){
                            var jsonUser: JSONObject = jsonArray.getJSONObject(i)
                            var post: Post = Post(jsonUser.getString("name"),
                                jsonUser.getString("postId"),
                                jsonUser.getString("postDesc"),
                                jsonUser.getInt("likes"),
                                jsonUser.getString("created_at"))

                            postList.add(post)
                        }
                    }
                }catch (e:Exception){
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
        jsonObjectRequest.tag = TAG
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    override fun onBackPressed() {

        MySingleton.getInstance(this).cancelRequest(TAG)
        super.onBackPressed()
    }

    companion object{
        const val TAG = "com.example.myapplication"
    }
}
