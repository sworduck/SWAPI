package com.example.swapi.presentation.desc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.data.CharacterData
import com.example.swapi.data.FilmData
import com.example.swapi.databinding.FilmsItemBinding
import com.example.swapi.presentation.search.CharacterDataDiffCallback
import com.example.swapi.presentation.search.SearchFragmentAdapter

class DescriptionFilmAdapter() :
    ListAdapter<FilmData, DescriptionFilmAdapter.FilmViewHolder>(FilmDataDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(FilmsItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holderFilm: FilmViewHolder, position: Int) {
        holderFilm.bind(getItem(position))
    }

    class FilmViewHolder(private val binding:FilmsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(filmData: FilmData) {
            binding.titleDescription.text = filmData.title
            binding.openingCrawl.text = filmData.opening_crawl
        }
    }

}