package com.example.swapi

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.swapi.databinding.FavoriteCharactersFragmentBinding

class FavoriteCharactersFragment : Fragment() {


    private lateinit var viewModel: FavoriteCharactersViewModel

    private lateinit var binding: FavoriteCharactersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.favorite_characters_fragment,container,false)

        viewModel = ViewModelProvider(this)[FavoriteCharactersViewModel::class.java]
        return binding.root
    }


}