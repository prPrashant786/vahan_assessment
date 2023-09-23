package com.example.vahan

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("search")// Replace with the actual API endpoint
    fun getUniversities(): Call<List<University>>
}