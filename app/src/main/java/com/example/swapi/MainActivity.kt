package com.example.swapi

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.swapi.api.CharacterService
import com.example.swapi.data.cache.CharacterDb
import com.example.swapi.data.cloud.CharacterCloud
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit


class MainActivity : AppCompatActivity(),RealmProvider {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val BASE_URL = "https://swapi.dev/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provide(this)
        clearDb()
        //updateMigratedCharacterList()
        setContentView(R.layout.activity_main)


        val navController = this.findNavController(R.id.nav_host_fragment)
        val bottomNav:BottomNavigationView = findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNav, navController)
        /*
        val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))

         */
        // Obtain the FirebaseAnalytics instance.
        /*
        firebaseAnalytics = Firebase.analytics


        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, id)
            param(FirebaseAnalytics.Param.ITEM_NAME, name)
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        }

         */



    }
    override fun provide(context: Context){
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
    private fun updateMigratedCharacterList(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
        val typeCharacter = object : TypeToken<CharacterCloud>() {}.type
        val service = retrofit.create(CharacterService::class.java)
        var realm = Realm.getDefaultInstance()
        var characterDbList:MutableList<CharacterDb> = mutableListOf()
        var characterCloud: CharacterCloud? = null
        var characterDb: CharacterDb? = null
        realm.executeTransaction { r:Realm->
            characterDbList = realm.where(CharacterDb::class.java).equalTo("type","favorite").findAll()
        }
        CoroutineScope(Dispatchers.Main).launch {
            for (i in characterDbList.indices) {
                characterCloud = Gson().fromJson(service.fetchCharacter(characterDbList[i].id+1).string(), typeCharacter)
                realm.executeTransaction { r: Realm ->
                    characterDb = r.where(CharacterDb::class.java).equalTo("id", characterDbList[i].id)
                        .findFirst()
                    characterDb!!.idList = characterCloud!!.films!!.joinToString()
                }
            }
        }


    }
}