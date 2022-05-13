package com.example.worldnews

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worldnews.Api.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    lateinit var adapter: ArticlesAdapter
    lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getDataFromApi()
        initRecyclerView()
    }


    private fun initRecyclerView() {
        adapter = ArticlesAdapter()
        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        article_recycler.adapter = adapter
        article_recycler.layoutManager = layoutManager

        adapter.setOnCardClickListener(object : ArticlesAdapter.onItemClickListener {

            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SimpleDateFormat")
            override fun onItemClick(article: ArticlesItem) {

                val intent = Intent(this@MainActivity, SingleArticleActivity::class.java)

                intent.putExtra("articleTitle", article.title)
                intent.putExtra("articleContent", (article.content).toString())
                intent.putExtra("imageUrl", article.urlToImage)

                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
                val formattedDate = formatter.format(parser.parse(article?.publishedAt))
                intent.putExtra("articleDate", formattedDate)

                startActivity(intent)

            }
        })
    }

    private fun getDataFromApi() {
        ApiManager
            .getWebServices()
            .getSources()
            .enqueue(object : Callback<ResourcesResponse> {
                override fun onResponse(
                    call: Call<ResourcesResponse>,
                    response: Response<ResourcesResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status.equals("ok")) {
//                        Log.e("data responce", "data responce: " + response.body())
                        setupTabs(response.body()?.sources)
                        tabs_news.getTabAt(0)?.select()

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            response.body()?.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResourcesResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun setupTabs(sources: List<SourcesItem?>?) {
        sources?.forEach { item ->
            val tab = tabs_news.newTab()
            tab.text = item?.name
            tab.tag = item
            tabs_news.addTab(tab)
        }

        tabs_news.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val item = tab?.tag as SourcesItem
                getArticles(item.id)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val item = tab?.tag as SourcesItem
                getArticles(item.id)
            }
        })
    }

    fun getArticles(articleSourceId: String?) {
        articleSourceId?.let {
            ApiManager.getWebServices()
                .getArticles(articleSourceId)
                .enqueue(object : Callback<ArticlesResponse> {
                    override fun onResponse(
                        call: Call<ArticlesResponse>,
                        response: Response<ArticlesResponse>
                    ) {
                        progressBar.visibility = View.GONE
                        if (response.isSuccessful && response.body()?.status.equals("ok")) {
//                            Log.e("data responce", "data responce: " + response.body())
                            adapter.getData(response.body()?.articles)

                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                response.body()?.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ArticlesResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                })
        }
    }
}