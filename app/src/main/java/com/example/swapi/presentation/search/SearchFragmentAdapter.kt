package com.example.swapi.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.*
import com.example.swapi.data.CharacterData
import com.example.swapi.databinding.CharactersItemBinding
import com.example.swapi.domain.FavoriteUseCase
import javax.inject.Inject

class SearchFragmentAdapter(
    private val onClick: (Int) -> Unit,
    private val onFeaturedClick: () -> Boolean,
    private val clickFavoriteButton: FavoriteUseCase
): ListAdapter<CharacterData, SearchFragmentAdapter.DataViewHolder>(CharacterDataDiffCallback()) {

    private val differ = AsyncListDiffer(this, CharacterDataDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(CharactersItemBinding.inflate(LayoutInflater.from(parent.context)),onClick,onFeaturedClick,clickFavoriteButton,::removeItem)

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun removeItem(position: Int){
        differ.currentList.removeAt(position)
        notifyItemChanged(position)
    }

    class DataViewHolder(private val binding: CharactersItemBinding, private val onClick: (Int) -> Unit, private val onFeaturedClick: () -> Boolean,
                         private val clickFavoriteButton: FavoriteUseCase,private val removeItem:(Int)->Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(characterData: CharacterData) {
                binding.name.text = characterData.name
                if (characterData.type == "default") {
                    binding.favorite.setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                } else {
                    binding.favorite.setBackgroundResource(R.drawable.ic_baseline_star_rate_24)
                }

                binding.name.setOnClickListener {
                    onClick(characterData.id)
                }

                binding.favorite.setOnClickListener {
                    clickFavoriteButton.onClickFavoriteButton(characterData.type,
                        characterData.id)
                    if (characterData.type == "default") {
                        binding.favorite
                            .setBackgroundResource(R.drawable.ic_baseline_star_rate_24)
                        characterData.type = "favorite"
                    } else {
                        binding.favorite
                            .setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                        if (onFeaturedClick()) {
                            removeItem(characterData.id)
                        } else {
                            characterData.type = "default"
                        }
                    }
                }
        }
    }
}