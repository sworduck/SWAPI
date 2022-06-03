package com.example.swapi

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class RecyclerSearchFragmentAdapter(private val listCharactersName:List<String>):RecyclerView.Adapter<RecyclerSearchFragmentAdapter.SearchViewHolder>() {

    private var page = 0
    public fun setPage(a:Int){
        page = a
    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.characters_item, parent, false)
            return SearchViewHolder(itemView)
        }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.textView.text = listCharactersName[position]
        holder.textView.setOnClickListener {
            val activity = holder.itemView.context as Activity
            val intent = Intent(activity,CharacterDescription::class.java)
            intent.putExtra("id",position+page*10)
            activity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listCharactersName.count()
    }

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView:TextView =itemView.findViewById(R.id.name)
    }
}