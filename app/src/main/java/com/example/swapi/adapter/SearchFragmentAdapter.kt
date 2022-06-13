package com.example.swapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.CharacterData
import com.example.swapi.FavoriteCharactersViewModel
import com.example.swapi.R
import com.example.swapi.SearchViewModel
import java.util.*
import kotlin.collections.ArrayList

class SearchFragmentAdapter(
    private val characterList: ArrayList<CharacterData>,
    private val viewModel: FavoriteCharactersViewModel
) : RecyclerView.Adapter<SearchFragmentAdapter.DataViewHolder>() {


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(characterData: CharacterData, viewModel: FavoriteCharactersViewModel?) {
            itemView.apply {
                findViewById<Button>(R.id.name).text = characterData.name
                findViewById<Button>(R.id.favorite).setOnClickListener {
                    viewModel!!.addElementFavoriteList(characterData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.characters_item, parent, false)
        )

    override fun getItemCount(): Int = characterList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(characterList[position],viewModel)
    }

    fun addCharacterList(users: List<CharacterData>) {
        this.characterList.apply {
            clear()
            addAll(users)
        }

    }
}