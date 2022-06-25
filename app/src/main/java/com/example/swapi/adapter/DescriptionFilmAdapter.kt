package com.example.swapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.R
import com.example.swapi.data.FilmDb

class DescriptionFilmAdapter(private val filmList:ArrayList<FilmDb>):RecyclerView.Adapter<DescriptionFilmAdapter.FilmViewHolder>() {

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.title2)!!

        var opening_crawl = itemView.findViewById<TextView>(R.id.opening_crawl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.films_item,parent,false))
    }

    override fun onBindViewHolder(holderFilm: FilmViewHolder, position: Int) {
        holderFilm.title.text = filmList[position].title
        holderFilm.opening_crawl.text = filmList[position].opening_crawl
    }

    override fun getItemCount(): Int {
        return filmList.count()
    }
    fun addFilmList(filmList: List<FilmDb>) {
        this.filmList.apply {
            clear()
            addAll(filmList)
        }

    }
}