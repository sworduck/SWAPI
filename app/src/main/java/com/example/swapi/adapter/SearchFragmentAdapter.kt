package com.example.swapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.*
import com.example.swapi.data.CharacterData
import com.example.swapi.data.CharacterDb
import io.realm.Realm
import kotlin.collections.ArrayList

class SearchFragmentAdapter(
    private val characterList: ArrayList<CharacterData>
) : RecyclerView.Adapter<SearchFragmentAdapter.DataViewHolder>() {
    private var onClickListener : OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClickName(position: Int)
        //TODO fix it
        fun onClickFavorite():Boolean
    }

    private fun removeItem(position: Int){
        characterList.removeAt(position)
        notifyItemRemoved(position)
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(characterData: CharacterData) {//, viewModel: FavoriteCharactersViewModel?) {
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
            val realm = Realm.getDefaultInstance()
            if (characterList[position].type == "default") {
                //переключение персонажа с default на favorite
                holder.itemView.findViewById<ImageButton>(R.id.favorite)
                    .setBackgroundResource(R.drawable.ic_baseline_star_rate_24)
                //characterData.type = "favorite"
                realm.executeTransaction { r ->
                    val characterDb =
                        r.where(CharacterDb::class.java).equalTo("id", characterList[position].id)
                            .findFirst()
                    characterDb!!.type = "favorite"
                }
                characterList[position].type = "favorite"
            } else {
                //переключение персонажа с favorite на default
                holder.itemView.findViewById<ImageButton>(R.id.favorite)
                    .setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                realm.executeTransaction { r ->
                    val characterDb =
                        r.where(CharacterDb::class.java).equalTo("id", characterList[position].id)
                            .findFirst()
                    characterDb!!.type = "default"
                }
                if(onClickListener!!.onClickFavorite()){
                    removeItem(position)
                }
                else{
                    characterList[position].type = "default"
                }

            }
        }
            holder.itemView.findViewById<Button>(R.id.name).setOnClickListener {
                this.onClickListener!!.onClickName(position)
            }
    }

    fun addCharacterList(users: List<CharacterData>) {
        this.characterList.apply {
            clear()
            addAll(users)
        }

    }
}