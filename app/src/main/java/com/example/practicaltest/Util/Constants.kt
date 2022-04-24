package com.example.practicaltest.Util

class Constants {
    companion object{
        const val API_KEY = "86f63ff0d3e5461ebd6909e54dbe6f9f"
        const val BASE_URL = "https://newsapi.org"
        const val brakingUrl = "${BASE_URL}/v2/top-headlines?country=us&apiKey=${API_KEY}&pageSize=2&page=1"
        const val topUrl = "${BASE_URL}/v2/everything?q=bitcoin&apiKey=${API_KEY}&pageSize=3&page=1"
    }
}