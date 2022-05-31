package com.example.swapi

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit


class MainActivity : AppCompatActivity() {
    private companion object{
        const val BASE_URL = "https://swapi.dev/api/"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
        val service = retrofit.create(CharacterService::class.java)
        val gson = Gson()
        val typelist = object:TypeToken<CharacterList>(){}.type
        val type = object:TypeToken<CharacterCloud>(){}.type
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        var list: CharacterList? = null

        var cList:MutableList<CharacterCloud> = mutableListOf()
        scope.launch {
            //val apiResponse = URL(BASE_URL+"people/").readText()//не вариант, текст длинный слишком
            //Log.i("TAG", "${apiResponse}");
            list =gson.fromJson(service.fetchCharacters().string(),typelist)
            //val a = service.getTripCoord(1)//нужен адаптер для Deffered<JSONArray>
            //val a:Character = gson.fromJson(service.getTripCoord(1).string(),object:TypeToken<Character>(){}.type)
            //val b:Character = gson.fromJson(service.getTripCoord(1).string(),object:TypeToken<Character>(){}.type)
            //val a = service.fetchCharacters()
            //val a:Character = gson.fromJson(service.fetchCharacter(1).string(),type)
            for(i in 1..10){
                cList.add(gson.fromJson(service.fetchCharacter(i).string(),type))//работает
            }
            Log.i("TAG", "${list}");
            val c =3
        }
        //читать по одному


        /*
        val type = object:TypeToken<Character>(){}.type
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        var list: Character? = null
        scope.launch {
            list =gson.fromJson(service.fetchCharacters().string(),type)
            //val a = service.fetchCharacters()
            Log.i("TAG", "${list}");
            val b =3
        }

         */
        //val list:List<Character> =gson.fromJson(service.fetchCharacters().string(),type)
        val a = 4
        //данные с сервера читаются, я могу прочесть одного человека, но не выходит преобразовать сразу список!!!
        //целый объект со списком также читается, но сам список почему-то null
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.menu.getItem(0).isCheckable = true
        setFragment(SearchFragment())
        bottomNav.setOnNavigationItemSelectedListener {menu->
            when(menu.itemId){
                R.id.action_search->{
                    setFragment(SearchFragment())
                    true
                }
                R.id.action_settings->{
                    setFragment(FavoriteCharactersFragment())
                    true
                }
                else ->false
            }
        }
    }

    fun setFragment(fr : Fragment){
        val frag = supportFragmentManager.beginTransaction()
        frag.replace(R.id.fragment,fr)
        frag.commit()
    }

}