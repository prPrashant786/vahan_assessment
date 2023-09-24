package com.example.vahan
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



class DataRefreshService : Service() {

    interface DataRefreshCallback {
        fun onDataRefreshed(data: List<University>)
    }

    private val handler = Handler(Looper.getMainLooper())
    private val refreshInterval = 10000L

    private var dataRefreshCallback: DataRefreshCallback? = null

    fun setCallback(callback: DataRefreshCallback) {
        dataRefreshCallback = callback
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "DataRefreshService started")

        // Fetch data from the API here
        fetchDataFromApi()

        // Schedule the next refresh after 10 seconds
        handler.postDelayed(refreshRunnable, refreshInterval)

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        // Return an instance of LocalBinder
        return LocalBinder()
    }

    override fun onDestroy() {
        Log.d(TAG, "DataRefreshService destroyed")

        // Stop the refresh task when the service is destroyed
        handler.removeCallbacks(refreshRunnable)
        super.onDestroy()
    }

    private val refreshRunnable = object : Runnable {
        override fun run() {
            // Fetch data from the API here
            fetchDataFromApi()

            // Schedule the next refresh after 10 seconds
            handler.postDelayed(this, refreshInterval)
        }
    }

    private fun fetchDataFromApi() {
        Toast.makeText(this,"Refreshing API ",Toast.LENGTH_LONG).show()
        Log.d(TAG, "Fetching data from API")

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
                    dataRefreshCallback?.onDataRefreshed(universities)
                    // Update your app's data or UI with the fetched data here
                    Log.d(TAG, "Fetched data: $universities")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "API Error: ${response.code()}, $errorBody")
                }
            }

            override fun onFailure(call: Call<List<University>>, t: Throwable) {
                Log.e(TAG, "Network Error: ${t.message}", t)
            }

        })
        Log.d(TAG, "Data fetched successfully")
    }
    inner class LocalBinder : Binder() {
        fun getService(): DataRefreshService {
            return this@DataRefreshService
        }
    }


    companion object {
        private const val TAG = "DataRefreshService"
    }
}
