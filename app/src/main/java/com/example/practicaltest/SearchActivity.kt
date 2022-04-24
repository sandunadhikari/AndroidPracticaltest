package com.example.practicaltest

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES.BASE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.practicaltest.Adapters.CatItemClicked
import com.example.practicaltest.Adapters.CategoryAdapter
import com.example.practicaltest.Adapters.NewsItemClicked
import com.example.practicaltest.Adapters.NewsListAdapter
import com.example.practicaltest.Models.News
import com.example.practicaltest.Models.cat_data
import com.example.practicaltest.Util.Constants.Companion.API_KEY
import com.example.practicaltest.Util.Constants.Companion.BASE_URL

class SearchActivity : AppCompatActivity(), NewsItemClicked, CatItemClicked {

    private lateinit var mAdapter: NewsListAdapter
    var count=0
    var desh="in"
    var cat_data="general"
    var searchValue : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val recyclerView: RecyclerView = findViewById(R.id.recycle)
        val searchView: SearchView = findViewById(R.id.search_bar)
        val cat_recycle: RecyclerView = findViewById(R.id.cat_recycle)

        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = NewsListAdapter(this,this)
        recyclerView.adapter = mAdapter

        val breakingNewsUrl=intent.getStringExtra("BREAKINGNEWS")
        val topNewsUrl=intent.getStringExtra("TOPNEWS")

        if(breakingNewsUrl != null){
            fetchData(breakingNewsUrl)
        }else if(topNewsUrl != null){
            fetchData(topNewsUrl)
        }else {
            fetchData("")
        }

        val array= ArrayList<cat_data>()
        array.add((cat_data(R.drawable.general,"General")))
        array.add((cat_data(R.drawable.sports,"Sports")))
        array.add((cat_data(R.drawable.health,"Health")))
        array.add((cat_data(R.drawable.science,"Science")))
        array.add((cat_data(R.drawable.technology,"Technology")))
        array.add((cat_data(R.drawable.entertainment,"Entertainment")))
        cat_recycle.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val catAdapter=CategoryAdapter(array,this)
        cat_recycle.adapter=catAdapter

        val country = arrayListOf<String>(
            "INDIA",
            "USA",
            "RUSSIA",
            "CHINA",
            "New Zealand",
            "UNITED ARAB EMIRTAS"
        )
        val adapter = ArrayAdapter(
            this,
            R.layout.row_layout, R.id.text, country
        )

        val spinner: Spinner = findViewById(R.id.spinner)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                count= position
                if(position==null){
                    count=0
                }
//                // get selected item text
//                //Toast.makeText(this@MainActivity, "${parent.getItemAtPosition(position)}", Toast.LENGTH_SHORT).show()
                if(count==0) {
                    desh="in"
                    //Toast.makeText(this@MainActivity, "india", Toast.LENGTH_SHORT).show()
                }
                if(count==1){
                    //Toast.makeText(this@MainActivity, "usa country", Toast.LENGTH_SHORT).show()
                    desh="us"
                }
                if(count==2){
                    desh="ru"
                }
                if(count==3){
                    desh="ch"
                }
                if(count==4){
                    desh="nz"
                }
                if(count==5){
                    desh="ae"
                }
                fetchData("")
//
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // another interface callback
            }
        }


    }
    private fun fetchData(url :String) {
        var mainUrl  = ""
        if(url.isEmpty()){
            mainUrl = "${BASE_URL}/v2/top-headlines?country=${desh}&category=${cat_data}&apiKey=${API_KEY}"
        }else{
            mainUrl = url
        }

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            mainUrl,
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

                mAdapter.updateNews(newsArray)
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

    private fun queryData() {

        val url = "https://newsapi.org/v2/everything?q=${searchValue}&apiKey=86f63ff0d3e5461ebd6909e54dbe6f9f"
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

                mAdapter.updateNews(newsArray)
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
        val intent = Intent(this@SearchActivity,DetailActivity::class.java)
        intent.putExtra("Title",item.title)
        intent.putExtra("Author",item.author)
        intent.putExtra("Url",item.url)
        intent.putExtra("ImageUrl",item.imageUrl)
        intent.putExtra("Description",item.description)
        startActivity(intent)
    }

    override fun onItemClicked(item: cat_data) {
        if(item.name=="General"){
            cat_data="General"
        }
        if((item.name)=="Sports"){
            cat_data="Sports"
        }
        if((item.name)=="Health"){
            cat_data="Health"
        }
        if((item.name)=="Science"){
            cat_data="Science"
        }
        if((item.name)=="Technology"){
            cat_data="Technology"
        }
        if((item.name)=="Entertainment"){
            cat_data="Entertainment"
        }
        fetchData("")
        Toast.makeText(this, "wait your ${item.name} news is loading", Toast.LENGTH_SHORT).show()
    }


}