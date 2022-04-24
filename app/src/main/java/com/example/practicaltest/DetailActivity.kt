package com.example.practicaltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val titleView: TextView = findViewById(R.id.detailPageTitle)
        val imageView: ImageView = findViewById(R.id.detailPageImage)
        val descriptionView: TextView = findViewById(R.id.detailPageDescription)
        val authorView: TextView = findViewById(R.id.detailPageAuthor)

        val title=intent.getStringExtra("Title")
        val author=intent.getStringExtra("Author")
        val url=intent.getStringExtra("Url")
        val imageUrl=intent.getStringExtra("ImageUrl")
        val description=intent.getStringExtra("Description")

        titleView.text = title
        Glide.with(this).load(imageUrl).into(imageView)
        descriptionView.text = description
        authorView.text = author
    }
}