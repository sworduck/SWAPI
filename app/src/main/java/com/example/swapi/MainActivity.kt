package com.example.swapi

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.swapi.api.CharacterService
import com.example.swapi.data.CharacterData
import com.example.swapi.data.cache.CharacterDataBaseDao
import com.example.swapi.data.cache.CharacterDataBaseEntity
import com.example.swapi.data.cache.CharacterRoomDataBase
import com.example.swapi.data.cloud.CharacterCloud
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit


class MainActivity : AppCompatActivity(), RealmProvider {
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //provide(this)
        //clearDb()
        //updateMigratedCharacterList()
        setContentView(R.layout.activity_main)


        val navController = this.findNavController(R.id.nav_host_fragment)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNav, navController)

        val db = CharacterRoomDataBase.getDataBase(this)
        val characterDataBaseDao: CharacterDataBaseDao = db.characterDataBaseDao()
        val characterDataBaseEntity: CharacterDataBaseEntity = CharacterDataBaseEntity(
            100, "1", "1", "1", "1", "default", "1")

        CoroutineScope(Job() + Dispatchers.IO).launch {
            //characterDataBaseDao.insert(characterDataBaseEntity)
            /*
            val list:List<CharacterDataBaseEntity> = characterDataBaseDao.getCharacterList(1)
            Log.i("TAG","${list.size}")
            Log.i("TAG","${list[0].type}")
            val characterDataBaseEntityTwo: CharacterDataBaseEntity = characterDataBaseDao.getCharacter(100)
            Log.i("TAG","${characterDataBaseEntityTwo.type}")
            //characterDataBaseDao.delete(100)

             */
            //characterDataBaseDao.deleteAll()
            /*
            val characterDataBaseEntityThree:CharacterDataBaseEntity = characterDataBaseDao.getCharacter(100)
            Log.i("TAG","$characterDataBaseEntityTwo")
            Log.i("TAG", "успешное выполнение запросов бд")

             */
        }


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

    override fun provide(context: Context) {
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

}