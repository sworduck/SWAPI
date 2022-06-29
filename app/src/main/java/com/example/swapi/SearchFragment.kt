package com.example.swapi

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
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
import kotlinx.coroutines.*
import retrofit2.Retrofit

class SearchFragment : Fragment() {
    companion object {
        fun newInstance() = SearchFragment()
    }

    private val BASE_URL = "https://swapi.dev/"

    private var urlId:Int = 1

    private lateinit var bigViewModel: SearchViewModel

    private val viewModel:SearchViewModel by navGraphViewModels(R.id.navigation)

    private lateinit var viewModelFavorite: FavoriteCharactersViewModel

    private lateinit var binding: SearchFragmentBinding


    private var adapter:SearchFragmentAdapter? = null

    private var recyclerView:RecyclerView? = null
    private var progressBar:ProgressBar? = null

    //private var page: MutableLiveData<Int> = MutableLiveData(1)
    private var previousPage = 0

    private var flagClearDbLiveData:MutableLiveData<Boolean> = MutableLiveData(true)

    private var flagClearDb:Boolean = true

    private val onClickListener:SearchFragmentAdapter.OnClickListener = object:SearchFragmentAdapter.OnClickListener{
        override fun onClickName(position: Int) {
            view!!.findNavController().
            navigate(SearchFragmentDirections.actionSearchFragmentToCharacterDescriptionFragment2(position,"search"))
        }

        override fun onClickFavorite(): Boolean {
            return false
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("TAG","onCreateView")
        binding = DataBindingUtil.inflate(inflater,R.layout.search_fragment,container,false)


        recyclerView = binding.charactersRecyclerView

        progressBar = binding.progresbar
                                            //чтобы информация передавалась нужно передавать activity
                                            //или requireActivity()
        viewModelFavorite = ViewModelProvider(requireActivity())[FavoriteCharactersViewModel::class.java]


        //page.value = 1
        //provide(requireContext())
        //clearDb()//ДЛЯ БД
        //updateMigratedCharacterList()
        setupViewModel()
        fetchFilmList()
        setupUI()
        val list =Realm.getDefaultInstance().where(CharacterDb::class.java).findAll()
        val a = list.toString()
        val b = 1+1
        if(viewModel.page.value == null){
            viewModel.page.value = 1
        }
        viewModel.page.observe(viewLifecycleOwner, Observer {
            setupObservers(it)
        })

        binding.next.setOnClickListener {
            previousPage = viewModel.page.value!!
            viewModel.page.value = (viewModel.page.value)?.plus(1)
        }

        binding.previous.setOnClickListener {
            previousPage = viewModel.page.value!!
            viewModel.page.value = (viewModel.page.value)?.minus(1)
        }
        return binding.root
    }

    private fun setupViewModel() {
        //viewModel = ViewModelProvider(this,SearchViewModelFactory(ApiHelper(RetrofitBuilder.apiService))).get(SearchViewModel::class.java)
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
        adapter!!.setOnClickListener(onClickListener)
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
                    }
                    Status.ERROR -> {
                            //if(it.message!="Unable to resolve host \"swapi.dev\": No address associated with hostname")
                            this.viewModel.page.value = previousPage
                            recyclerView!!.visibility = View.VISIBLE
                            progressBar!!.visibility = View.GONE
                            //Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                            Log.i("TAG","СООБЩЕНИЕ ОБ ОШИБКЕ: ${it.message.toString()}")
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
            else{
                characterList[i].type = "default"
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
            .schemaVersion(5L)
            //.deleteRealmIfMigrationNeeded()
            //.migration(FilmRealmMigration())
            .allowWritesOnUiThread(true)
            .build())
        //Realm.setDefaultConfiguration(config)
    }

    private fun fetchFilmList(){
        val realm = Realm.getDefaultInstance()
        var filmList: MutableLiveData<FilmCloudList> = MutableLiveData()
        filmList?.observe(viewLifecycleOwner, Observer {
            realm.executeTransactionAsync { r: Realm ->
                for (i in filmList.value!!.results!!.indices) {
                    val filmDb =
                        r.createObject(FilmDb::class.java, i)
                    //characterDb.id = characterDataList[0].id
                    filmDb.title = filmList.value!!.results!![i].title
                    filmDb.opening_crawl = filmList.value!!.results!![i].opening_crawl
                    r.insertOrUpdate(filmDb)
                }
            }
        })
        var filmDbList = realm.where(FilmDb::class.java).findAll()//.deleteAllFromRealm()
        //filmDbList = realm.where(FilmDb::class.java).findAll()
        if (filmDbList.isEmpty()) {
            CoroutineScope(Job() + Dispatchers.Main).launch {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .build()
                    val typeCharacter = object : TypeToken<FilmCloudList>() {}.type
                    val service = retrofit.create(CharacterService::class.java)
                    filmList.value =
                        Gson().fromJson(service.fetchFilmList().string(), typeCharacter)

            }
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
        var characterCloud:CharacterCloud? = null
        var characterDb:CharacterDb? = null
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

    private fun clearDb(){
        if(flagClearDb) {
            var realm = Realm.getDefaultInstance()
            realm.executeTransactionAsync { r: Realm ->
                r.where(CharacterDb::class.java).equalTo("type", "default").findAll()
                    .deleteAllFromRealm()
            }
            flagClearDb = false
        }
    }


}