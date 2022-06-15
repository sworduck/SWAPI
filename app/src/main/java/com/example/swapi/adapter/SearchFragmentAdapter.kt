package com.example.swapi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.*
import io.realm.Realm
import java.util.*
import kotlin.collections.ArrayList

class SearchFragmentAdapter(
    private val characterList: ArrayList<CharacterData>//, private val viewModel: FavoriteCharactersViewModel
) : RecyclerView.Adapter<SearchFragmentAdapter.DataViewHolder>() {


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(characterData: CharacterData){//, viewModel: FavoriteCharactersViewModel?) {
            itemView.apply {
                findViewById<Button>(R.id.name).text = characterData.name
                findViewById<ImageButton>(R.id.favorite).setOnClickListener {
                    //viewModel!!.addElementFavoriteList(characterData)
                    val realm = Realm.getDefaultInstance()
                    if(characterData.type == "default"){
                        //переключение персонажа с default на favorite
                        findViewById<ImageButton>(R.id.favorite).setBackgroundResource(R.drawable.ic_baseline_star_rate_24)
                        //characterData.type = "favorite"
                        realm.executeTransaction {r->
                           val characterData = r.where(CharacterDb::class.java).equalTo("id",characterData.id).findFirst()
                            val realmList = r.where(CharacterDb::class.java).findAll()
                            characterData!!.type = "favorite"
                        }
                    }
                    else{
                        //переключение персонажа с favorite на default
                        findViewById<ImageButton>(R.id.favorite).setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                        //characterData.type = "default"
                        realm.executeTransaction {r->
                            val characterData = r.where(CharacterDb::class.java).equalTo("id",characterData.id).findFirst()
                            characterData!!.type = "default"
                        }
                    }


                    /*
                    var realm = Realm.getDefaultInstance()
                    realm.executeTransaction {r->
                        val characterDb =
                            r.createObject(CharacterDb::class.java,UUID.randomUUID().hashCode())
                        //characterDb.id = characterDataList[0].id
                        characterDb.name = characterData.name
                        characterDb.height = characterData.height
                        characterDb.mass = characterData.mass
                        characterDb.homeworld = characterData.homeworld
                        characterDb.type = "favorite"
                        r.insertOrUpdate(characterDb)
                    }

                     */
                }
                if(characterData.type == "default"){
                    findViewById<ImageButton>(R.id.favorite).setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                }
                else{
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
    }

    fun addCharacterList(users: List<CharacterData>) {
        this.characterList.apply {
            clear()
            addAll(users)
        }

    }
}