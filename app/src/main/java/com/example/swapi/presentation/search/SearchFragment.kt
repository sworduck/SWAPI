package com.example.swapi.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.swapi.databinding.SearchFragmentBinding
import com.example.swapi.utilis.ErrorType
import com.example.swapi.utilis.Resource
import com.example.swapi.utilis.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: SearchFragmentBinding
    private val searchViewModel: SearchViewModel by viewModels()
    //при вызове происходит проблема с searchViewModel.getClickFavoriteButton() - из-за него ошибка  Can't access ViewModels from detached fragment
    private var adapter:SearchFragmentAdapter? = null //SearchFragmentAdapter(::onClickName, ::onClickFavoriteOnSearchOrFavoritePage,
    // searchViewModel.clickFavoriteButton())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = SearchFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initObservers()
        searchViewModel.viewCreated()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initObservers() {

        searchViewModel.page.observe(viewLifecycleOwner) {
            searchViewModel.getCharacterList()
        }
        searchViewModel.getCharacterList().observe(viewLifecycleOwner) {
            it?.let { resource ->
                setVisibility(resource.status)
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { characterDataList ->
                            adapter?.submitList(characterDataList)
                        }
                    }
                    Status.ERROR -> {
                        when (it.message) {
                            ErrorType.NO_CONNECTION -> {
                                binding.errorMessage?.text = "Отсутсвует интернет, попробуйте еще раз"
                            }
                            ErrorType.SERVICE_UNAVAILABLE-> {
                                binding.errorMessage?.text = "SWAPI не отвечает, попробуйте еще раз"
                            }
                            ErrorType.GENERIC_ERROR -> {
                                binding.errorMessage?.text = "Неизвестная ошибка, попробуйте еще раз"
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initView() {

        adapter = SearchFragmentAdapter(::onClickName, ::onClickFavoriteOnSearchOrFavoritePage,
             searchViewModel.clickFavoriteButton)

        binding.charactersRecyclerView.adapter = adapter

        binding.retryButton.setOnClickListener {
            searchViewModel.retryClicked(binding.retryButton.isVisible)
        }

        binding.next.setOnClickListener {
            searchViewModel.nextClicked()
        }

        binding.previous.setOnClickListener {
            searchViewModel.previousClicked()
        }
    }

    private fun onClickName(position: Int) {
        view?.findNavController()
            ?.navigate(SearchFragmentDirections.actionSearchFragmentToCharacterDescriptionFragment2(
                position,
                "search"))
    }

    private fun onClickFavoriteOnSearchOrFavoritePage(): Boolean {
        return false
    }

    private fun setVisibility(status: Status){
        when (status) {
            Status.SUCCESS -> {
                binding.charactersRecyclerView.isVisible = true
                binding.next.isVisible = true
                binding.previous.isVisible = true
                binding.progressbar.isVisible = false
                binding.retryButton.isVisible = false
                binding.errorMessage.isVisible = false
            }
            Status.ERROR -> {
                binding.retryButton.isVisible = true
                binding.errorMessage.isVisible = true
                binding.charactersRecyclerView.isVisible = false
                binding.progressbar.isVisible = false
                binding.next.isVisible = false
                binding.previous.isVisible = false
            }
            Status.LOADING -> {
                binding.progressbar.isVisible = true
                binding.charactersRecyclerView.isVisible = false
                binding.retryButton.isVisible = false
                binding.errorMessage.isVisible = false
                binding.next.isVisible = false
                binding.previous.isVisible = false
            }
        }
    }
}