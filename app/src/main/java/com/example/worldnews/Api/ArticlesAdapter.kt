package com.example.worldnews.Api

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.worldnews.R
import java.text.SimpleDateFormat


class ArticlesAdapter : RecyclerView.Adapter<ArticlesAdapter.articlesViewHolder>() {

    var data: List<ArticlesItem>? = null

    class articlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val articleDate: TextView = itemView.findViewById(R.id.article_date)
        val articleTitle: TextView = itemView.findViewById(R.id.article_title)
        val articleDescription: TextView = itemView.findViewById(R.id.article_description)
        val articleAuthor: TextView = itemView.findViewById(R.id.article_author)
        val articleImage: ImageView = itemView.findViewById(R.id.article_image)
        val     cardView: CardView = itemView.findViewById(R.id.article_card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): articlesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return articlesViewHolder(view)
    }


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: articlesViewHolder, position: Int) {

        var article = data?.get(position)

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val formattedDate = formatter.format(parser.parse(article?.publishedAt))

        holder.articleDate.text = formattedDate
        holder.articleDescription.text = article?.description
        holder.articleTitle.text = article?.title
        holder.articleAuthor.text = article?.author
        Glide.with(holder.itemView).load(article?.urlToImage).into(holder.articleImage)

        holder.cardView.setOnClickListener {
            onCardClickListener.onItemClick(article!!)
        }

    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    fun getData(comingData: List<ArticlesItem>?) {
        data = comingData
        notifyDataSetChanged()
    }


    interface onItemClickListener {
        fun onItemClick(article : ArticlesItem)
    }

    private lateinit var onCardClickListener: onItemClickListener

    fun setOnCardClickListener(onCardClick: onItemClickListener) {
        onCardClickListener = onCardClick
    }


}