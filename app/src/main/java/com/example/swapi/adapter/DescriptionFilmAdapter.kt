package com.example.swapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.R
import com.example.swapi.data.FilmData
import com.example.swapi.data.cache.FilmDataBaseEntity

class DescriptionFilmAdapter(private val filmList: ArrayList<FilmData>) :
    RecyclerView.Adapter<DescriptionFilmAdapter.FilmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.films_item, parent, false))
    }

    override fun onBindViewHolder(holderFilm: FilmViewHolder, position: Int) {
        holderFilm.title.text = filmList[position].title
        holderFilm.opening_crawl.text = filmList[position].opening_crawl
    }

    override fun getItemCount(): Int {
        return filmList.count()
    }

    fun addFilmList(filmList: List<FilmData>) {
        this.filmList.apply {
            clear()
            addAll(filmList)
        }

    }

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title2)
        val opening_crawl: TextView = itemView.findViewById(R.id.opening_crawl)
    }

}