package com.example.practicaltest

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.practicaltest.Adapters.CardAdapter
import com.example.practicaltest.Adapters.CardItemClicked
import com.example.practicaltest.Models.News
import com.example.practicaltest.Util.Constants.Companion.API_KEY
import com.example.practicaltest.Util.Constants.Companion.BASE_URL
import com.example.practicaltest.Util.Constants.Companion.brakingUrl
import com.example.practicaltest.Util.Constants.Companion.topUrl

class HomeActivity : AppCompatActivity(), CardItemClicked {
    private lateinit var cAdapter: CardAdapter
    private lateinit var bAdapter: CardAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val searchView: LinearLayout = findViewById(R.id.search_view)

        val allbreaking: Button = findViewById(R.id.allbreaking)
        val topnews: Button = findViewById(R.id.topnews)

        val breakingrecyclerView: RecyclerView = findViewById(R.id.breaking_news_recycler_main)
        val toprecyclerView: RecyclerView = findViewById(R.id.top_news_recycler_main)


        breakingrecyclerView.layoutManager = LinearLayoutManager(this)
        cAdapter = CardAdapter(this,this)
        breakingrecyclerView.adapter = cAdapter

        toprecyclerView.layoutManager = LinearLayoutManager(this)
        bAdapter = CardAdapter(this,this)
        toprecyclerView.adapter = bAdapter

        fetchData(brakingUrl,cAdapter)
        fetchData(topUrl,bAdapter)

        searchView.setOnClickListener {
            var Intent = Intent(this,SearchActivity::class.java)
            startActivity(Intent)
        }

        allbreaking.setOnClickListener {
            callAllbreaking()
        }

        topnews.setOnClickListener {
            callAlltopnews()
        }
    }

    override fun onBackPressed(){

    }

    private fun callAllbreaking(){
        val intent = Intent(this@HomeActivity,SearchActivity::class.java)
        intent.putExtra("BREAKINGNEWS",brakingUrl)
        startActivity(intent)
    }

    private fun callAlltopnews(){
        val intent = Intent(this@HomeActivity,SearchActivity::class.java)
        intent.putExtra("TOPNEWS",topUrl)
        startActivity(intent)
    }

    private fun fetchData(url:String, xmlReaderAdapter: CardAdapter) {

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage"),
                        newsJsonObject.getString("description")
                    )
                    newsArray.add(news)
                }

                xmlReaderAdapter.updateNews(newsArray)
            },
            {
                // Toast.makeText(this, "volly exception", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val intent = Intent(this@HomeActivity,DetailActivity::class.java)
        intent.putExtra("Title",item.title)
        intent.putExtra("Author",item.author)
        intent.putExtra("Url",item.url)
        intent.putExtra("ImageUrl",item.imageUrl)
        intent.putExtra("Description",item.description)
        startActivity(intent)
    }
}