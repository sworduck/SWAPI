package com.example.swapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.CharacterCloud
import com.example.swapi.CharacterData
import com.example.swapi.R

class SearchFragmentAdapter(private val characterList: ArrayList<CharacterData>) : RecyclerView.Adapter<SearchFragmentAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: CharacterData) {
            itemView.apply {
                findViewById<Button>(R.id.name).text = user.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.characters_item, parent, false)
        )

    override fun getItemCount(): Int = characterList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        holder.bind(characterList[position])
    }

    fun addCharacterList(users: List<CharacterData>) {
        this.characterList.apply {
            clear()
            addAll(users)
        }

    }
}