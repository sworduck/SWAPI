package com.example.swapi.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.swapi.R
import com.example.swapi.adapter.SearchFragmentAdapter
import com.example.swapi.data.CharacterData
import com.example.swapi.data.cache.CharacterDataBaseEntity
import com.example.swapi.data.cache.CharacterRoomDataBase
import com.example.swapi.databinding.FavoriteCharactersFragmentBinding
import com.example.swapi.presentation.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private lateinit var binding: FavoriteCharactersFragmentBinding

    private val favoriteViewModel:FavoriteViewModel by viewModels()

    private val onClickListener: SearchFragmentAdapter.OnClickListener =
        object : SearchFragmentAdapter.OnClickListener {
            override fun onClickName(position: Int) {
                view?.findNavController()?.navigate(FavoriteFragmentDirections
                    .actionFavoriteCharactersFragmentToCharacterDescriptionFragment(position,
                        "favorite"))
            }

            override fun onClickFavoriteOnSearchOrFavoritePage(): Boolean {
                return true
            }
        }
    private var adapter: SearchFragmentAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.favorite_characters_fragment,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView(){
        adapter = SearchFragmentAdapter(arrayListOf(), onClickListener,favoriteViewModel.clickFavoriteButton)
        favoriteViewModel.getCharacterListByType()
        binding.favoriteRecyclerView.adapter = adapter
    }

    private fun initObservers(){
        favoriteViewModel.listCharacter.observe(viewLifecycleOwner, Observer { dbList ->
            retrieveList(dbList.map { characterDataBaseEntity ->
                characterDataBaseEntity.mapToCharacterData()
            })
        })
    }

    private fun retrieveList(characterList: List<CharacterData>) {
        adapter.apply {
            this?.addCharacterList(characterList)
            this?.notifyDataSetChanged()
        }
    }




}