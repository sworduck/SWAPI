package com.example.swapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.adapter.DescriptionFilmAdapter
import com.example.swapi.data.CharacterDb
import com.example.swapi.data.FilmDb
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CharacterDescriptionActivity : AppCompatActivity() {
    private val BASE_URL = "https://swapi.dev/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_description)
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        val realm = Realm.getDefaultInstance()


        val id = intent.getIntExtra("id", 0)
        var text = findViewById<TextView>(R.id.textView)
        text.text = "номер чего-то: $id"


        var filmsDb: MutableList<FilmDb>? = mutableListOf()
        var filmsId:List<Int>? = null
        var recycler = findViewById<RecyclerView>(R.id.descriptionRecycler)
        var adapter = DescriptionFilmAdapter(arrayListOf(FilmDb()))
        recycler.adapter = adapter
        var character:CharacterDb =
            realm.where(CharacterDb::class.java).equalTo("id",id).findFirst()!!
        var list:List<Int> = character.idList!!.split(",").map {
            it.replace("/","").substringAfterLast("films").toInt()
        }


        scope.launch {

            val filmListDb = realm.where(FilmDb::class.java).findAll()
            val a = filmListDb.toString()

            for(i in list){
                filmsDb!!.add(realm.where(FilmDb::class.java).equalTo("id",i-1).findFirst()!!)
            }


            adapter.addFilmList(filmsDb!!)
            adapter.notifyDataSetChanged()
            text.text ="Name: ${character!!.name} \nMass: ${character!!.mass}" +
                    "\nHeight: ${character!!.height}"
        }


        var button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}