package com.example.swapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.R
import com.example.swapi.data.CharacterData

class DescriptionAdapter(private val filmList:ArrayList<String>):RecyclerView.Adapter<DescriptionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView = itemView.findViewById<TextView>(R.id.textView2)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.films_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = filmList[position]
    }

    override fun getItemCount(): Int {
        return filmList.count()
    }
    fun addFilmList(filmList: List<String>) {
        this.filmList.apply {
            clear()
            addAll(filmList)
        }

    }
}