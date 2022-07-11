package com.example.swapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.*
import com.example.swapi.data.CharacterData
import com.example.swapi.data.cache.CharacterDb
import io.realm.Realm
import kotlin.collections.ArrayList

class SearchFragmentAdapter(
    private val characterList: ArrayList<CharacterData>,
    private val onClickListener : OnClickListener
) : RecyclerView.Adapter<SearchFragmentAdapter.DataViewHolder>() {

    interface OnClickListener {
        fun onClickName(position: Int)
        fun onClickFavoriteOnSearchOrFavoritePage():Boolean
        fun onClickFavoriteButton(type:String,id:Int)
    }

    private fun removeItem(position: Int){
        characterList.removeAt(position)
        notifyItemRemoved(position)
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(characterData: CharacterData) {
            itemView.apply {
                findViewById<Button>(R.id.name).text = characterData.name
                if (characterData.type == "default") {
                    findViewById<ImageButton>(R.id.favorite).setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                } else {
                    findViewById<ImageButton>(R.id.favorite).setBackgroundResource(R.drawable.ic_baseline_star_rate_24)
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
        holder.bind(characterList[position])//,viewModel)
        holder.itemView.findViewById<ImageButton>(R.id.favorite).setOnClickListener {
            onClickListener.onClickFavoriteButton(characterList[position].type,characterList[position].id)
            if (characterList[position].type == "default") {
                holder.itemView.findViewById<ImageButton>(R.id.favorite)
                    .setBackgroundResource(R.drawable.ic_baseline_star_rate_24)
                characterList[position].type = "favorite"
            } else {
                holder.itemView.findViewById<ImageButton>(R.id.favorite)
                    .setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                if(onClickListener.onClickFavoriteOnSearchOrFavoritePage()){
                    removeItem(position)
                }
                else{
                    characterList[position].type = "default"
                }

            }
        }
            holder.itemView.findViewById<Button>(R.id.name).setOnClickListener {
                this.onClickListener.onClickName(characterList[position].id)
            }
    }

    fun addCharacterList(users: List<CharacterData>) {
        this.characterList.apply {
            clear()
            addAll(users)
        }

    }
}