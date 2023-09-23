package com.example.vahan

data class University(
    val name: String,
    val domains: List<String>,
    val country: String,
    val alpha_two_code: String,
    val web_pages: List<String>,
    val state_province: String?
)