package com.example.swapi.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.R
import com.example.swapi.adapter.SearchFragmentAdapter
import com.example.swapi.data.*
import com.example.swapi.databinding.SearchFragmentBinding
import com.example.swapi.presentation.viewmodel.SearchViewModel
import com.example.swapi.utilis.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: SearchFragmentBinding
    private val searchViewModel: SearchViewModel by viewModels()
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
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.charactersRecyclerView
        retryButton = binding.retryButton
        progressBar = binding.progresbar
        errorMessage = binding.errorMessage
        initView()
        initObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObservers() {
        searchViewModel?.page?.observe(viewLifecycleOwner, Observer {
            getCharacterList(it)
        })
    }

    private fun initView() {
        recyclerView?.layoutManager = LinearLayoutManager(activity)

        adapter = SearchFragmentAdapter(arrayListOf(), onClickListener,searchViewModel.clickFavoriteButton)

        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                recyclerView?.context,
                (recyclerView?.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView?.adapter = adapter

        fetchFilmList()

        retryButton?.setOnClickListener {
            if (retryButton?.visibility == View.VISIBLE) {
                this.searchViewModel?.page?.value = previousPage
            }
        }

        binding.next.setOnClickListener {
            if (searchViewModel?.page?.value ?: 1 + 1 in 1..9) {//9 потому что всего 82 персонажа, 9 страниц
                previousPage = searchViewModel?.page?.value ?: 1
                searchViewModel?.page?.value = (searchViewModel?.page?.value)?.plus(1)
            }
        }

        binding.previous.setOnClickListener {
            if (searchViewModel?.page?.value ?: 1 - 1 in 1..9) {//9 потому что всего 82 персонажа, 9 страниц
                previousPage = searchViewModel?.page?.value ?: 1
                searchViewModel?.page?.value = (searchViewModel?.page?.value)?.minus(1)
            }
        }
    }

    private fun getCharacterList(page: Int) {
        searchViewModel?.getCharacterList(page)?.observe(viewLifecycleOwner, Observer {
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
                        if (countCachedPages < this.searchViewModel?.page?.value ?: 1) {
                            countCachedPages = this.searchViewModel?.page?.value ?: 1
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
        searchViewModel.saveFilmList()
    }
}