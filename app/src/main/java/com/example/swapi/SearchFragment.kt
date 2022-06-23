package com.example.swapi

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.adapter.SearchFragmentAdapter
import com.example.swapi.api.ApiHelper
import com.example.swapi.api.CharacterService
import com.example.swapi.api.RetrofitBuilder
import com.example.swapi.base.SearchViewModelFactory
import com.example.swapi.data.*
import com.example.swapi.databinding.SearchFragmentBinding
import com.example.swapi.utilis.Status
import com.example.swapi.viewmodel.FavoriteCharactersViewModel
import com.example.swapi.viewmodel.SearchViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration
import kotlinx.coroutines.*
import retrofit2.Retrofit

class SearchFragment : Fragment() {
    companion object {
        fun newInstance() = SearchFragment()
    }

    private val BASE_URL = "https://swapi.dev/"

    private var urlId:Int = 1

    private lateinit var viewModel: SearchViewModel

    private lateinit var viewModelFavorite: FavoriteCharactersViewModel

    private lateinit var binding: SearchFragmentBinding


    private var adapter:SearchFragmentAdapter? = null

    private var recyclerView:RecyclerView? = null
    private var progressBar:ProgressBar? = null

    private var page: MutableLiveData<Int> = MutableLiveData(1)
    private var previousPage = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.search_fragment,container,false)



        recyclerView = binding.charactersRecyclerView

        progressBar = binding.progresbar
                                            //чтобы информация передавалась нужно передавать activity
                                            //или requireActivity()
        viewModelFavorite = ViewModelProvider(requireActivity())[FavoriteCharactersViewModel::class.java]

        //page.value = 1
        provide(requireContext())
        clearDb()//ДЛЯ БД
        setupViewModel()
        fetchFilmList()
        setupUI()

        page.observe(viewLifecycleOwner, Observer {
            setupObservers(it)
        })

        binding.next.setOnClickListener {
            previousPage = page.value!!
            page.value = (page.value)?.plus(1)
        }

        binding.previous.setOnClickListener {
            previousPage = page.value!!
            page.value = (page.value)?.minus(1)
        }
        return binding.root
    }
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this,SearchViewModelFactory(ApiHelper(RetrofitBuilder.apiService)))
            .get(SearchViewModel::class.java)
    }

    private fun setupUI() {
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        adapter = SearchFragmentAdapter(arrayListOf())//,viewModelFavorite)
        recyclerView!!.addItemDecoration(
            DividerItemDecoration(
                recyclerView!!.context,
                (recyclerView!!.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView!!.adapter = adapter
    }

    private fun setupObservers(page:Int) {
        viewModel.getCharacterList(page).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView!!.visibility = View.VISIBLE
                        progressBar!!.visibility = View.GONE

                        resource.data?.let { characterDataList ->
                            retrieveList(checkFavoriteCharacterListInResponseFromServer(characterDataList,page)) }
                        val result = Realm.getDefaultInstance().where(CharacterDb::class.java).findAll()
                        val a = 1+1
                    }
                    Status.ERROR -> {
                            this.page.value = previousPage
                            recyclerView!!.visibility = View.VISIBLE
                            progressBar!!.visibility = View.GONE
                            Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar!!.visibility = View.VISIBLE
                        recyclerView!!.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun checkFavoriteCharacterListInResponseFromServer(characterList: List<CharacterData>, page: Int):List<CharacterData>{
        var list:List<Int> =listOf()
        Realm.getDefaultInstance().executeTransaction {realm->
            list =
                realm.where(CharacterDb::class.java).equalTo("type","favorite").findAll().map { characterDb ->
                    characterDb.id
                }
        }
        for(i in characterList.indices){
            if(characterList[i].id in list){
                characterList[i].type = "favorite"
            }
        }
        return characterList
    }

    private fun retrieveList(characterList: List<CharacterData>) {
        adapter.apply {
            this!!.addCharacterList(characterList.sortedBy { it.id })
            this.notifyDataSetChanged()
        }
    }

    private fun provide(context: Context){
        Realm.init(context)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
            .name("characterdb.realm")
            //.encryptionKey(getKey())
            .schemaVersion(3L)
            .migration(FilmRealmMigration())
            .allowWritesOnUiThread(true)
            .build())
        //Realm.setDefaultConfiguration(config)
    }

    private fun fetchFilmList(){
        CoroutineScope(Job() + Dispatchers.IO).launch {
            //TODO если списка фильмов нет то добавлять его в бд
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build()
            val typeCharacter = object : TypeToken<FilmCloudList>() {}.type
            val service = retrofit.create(CharacterService::class.java)
            val filmList: FilmCloudList =
                Gson().fromJson(service.fetchFilmList().string(), typeCharacter)


            val realm = Realm.getDefaultInstance()
            realm.executeTransactionAsync { r: Realm ->
                for (i in filmList.results!!.indices) {
                    val filmDb =
                        r.createObject(FilmDb::class.java,i)
                    //characterDb.id = characterDataList[0].id
                    filmDb.title = filmList.results[i].title
                    filmDb.opening_crawl = filmList.results[i].opening_crawl
                    r.insertOrUpdate(filmDb)
                }
            }
        }
    }

    private fun clearDb(){
        var realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync { r: Realm ->
            r.where(CharacterDb::class.java).equalTo("type","default").findAll().deleteAllFromRealm()
        }
    }


}