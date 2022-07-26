package com.example.swapi.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.R
import com.example.swapi.data.CharacterData
import com.example.swapi.databinding.CharactersItemBinding
import com.example.swapi.utilis.Type

class SearchFragmentAdapter(
    private val onClick: (Int) -> Unit,
    private val onFeaturedClick: () -> Boolean,
    private val clickFavoriteButton: (CharacterData) -> Unit,
    private val removeItem: (CharacterData) -> Unit,
) : ListAdapter<CharacterData, SearchFragmentAdapter.DataViewHolder>(CharacterDataDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(CharactersItemBinding.inflate(LayoutInflater.from(parent.context)),
            onClick,
            onFeaturedClick,
            clickFavoriteButton,
            removeItem)

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DataViewHolder(
        private val binding: CharactersItemBinding,
        private val onClick: (Int) -> Unit,
        private val onFeaturedClick: () -> Boolean,
        private val clickFavoriteButton: (CharacterData) -> Unit,
        private val removeItem: (CharacterData) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(characterData: CharacterData) {
            binding.name.text = characterData.name
            when (characterData.type) {
                Type.DEFAULT -> {
                    binding.favorite.setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                }
                Type.FAVORITE -> {
                    binding.favorite.setBackgroundResource(R.drawable.ic_baseline_star_rate_24)
                }
            }

            binding.name.setOnClickListener {
                onClick(characterData.id)
            }

            binding.favorite.setOnClickListener {
                clickFavoriteButton(characterData)
                when (characterData.type) {
                    Type.DEFAULT -> {
                        binding.favorite
                            .setBackgroundResource(R.drawable.ic_baseline_star_rate_24)
                        characterData.type = Type.FAVORITE
                    }
                    Type.FAVORITE -> {
                        binding.favorite
                            .setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                        if (onFeaturedClick()) {
                            removeItem(characterData)
                        } else {
                            characterData.type = Type.DEFAULT
                        }
                    }
                }
            }
        }
    }
}