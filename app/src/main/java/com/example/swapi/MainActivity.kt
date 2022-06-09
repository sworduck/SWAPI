package com.example.swapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.adapter.MainAdapter
import com.example.swapi.api.ApiHelper
import com.example.swapi.api.RetrofitBuilder
import com.example.swapi.base.ViewModelFactory
import com.example.swapi.utilis.Status
import com.example.swapi.viewmodel.MainViewModel
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
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private var recyclerView:RecyclerView? = null
    private var progressBar:ProgressBar? =null

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
        /*
        recyclerView = findViewById(R.id.recyclerview)
        progressBar = findViewById(R.id.progresbar)

        setupViewModel()
        setupUI()
        setupObservers()

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




    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }

    private fun setupUI() {
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                recyclerView!!.context,
                (recyclerView!!.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView!!.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.getUsers(1).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView!!.visibility = View.VISIBLE
                        progressBar!!.visibility = View.GONE
                        resource.data?.let { users -> retrieveList(users.results!!) }
                    }
                    Status.ERROR -> {
                        recyclerView!!.visibility = View.VISIBLE
                        progressBar!!.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar!!.visibility = View.VISIBLE
                        recyclerView!!.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(users: List<CharacterCloud>) {
        adapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
    }



}