package com.example.swapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.CharacterCloud
import com.example.swapi.R

class MainAdapter(private val users: ArrayList<CharacterCloud>) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: CharacterCloud) {
            itemView.apply {
                findViewById<Button>(R.id.name).text = user.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.characters_item, parent, false)
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        holder.bind(users[position])
    }

    fun addUsers(users: List<CharacterCloud>) {
        this.users.apply {
            clear()
            addAll(users)
        }

    }
}