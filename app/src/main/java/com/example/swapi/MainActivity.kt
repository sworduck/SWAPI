package com.example.swapi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.adapter.MainAdapter
import com.example.swapi.api.ApiHelper
import com.example.swapi.api.RetrofitBuilder
import com.example.swapi.base.ViewModelFactory
import com.example.swapi.data.CharacterCloud
import com.example.swapi.data.CharacterDb
import com.example.swapi.utilis.Status
import com.example.swapi.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.realm.Realm
import io.realm.RealmConfiguration


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
        provide(this)
        clearDb()
        setContentView(R.layout.activity_main)


        val navController = this.findNavController(R.id.nav_host_fragment)
        val bottomNav:BottomNavigationView = findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNav, navController)


    }

    private fun provide(context: Context){
        Realm.init(context)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
            .name("characterdb.realm")
            //.encryptionKey(getKey())
            .schemaVersion(5L)
            //.deleteRealmIfMigrationNeeded()
            //.migration(FilmRealmMigration())
            .allowWritesOnUiThread(true)
            .build())
        //Realm.setDefaultConfiguration(config)
    }

    private fun clearDb(){
        var realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync { r: Realm ->
            r.where(CharacterDb::class.java).equalTo("type","default").findAll().deleteAllFromRealm()
        }
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