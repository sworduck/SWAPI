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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.adapter.SearchFragmentAdapter
import com.example.swapi.api.CharacterService
import com.example.swapi.api.RetrofitBuilder
import com.example.swapi.data.*
import com.example.swapi.data.cache.CharacterRoomDataBase
import com.example.swapi.data.cloud.FilmCloudList
import com.example.swapi.databinding.SearchFragmentBinding
import com.example.swapi.utilis.Status
import com.example.swapi.viewmodel.SearchViewModel
import com.example.swapi.viewmodel.SearchViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Retrofit


class SearchFragment : Fragment() {
    private lateinit var binding: SearchFragmentBinding
    private var mainViewModel: SearchViewModel?=null
    private var adapter: SearchFragmentAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var retryButton: Button? = null
    private var errorMessage: TextView? = null
    private var previousPage = 1
    private var countCachedPages = 1

    private val onClickListener: SearchFragmentAdapter.OnClickListener =
        object : SearchFragmentAdapter.OnClickListener {
            override fun onClickName(position: Int) {
                view?.findNavController()
                    ?.navigate(SearchFragmentDirections.actionSearchFragmentToCharacterDescriptionFragment2(
                        position,
                        "search"))
            }

            override fun onClickFavoriteOnSearchOrFavoritePage(): Boolean {
                return false
            }

            override fun onClickFavoriteButton(type: String, id: Int) {
                val dao =
                    CharacterRoomDataBase.getDataBase(requireActivity()).characterDataBaseDao()
                CoroutineScope(Job() + Dispatchers.IO).launch {
                    var character = dao.getCharacter(id)
                    if (type == "default")
                        character.type = "favorite"
                    else//type=="favorite"
                        character.type = "default"
                    dao.update(character)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false)
        recyclerView = binding.charactersRecyclerView
        retryButton = binding.retryButton
        progressBar = binding.progresbar
        errorMessage = binding.errorMessage
        fetchFilmList()
        mainViewModel = ViewModelProvider(this, SearchViewModelFactory(requireActivity()))
            .get(SearchViewModel::class.java)
        setupUI()

        if (mainViewModel?.page?.value == 0) {
            mainViewModel?.page?.value = 1
        }
        mainViewModel?.page?.observe(viewLifecycleOwner, Observer {
            setupObservers(it)
        })

        retryButton?.setOnClickListener {
            if (retryButton?.visibility == View.VISIBLE) {
                this.mainViewModel?.page?.value = previousPage
            }
        }

        binding.next.setOnClickListener {
            if (mainViewModel?.page?.value ?: 1 + 1 in 1..9) {//9 потому что всего 82 персонажа, 9 страниц
                previousPage = mainViewModel?.page?.value ?: 1
                mainViewModel?.page?.value = (mainViewModel?.page?.value)?.plus(1)
            }
        }

        binding.previous.setOnClickListener {
            if (mainViewModel?.page?.value ?: 1 - 1 in 1..9) {//9 потому что всего 82 персонажа, 9 страниц
                previousPage = mainViewModel?.page?.value ?: 1
                mainViewModel?.page?.value = (mainViewModel?.page?.value)?.minus(1)
            }
        }
        return binding.root
    }

    private fun setupUI() {
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        adapter = SearchFragmentAdapter(arrayListOf(), onClickListener)
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                recyclerView?.context,
                (recyclerView?.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView?.adapter = adapter
    }

    private fun setupObservers(page: Int) {
        mainViewModel?.getCharacterList(page)?.observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView?.visibility = View.VISIBLE
                        binding.next.visibility = View.VISIBLE
                        binding.previous.visibility = View.VISIBLE
                        progressBar?.visibility = View.GONE
                        retryButton?.visibility = View.GONE
                        errorMessage?.visibility = View.GONE

                        resource.data?.let { characterDataList ->
                            retrieveList(characterDataList)
                        }
                        if (countCachedPages < this.mainViewModel?.page?.value ?: 1) {
                            countCachedPages = this.mainViewModel?.page?.value ?: 1
                        }
                    }
                    Status.ERROR -> {
                        retryButton?.visibility = View.VISIBLE
                        errorMessage?.visibility = View.VISIBLE
                        recyclerView?.visibility = View.GONE
                        progressBar?.visibility = View.GONE
                        binding.next.visibility = View.GONE
                        binding.previous.visibility = View.GONE

                        when (it.message) {
                            "HTTP 404 NOT FOUND" -> {
                                errorMessage?.text = "Отсутсвует интернет, попробуйте еще раз"
                            }
                            "Unable to resolve host \"swapi.dev\": No address associated with hostname" -> {
                                errorMessage?.text = "Отсутсвует интернет, попробуйте еще раз"
                            }
                            else -> {
                                errorMessage?.text = "Неизвестная ошибка, попробуйте еще раз"
                            }
                        }
                    }
                    Status.LOADING -> {
                        progressBar?.visibility = View.VISIBLE
                        recyclerView?.visibility = View.GONE
                        retryButton?.visibility = View.GONE
                        errorMessage?.visibility = View.GONE
                        binding.next.visibility = View.GONE
                        binding.previous.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(characterList: List<CharacterData>) {
        adapter.apply {
            this?.addCharacterList(characterList.sortedBy { it.id })
            this?.notifyDataSetChanged()
        }
    }

    private fun fetchFilmList() {
        val filmDao = CharacterRoomDataBase.getDataBase(requireActivity()).filmDataBaseDao()
        var filmList: FilmCloudList? = null
        CoroutineScope(Job() + Dispatchers.IO).launch {
            var filmDbList = filmDao.getAllFilm()
            if (filmDbList.isEmpty()) {
                val service = RetrofitBuilder.apiService
                val typeCharacter = object : TypeToken<FilmCloudList>() {}.type
                filmList = Gson().fromJson(service.fetchFilmList().string(), typeCharacter)
                filmList?.results?.let {
                    filmDao.insertList(it.mapIndexed { index, filmCloud ->
                        filmCloud.mapToFilmDataBaseEntity(index)
                    })
                }
            }
        }

    }
}