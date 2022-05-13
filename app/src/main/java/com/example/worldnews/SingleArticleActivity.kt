package com.example.worldnews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_single_article.*

class SingleArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_article)

        Log.e("e", "data" + intent.getStringExtra("articleContent"))

        article_title2.text = intent.getStringExtra("articleTitle")
        article_content.text = intent.getStringExtra("articleContent")
        article_date2.text = intent.getStringExtra("articleDate")

        Glide.with(this).load(intent.getStringExtra("imageUrl")).into(article_image2)

        backIC()

    }

    fun backIC() {
        back.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}