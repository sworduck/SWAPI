package com.example.swapi.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.R
import com.example.swapi.data.CharacterData
import com.example.swapi.databinding.CharactersItemBinding
import com.example.swapi.utilis.Type
import java.util.*

class SearchFragmentAdapter(
    private val onClick: (Int) -> Unit,
    private val onFeaturedClick: () -> Boolean,
    private val clickFavoriteButton: (CharacterData) -> Unit,
    private val removeItem: (CharacterData) -> Unit,
) : ListAdapter<CharacterData, SearchFragmentAdapter.DataViewHolder>(CharacterDataDiffCallback()),Filterable {

    private var list = mutableListOf<CharacterData>()

    fun setData(list: MutableList<CharacterData>?){
        this.list = (list ?: mutableListOf())
        submitList(list)
    }

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

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<CharacterData>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(list.slice(0..10))
            } else {
                for (item in list) {
                    if (item.name.lowercase(Locale.getDefault()).startsWith(constraint.toString()
                            .lowercase(Locale.getDefault()))) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<CharacterData>?)
        }
    }
}