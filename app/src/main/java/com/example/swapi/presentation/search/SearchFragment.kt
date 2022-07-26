package com.example.swapi.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.swapi.data.CharacterData
import com.example.swapi.databinding.SearchFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: SearchFragmentBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val adapter: SearchFragmentAdapter = SearchFragmentAdapter(::onClickName,
        ::onClickFavoriteOnSearchOrFavoritePage,
        ::clickFavoriteButton,
        ::removeItem)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
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

        searchViewModel.characterDataList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            setVisibility(true)
        }

        searchViewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.errorMessage.text = it
            setVisibility(false)
        }
    }

    private fun clickFavoriteButton(characterData: CharacterData) {
        searchViewModel.onClickFavoriteButton(characterData)
    }

    private fun initView() {
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
                position))
    }

    private fun onClickFavoriteOnSearchOrFavoritePage(): Boolean {
        return false
    }

    private fun removeItem(characterData: CharacterData) {
        val list = adapter.currentList
        list.remove(characterData)
        adapter.submitList(list)
    }

    private fun setVisibility(boolean: Boolean) {
        if (boolean) {
            binding.charactersRecyclerView.isVisible = true
            binding.next.isVisible = true
            binding.previous.isVisible = true
            binding.progressbar.isVisible = false
            binding.retryButton.isVisible = false
            binding.errorMessage.isVisible = false
        } else {
            binding.retryButton.isVisible = true
            binding.errorMessage.isVisible = true
            binding.charactersRecyclerView.isVisible = false
            binding.progressbar.isVisible = false
            binding.next.isVisible = false
            binding.previous.isVisible = false
        }
    }
}