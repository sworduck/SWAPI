package com.example.swapi

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.swapi.adapter.SearchFragmentAdapter
import com.example.swapi.api.ApiHelper
import com.example.swapi.api.RetrofitBuilder
import com.example.swapi.base.SearchViewModelFactory
import com.example.swapi.databinding.FavoriteCharactersFragmentBinding
import io.realm.Realm

class FavoriteCharactersFragment : Fragment() {


    private lateinit var viewModel: FavoriteCharactersViewModel

    private lateinit var binding: FavoriteCharactersFragmentBinding

    private lateinit var adapter:SearchFragmentAdapter

    var listCharacter: MutableLiveData<List<CharacterDb>> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.favorite_characters_fragment,container,false)

        viewModel = ViewModelProvider(requireActivity())[FavoriteCharactersViewModel::class.java]

        var recyclerView = binding.favoriteRecyclerView

        adapter = SearchFragmentAdapter(arrayListOf())//,viewModel)
        recyclerView.adapter = adapter

        val realm = Realm.getDefaultInstance()
        listCharacter.value = realm.where(CharacterDb::class.java).equalTo("type","favorite").findAll()

        listCharacter.observe(viewLifecycleOwner, Observer {dblist->
            retrieveList(dblist.map { characterDb->
                characterDb.map()
            })
        })
        /*
        retrieveList(list.map { characterDb ->
            characterDb.map()
        })

         */

        /*
        viewModel = ViewModelProvider(this,
            SearchViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(SearchViewModel::class.java)

         */
        //viewModel.addElementFavoriteList(CharacterData(0,"1","1","1","1"))
        /*
        viewModel.getSelected()!!.observe(viewLifecycleOwner, Observer{
            retrieveList(it)
            Log.i("TAG","${it.size}")
        })

         */


        return binding.root
    }

    private fun retrieveList(characterList: List<CharacterData>) {
        adapter.apply {
            this!!.addCharacterList(characterList)
            this.notifyDataSetChanged()
        }
    }


}