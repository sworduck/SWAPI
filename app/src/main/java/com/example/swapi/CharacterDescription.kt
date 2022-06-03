package com.example.swapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class CharacterDescription : AppCompatActivity() {
    private val BASE_URL = "https://swapi.dev/api/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_description)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
        val service = retrofit.create(CharacterService::class.java)
        val gson = Gson()
        val typelist = object: TypeToken<CharacterCloud>(){}.type
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        var character:CharacterCloud? =null





        val int = intent.getIntExtra("id",0)
        var text = findViewById<TextView>(R.id.textView)
        text.text = "номер чего-то: $int"






        scope.launch {
            character =gson.fromJson(service.fetchCharacter(int+1).string(),typelist)
            text.text = "${character!!.name} ${character!!.mass} ${character!!.height} ${character!!.homeworld}"
        }








        var button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}