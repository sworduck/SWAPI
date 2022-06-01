package com.example.swapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerSearchFragmentAdapter(private val listCharactersName:List<String>):RecyclerView.Adapter<RecyclerSearchFragmentAdapter.SearchViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.characters_item, parent, false)
            return SearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.textView.text = listCharactersName[position]
        holder.textView.setOnClickListener {
            //TODO переход в активити и обратно
        }
    }

    override fun getItemCount(): Int {
        return listCharactersName.count()
    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView:TextView =itemView.findViewById(R.id.name)
    }
}