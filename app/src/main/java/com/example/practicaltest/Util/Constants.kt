package com.example.practicaltest.Util

class Constants {
    companion object{
        const val API_KEY = "9caf9106ea20488581e20b523c1b2216"
        const val BASE_URL = "https://newsapi.org"
        const val brakingUrl = "${BASE_URL}/v2/top-headlines?country=us&apiKey=${API_KEY}&pageSize=2&page=1"
        const val topUrl = "${BASE_URL}/v2/everything?q=bitcoin&apiKey=${API_KEY}&pageSize=3&page=1"
    }
}