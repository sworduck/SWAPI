package com.example.swapi

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.adapter.SearchFragmentAdapter
import com.example.swapi.api.CharacterService
import com.example.swapi.data.*
import com.example.swapi.data.cache.CharacterDb
import com.example.swapi.data.cache.FilmDb
import com.example.swapi.data.cloud.FilmCloudList
import com.example.swapi.databinding.SearchFragmentBinding
import com.example.swapi.utilis.Status
import com.example.swapi.viewmodel.SearchViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit


class SearchFragment : Fragment() {

    private val BASE_URL = "https://swapi.dev/"

    private val mainViewModel:SearchViewModel by navGraphViewModels(R.id.navigation)


    private lateinit var binding: SearchFragmentBinding



    private var adapter:SearchFragmentAdapter? = null

    private var recyclerView:RecyclerView? = null
    private var progressBar:ProgressBar? = null
    private var retryButton:Button? = null
    private var errorMessage:TextView? = null


    private var previousPage = 1

    private var countCachedPages = 1


    private val onClickListener:SearchFragmentAdapter.OnClickListener = object:SearchFragmentAdapter.OnClickListener{
        override fun onClickName(position: Int) {
            view!!.findNavController().
            navigate(SearchFragmentDirections.actionSearchFragmentToCharacterDescriptionFragment2(position,"search"))
        }

        override fun onClickFavoriteOnSearchOrFavoritePage(): Boolean {
            return false
        }

        override fun onClickFavoriteButton(type: String,id:Int) {
            Realm.getDefaultInstance().executeTransaction { r ->
                val characterDb =
                    r.where(CharacterDb::class.java).equalTo("id", id)
                        .findFirst()
                if(type=="default")
                    characterDb!!.type = "favorite"
                else//type=="favorite"
                    characterDb!!.type = "default"
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("TAG","onCreateView")
        binding = DataBindingUtil.inflate(inflater,R.layout.search_fragment,container,false)


        recyclerView = binding.charactersRecyclerView
        retryButton = binding.retryButton
        progressBar = binding.progresbar
        errorMessage = binding.errorMessage

        fetchFilmList()
        isInternetAvailable(requireActivity())
        setupUI()

        if(mainViewModel.page.value == null){
            mainViewModel.page.value = 1
        }
        mainViewModel.page.observe(viewLifecycleOwner, Observer {
            setupObservers(it)
        })

        retryButton!!.setOnClickListener {
            if(retryButton!!.visibility==View.VISIBLE) {
                this.mainViewModel.page.value = previousPage
            }
        }

        binding.next.setOnClickListener {
            if(mainViewModel.page.value!!+1 in 1..9) {//9 потому что всего 82 персонажа, 9 страниц
                previousPage = mainViewModel.page.value!!
                mainViewModel.page.value = (mainViewModel.page.value)?.plus(1)
            }
        }

        binding.previous.setOnClickListener {
            if(mainViewModel.page.value!!-1 in 1..9) {//9 потому что всего 82 персонажа, 9 страниц
                previousPage = mainViewModel.page.value!!
                mainViewModel.page.value = (mainViewModel.page.value)?.minus(1)
            }
        }
        return binding.root
    }

    private fun setupUI() {
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        adapter = SearchFragmentAdapter(arrayListOf())
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
        mainViewModel.getCharacterList(page).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView!!.visibility = View.VISIBLE
                        binding.next.visibility=View.VISIBLE
                        binding.previous.visibility=View.VISIBLE
                        progressBar!!.visibility = View.GONE
                        retryButton!!.visibility = View.GONE
                        errorMessage!!.visibility = View.GONE

                        resource.data?.let { characterDataList ->
                            retrieveList(checkFavoriteCharacterListInResponseFromServer(characterDataList,page)) }
                        if(countCachedPages<this.mainViewModel.page.value!!){
                            countCachedPages = this.mainViewModel.page.value!!
                        }
                    }
                    Status.ERROR -> {
                            retryButton!!.visibility = View.VISIBLE
                            errorMessage!!.visibility = View.VISIBLE
                            recyclerView!!.visibility = View.GONE
                            progressBar!!.visibility = View.GONE
                            binding.next.visibility=View.GONE
                            binding.previous.visibility=View.GONE

                        when(it.message){
                            "HTTP 404 NOT FOUND"->{
                                errorMessage!!.text = "Отсутсвует интернет, попробуйте еще раз"
                            }
                            "Unable to resolve host \"swapi.dev\": No address associated with hostname"->{
                                errorMessage!!.text = "Отсутсвует интернет, попробуйте еще раз"
                            }
                            else->{
                                errorMessage!!.text = "Неизвестная ошибка, попробуйте еще раз"
                            }
                        }
                    }
                    Status.LOADING -> {
                        progressBar!!.visibility = View.VISIBLE
                        recyclerView!!.visibility = View.GONE
                        retryButton!!.visibility = View.GONE
                        errorMessage!!.visibility = View.GONE
                        binding.next.visibility=View.GONE
                        binding.previous.visibility=View.GONE
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
    private fun fetchFilmList(){
        val realm = Realm.getDefaultInstance()
        var filmList: MutableLiveData<FilmCloudList> = MutableLiveData()
        filmList.observe(viewLifecycleOwner, Observer {
            realm.executeTransactionAsync { r: Realm ->
                for (i in filmList.value!!.results!!.indices) {
                    val filmDb =
                        r.createObject(FilmDb::class.java, i)
                    filmDb.title = filmList.value!!.results!![i].title
                    filmDb.opening_crawl = filmList.value!!.results!![i].opening_crawl
                    r.insertOrUpdate(filmDb)
                }
            }
        })
        var filmDbList = realm.where(FilmDb::class.java).findAll()
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
    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}