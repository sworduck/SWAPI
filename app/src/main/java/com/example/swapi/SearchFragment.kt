package com.example.swapi

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.swapi.databinding.SearchFragmentBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class SearchFragment : Fragment() {
    companion object {
        fun newInstance() = SearchFragment()
    }

    private val BASE_URL = "https://swapi.dev/api/"

    private var urlId:Int = 1

    private lateinit var viewModel: SearchViewModel

    private lateinit var binding: SearchFragmentBinding

    private var adapter:RecyclerSearchFragmentAdapter? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.search_fragment,container,false)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
        val service = retrofit.create(CharacterService::class.java)
        val gson = Gson()
        val typelist = object: TypeToken<CharacterList>(){}.type
        val scope = CoroutineScope(Job() + Main)
        var list: CharacterList? = null
        adapter = RecyclerSearchFragmentAdapter(mutableListOf())
        binding.charactersRecyclerView.adapter = adapter

        scope.launch {
            list =gson.fromJson(service.fetchCharacters(urlId).string(),typelist)
            val listOfCharacters:MutableList<String> = mutableListOf()
            for(i in list!!.results!!.indices){
                listOfCharacters.add(i,list!!.results!![i].name)
            }
            adapter = RecyclerSearchFragmentAdapter(listOfCharacters)
            binding.charactersRecyclerView.adapter = adapter

        }

        binding.next.setOnClickListener {
            if (list!!.next !=null){
                scope.launch {
                    urlId++
                    list =gson.fromJson(service.fetchCharacters(urlId).string(),typelist)
                    val listOfCharacters:MutableList<String> = mutableListOf()
                    for(i in list!!.results!!.indices){
                        listOfCharacters.add(i,list!!.results!![i].name)
                    }
                    adapter = RecyclerSearchFragmentAdapter(listOfCharacters)
                    adapter!!.setPage(urlId-1)
                    binding.charactersRecyclerView.adapter = adapter
                }
            }
        }
        binding.previous.setOnClickListener {
            if (list!!.previous !=null){
                scope.launch {
                    urlId--
                    list =gson.fromJson(service.fetchCharacters(urlId).string(),typelist)
                    val listOfCharacters:MutableList<String> = mutableListOf()
                    for(i in list!!.results!!.indices){
                        listOfCharacters.add(i,list!!.results!![i].name)
                    }
                    adapter = RecyclerSearchFragmentAdapter(listOfCharacters)
                    adapter!!.setPage(urlId-1)
                    binding.charactersRecyclerView.adapter = adapter
                }
            }
        }



        //binding.charactersRecyclerView.adapter = RecyclerSearchFragmentAdapter(listOf(b))
        return binding.root
    }



}