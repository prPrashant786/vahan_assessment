package com.example.vahan

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var universityAdapter: UniversityAdapter

    private lateinit var loadingProgressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        loadingProgressBar.visibility = View.VISIBLE

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(20, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(20, TimeUnit.SECONDS)   // Write timeout (if needed)
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl("http://universities.hipolabs.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val call = apiService.getUniversities()

        call.enqueue(object : Callback<List<University>> {
            override fun onResponse(call: Call<List<University>>, response: Response<List<University>>) {
                if (response.isSuccessful) {
                    val universities = response.body() ?: emptyList()
                    universityAdapter = UniversityAdapter(universities)
                    recyclerView.adapter = universityAdapter
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "API Error: ${response.code()}, $errorBody")

                }
                loadingProgressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<University>>, t: Throwable) {
                Log.e(TAG, "Network Error: ${t.message}", t)

                Toast.makeText(this@MainActivity,"FAILLLLLLLLLLL",Toast.LENGTH_LONG).show()


                loadingProgressBar.visibility = View.GONE
            }
        })


    }
}