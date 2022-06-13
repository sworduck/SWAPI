package com.example.swapi

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.swapi.api.ApiHelper
import com.example.swapi.api.RetrofitBuilder
import com.example.swapi.base.SearchViewModelFactory
import com.example.swapi.databinding.FavoriteCharactersFragmentBinding

class FavoriteCharactersFragment : Fragment() {


    private lateinit var viewModel: FavoriteCharactersViewModel

    private lateinit var binding: FavoriteCharactersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.favorite_characters_fragment,container,false)

        viewModel = ViewModelProvider(requireActivity())[FavoriteCharactersViewModel::class.java]
        /*
        viewModel = ViewModelProvider(this,
            SearchViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(SearchViewModel::class.java)

         */
        //viewModel.addElementFavoriteList(CharacterData(0,"1","1","1","1"))
        viewModel.getSelected()!!.observe(viewLifecycleOwner, Observer{
            binding.favtextview.text = "${it.size}"
        })


        return binding.root
    }


}