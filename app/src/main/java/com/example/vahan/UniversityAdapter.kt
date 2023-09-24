package com.example.vahan

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UniversityAdapter(private var universities: List<University>) : RecyclerView.Adapter<UniversityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.university_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val university = universities[position]
        holder.bind(university)
    }

    override fun getItemCount(): Int {
        return universities.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val countryTextView: TextView = itemView.findViewById(R.id.countryTextView)
        private val webPagesRecyclerView: RecyclerView = itemView.findViewById(R.id.webPagesRecyclerView)

        fun bind(university: University) {
            nameTextView.text = university.name
            countryTextView.text = university.country

            val webPagesAdapter = WebPagesAdapter(university.web_pages,this)
            webPagesRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            webPagesRecyclerView.adapter = webPagesAdapter

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newUniversities: List<University>) {
        universities = newUniversities
        notifyDataSetChanged()
    }
}
