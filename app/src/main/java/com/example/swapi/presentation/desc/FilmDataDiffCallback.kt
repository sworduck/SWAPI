package com.example.swapi.presentation.desc

import androidx.recyclerview.widget.DiffUtil
import com.example.swapi.data.FilmData

class FilmDataDiffCallback : DiffUtil.ItemCallback<FilmData>() {
    override fun areItemsTheSame(oldItem: FilmData, newItem: FilmData): Boolean {
        return oldItem.idOnPage == newItem.idOnPage
    }

    override fun areContentsTheSame(oldItem: FilmData, newItem: FilmData): Boolean {
        return oldItem == newItem
    }
}