package com.example.swapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
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
        //данные с сервера читаются, я могу прочесть одного человека, но не выходит преобразовать сразу список!!!
        //целый объект со списком также читается, но сам список почему-то null

        /*
        val navController = this.findNavController(R.id.nav_host_fragment)
        val bottomNav:BottomNavigationView = findViewById(R.id.bottomNavigation)
        //bottomNav.setupWithNavController(navController)
        NavigationUI.setupWithNavController(bottomNav, navController);

         */


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