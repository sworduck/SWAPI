package com.example.swapi.presentation.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.swapi.data.CharacterData
import com.example.swapi.databinding.FavoriteCharactersFragmentBinding
import com.example.swapi.presentation.search.SearchFragmentAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FavoriteCharactersFragmentBinding

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val adapter: SearchFragmentAdapter = SearchFragmentAdapter(
        ::onClickName,
        ::onClickFavoriteOnSearchOrFavoritePage,
        ::clickFavoriteButton,
        ::removeItem)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FavoriteCharactersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        favoriteViewModel.getCharacterListByType()
        binding.favoriteRecyclerView.adapter = adapter
    }

    private fun clickFavoriteButton(characterData: CharacterData) {
        favoriteViewModel.onClickFavoriteButton(characterData)
    }

    private fun initObservers() {
        favoriteViewModel.listCharacter.observe(viewLifecycleOwner) { dbList ->
            adapter?.submitList(dbList.map { characterDataBaseEntity ->
                characterDataBaseEntity.mapToCharacterData()
            })
        }
    }

    private fun onClickName(position: Int) {
        view?.findNavController()
            ?.navigate(FavoriteFragmentDirections.actionFavoriteCharactersFragmentToCharacterDescriptionFragment(
                position))
    }

    private fun onClickFavoriteOnSearchOrFavoritePage(): Boolean {
        return true
    }

    private fun removeItem(characterData: CharacterData) {
        val list = adapter.currentList.toMutableList()
        list.remove(characterData)
        adapter.submitList(list)
    }
}