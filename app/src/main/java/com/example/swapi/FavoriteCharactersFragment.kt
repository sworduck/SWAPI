package com.example.swapi

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.swapi.adapter.SearchFragmentAdapter
import com.example.swapi.data.CharacterData
import com.example.swapi.data.CharacterDb
import com.example.swapi.databinding.FavoriteCharactersFragmentBinding
import com.example.swapi.viewmodel.FavoriteCharactersViewModel
import com.example.swapi.viewmodel.SearchViewModel
import io.realm.Realm

class FavoriteCharactersFragment : Fragment() {


    private lateinit var viewModel: FavoriteCharactersViewModel

    private lateinit var binding: FavoriteCharactersFragmentBinding

    private lateinit var adapter:SearchFragmentAdapter

    private val bigViewModel:SearchViewModel by navGraphViewModels(R.id.navigation)

    var listCharacter: MutableLiveData<List<CharacterDb>> = MutableLiveData()

    private val onClickListener:SearchFragmentAdapter.OnClickListener = object:SearchFragmentAdapter.OnClickListener{
        override fun onClickName(position: Int) {
            view!!.findNavController().
            navigate(FavoriteCharactersFragmentDirections
                .actionFavoriteCharactersFragmentToCharacterDescriptionFragment(position,"favorite"))
        }

        override fun onClickFavorite(): Boolean {
            return true
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.favorite_characters_fragment,container,false)

        viewModel = ViewModelProvider(requireActivity())[FavoriteCharactersViewModel::class.java]

        var recyclerView = binding.favoriteRecyclerView

        adapter = SearchFragmentAdapter(arrayListOf())//,viewModel)
        adapter!!.setOnClickListener(onClickListener)
        recyclerView.adapter = adapter


        val realm = Realm.getDefaultInstance()
        listCharacter.value = realm.where(CharacterDb::class.java).equalTo("type","favorite").findAll()

        listCharacter.observe(viewLifecycleOwner, Observer {dblist->
            retrieveList(dblist.map { characterDb->
                characterDb.map()
            })
        })
        return binding.root
    }

    private fun retrieveList(characterList: List<CharacterData>) {
        adapter.apply {
            this!!.addCharacterList(characterList)
            this.notifyDataSetChanged()
        }
    }


}