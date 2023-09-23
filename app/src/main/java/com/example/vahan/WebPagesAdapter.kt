package com.example.vahan

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WebPagesAdapter(private val webPages: List<String>, private val context: UniversityAdapter.ViewHolder) : RecyclerView.Adapter<WebPagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.web_page_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val webPage = webPages[position]
        holder.bind(webPage)
    }

    override fun getItemCount(): Int {
        return webPages.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val webPageTextView: TextView = itemView.findViewById(R.id.webPageTextView)

        fun bind(webPage: String) {
            webPageTextView.text = webPage

            webPageTextView.setOnClickListener {
                val intent = Intent(itemView.context,WebViewActivity::class.java)
                intent.putExtra("web_page_url", webPage)
                itemView.context.startActivity(intent)
            }
        }
    }
}
