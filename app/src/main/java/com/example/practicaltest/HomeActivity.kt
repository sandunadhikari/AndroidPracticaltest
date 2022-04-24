package com.example.practicaltest

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.practicaltest.Adapters.CardAdapter
import com.example.practicaltest.Adapters.CardItemClicked
import com.example.practicaltest.Models.Employee
import com.example.practicaltest.Models.News
import com.example.practicaltest.Util.Constants.Companion.API_KEY
import com.example.practicaltest.Util.Constants.Companion.BASE_URL
import com.example.practicaltest.Util.Constants.Companion.brakingUrl
import com.example.practicaltest.Util.Constants.Companion.topUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), CardItemClicked {
    private lateinit var cAdapter: CardAdapter
    private lateinit var bAdapter: CardAdapter
    private lateinit var listUsers: MutableList<Employee>
    private lateinit var handler: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        handler = DatabaseHelper(this)

        val userEmail = intent.getStringExtra("Email")
        val pref = getPreferences(Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("EMAIL",userEmail)
        editor.commit()
        if (userEmail != null) {
            logData(userEmail)
        }
        
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> navigateLogout(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateLogout(item: MenuItem) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to Logout?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Delete selected note from database
                val pref = getPreferences(Context.MODE_PRIVATE)
                val user_mail = pref.getString("EMAIL","")
                if (user_mail != null) {
                    logoutData(user_mail)
                }
            }
            .setNegativeButton("No") { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()
        alert.show()
    }

    private fun logoutData(useremail:String){
        listUsers = ArrayList()

        GlobalScope.launch(Dispatchers.IO) {
            val result= handler.getAllUser()
            listUsers.clear()
            listUsers.addAll(result!!)
            if(listUsers.size > 0) {
                val myEmployee: Employee? = listUsers.find { it.email == useremail }
                if (myEmployee != null) {
                    handler.updateUser(Employee(myEmployee.id,myEmployee.name,myEmployee.email,myEmployee.password,0))
                }
            }
            launch (Dispatchers.Main){
                val pref = getPreferences(Context.MODE_PRIVATE)
                val editor = pref.edit()
                editor.clear()
                editor.commit()

                val intent = Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun logData(useremail:String){
        listUsers = ArrayList()

        GlobalScope.launch(Dispatchers.IO) {
            val result= handler.getAllUser()
            listUsers.clear()
            listUsers.addAll(result!!)
            if(listUsers.size > 0) {
                val myEmployee: Employee? = listUsers.find { it.email == useremail }
                if (myEmployee != null) {
                    handler.updateUser(Employee(myEmployee.id,myEmployee.name,myEmployee.email,myEmployee.password,1))
                }
            }
            launch (Dispatchers.Main){

            }
        }
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