package com.example.swapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.adapter.DescriptionAdapter
import com.example.swapi.api.CharacterService
import com.example.swapi.data.CharacterCloud
import com.example.swapi.data.FilmCloud
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Retrofit

class CharacterDescriptionActivity : AppCompatActivity() {
    private val BASE_URL = "https://swapi.dev/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_description)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
        val service = retrofit.create(CharacterService::class.java)
        val gson = Gson()
        val typeCharacter = object : TypeToken<CharacterCloud>() {}.type
        val typeFilm = object : TypeToken<FilmCloud>() {}.type
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        var character: CharacterCloud? = null


        val id = intent.getIntExtra("id", 0)
        var text = findViewById<TextView>(R.id.textView)
        text.text = "номер чего-то: $id"


        var filmsCloud: MutableList<FilmCloud>? = mutableListOf()
        var filmsId:List<Int>? = null
        var body:ResponseBody? = null
        var recycler = findViewById<RecyclerView>(R.id.descriptionRecycler)
        var adapter = DescriptionAdapter(arrayListOf("1","2","3","4"))
        recycler.adapter = adapter
        var filmCloud:FilmCloud? = null


        scope.launch {
            character = gson.fromJson(service.fetchCharacter(id + 1).string(), typeCharacter)
            filmsId = character!!.films!!.map { filmLink->
                filmLink.substringAfterLast("films/").replace("/","").toInt()
            }

            //text.text ="${character!!.name} ${character!!.mass} ${character!!.height}"

            for(i in filmsId!!.indices){
                filmCloud = gson.fromJson(service.fetchFilm(filmsId!![i]).string(),typeFilm)

                filmsCloud!!.add(filmCloud!!)
            }
            //filmsCloud = gson.fromJson(service.fetchFilm(filmsId!![0]).string(),typeFilm)
            adapter.addFilmList(filmsCloud!!.map { filmCloud -> filmCloud.title })
            adapter.notifyDataSetChanged()
            //java.lang.ClassCastException: com.example.swapi.data.FilmCloud cannot be cast to java.util.List
            text.text ="${character!!.name} ${character!!.mass} ${character!!.height} ${filmsCloud!![0].title}"
        }


        var button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}