package com.example.swapi.presentation.search

import androidx.recyclerview.widget.DiffUtil
import com.example.swapi.data.CharacterData

class CharacterDataDiffCallback : DiffUtil.ItemCallback<CharacterData>() {

    override fun areItemsTheSame(oldItem: CharacterData, newItem: CharacterData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CharacterData, newItem: CharacterData): Boolean {
        return oldItem == newItem
    }
}