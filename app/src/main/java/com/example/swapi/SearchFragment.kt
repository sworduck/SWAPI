package com.example.swapi

import android.os.Bundle
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
import com.example.swapi.api.RetrofitBuilder
import com.example.swapi.base.SearchViewModelFactory
import com.example.swapi.databinding.SearchFragmentBinding
import com.example.swapi.utilis.Status

class SearchFragment : Fragment() {
    companion object {
        fun newInstance() = SearchFragment()
    }

    private val BASE_URL = "https://swapi.dev/api/"

    private var urlId:Int = 1

    private lateinit var viewModel: SearchViewModel

    private lateinit var binding: SearchFragmentBinding

    private var adapter:SearchFragmentAdapter? = null

    private var recyclerView:RecyclerView? = null
    private var progressBar:ProgressBar? = null

    private var page: MutableLiveData<Int> = MutableLiveData(1)

    /*
    viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
   binding.scoreText.text = newScore.toString()
})
     */


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.search_fragment,container,false)

        //viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        recyclerView = binding.charactersRecyclerView

        progressBar = binding.progresbar

        //page.value = 1

        setupViewModel()
        setupUI()

        page.observe(viewLifecycleOwner, Observer {
            setupObservers(it)
        })

        binding.next.setOnClickListener {
            page.value = (page.value)?.plus(1)
        }

        binding.previous.setOnClickListener {
            page.value = (page.value)?.minus(1)
        }

        /*

        Realm.init(context)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build()
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
        val service = retrofit.create(CharacterService::class.java)
        val gson = Gson()
        val typelist = object: TypeToken<CharacterCloudList>(){}.type
        val scope = CoroutineScope(Job() + Main)
        var cloudList: CharacterCloudList? = null
        var dataList: List<CharacterData>? = null
        adapter = RecyclerSearchFragmentAdapter(mutableListOf())
        binding.charactersRecyclerView.adapter = adapter


        scope.launch {
            cloudList =gson.fromJson(service.fetchCharacters(urlId).string(),typelist)
            val listOfCharacters:MutableList<String> = mutableListOf()
            for(i in cloudList!!.results!!.indices){
                listOfCharacters.add(i,cloudList!!.results!![i].name)
            }
            adapter = RecyclerSearchFragmentAdapter(listOfCharacters)
            binding.charactersRecyclerView.adapter = adapter

        }

        binding.next.setOnClickListener {
            if (cloudList!!.next !=null){
                scope.launch {
                    urlId++
                    //dataList =viewModel.fetchCharacterList(urlId)
                    cloudList =gson.fromJson(service.fetchCharacters(urlId).string(),typelist)
                    val listOfCharacters:MutableList<String> = mutableListOf()
                    for(i in dataList!!.indices){
                        listOfCharacters.add(i, dataList!![i].name)
                    }
                    adapter = RecyclerSearchFragmentAdapter(listOfCharacters)
                    adapter!!.setPage(urlId-1)
                    binding.charactersRecyclerView.adapter = adapter
                }
            }
        }
        binding.previous.setOnClickListener {
            if (cloudList!!.previous !=null){
                scope.launch {
                    urlId--
                    cloudList =gson.fromJson(service.fetchCharacters(urlId).string(),typelist)
                    val listOfCharacters:MutableList<String> = mutableListOf()
                    for(i in cloudList!!.results!!.indices){
                        listOfCharacters.add(i,cloudList!!.results!![i].name)
                    }
                    adapter = RecyclerSearchFragmentAdapter(listOfCharacters)
                    adapter!!.setPage(urlId-1)
                    binding.charactersRecyclerView.adapter = adapter
                }
            }
        }

         */
        //binding.charactersRecyclerView.adapter = RecyclerSearchFragmentAdapter(listOf(b))
        return binding.root
    }
    private fun setupViewModel() {
        /*
        viewModel = ViewModelProviders.of(
            this,
            SearchViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(SearchViewModel::class.java)
         */
        viewModel = ViewModelProvider(this,SearchViewModelFactory(ApiHelper(RetrofitBuilder.apiService)))
            .get(SearchViewModel::class.java)

        // With ViewModelFactory
        //val viewModel = ViewModelProvider(this, YourViewModelFactory).get(YourViewModel::class.java)
        //Without ViewModelFactory
        //val viewModel = ViewModelProvider(this).get(YourViewModel::class.java)
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
        recyclerView!!.adapter = adapter
    }

    private fun setupObservers(page:Int) {
        viewModel.getCharacterList(page).observe(viewLifecycleOwner, Observer {
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

    private fun retrieveList(users: List<CharacterCloud>) {
        adapter.apply {
            this!!.addCharacterList(users)
            this.notifyDataSetChanged()
        }
    }


}