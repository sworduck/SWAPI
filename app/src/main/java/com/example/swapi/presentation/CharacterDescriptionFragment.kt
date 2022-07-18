package com.example.swapi.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.swapi.R
import com.example.swapi.adapter.DescriptionFilmAdapter
import com.example.swapi.databinding.CharacterDescriptionFragmentBinding
import com.example.swapi.domain.ClickFavoriteButton
import com.example.swapi.presentation.viewmodel.CharacterDescriptionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDescriptionFragment : Fragment() {
    private lateinit var binding: CharacterDescriptionFragmentBinding
    private var args:CharacterDescriptionFragmentArgs? = null //CharacterDescriptionFragmentArgs.fromBundle(requireArguments())
    private val descriptionViewModel:CharacterDescriptionViewModel by viewModels()
    private val adapter = DescriptionFilmAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.character_description_fragment,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.descriptionRecycler.adapter = adapter
        args = CharacterDescriptionFragmentArgs.fromBundle(requireArguments())
        initView()
        initObservers()
        descriptionViewModel.viewCreated(args?.position ?:1)
        super.onViewCreated(view, savedInstanceState)
    }
    private fun initView(){
        binding.imageButton.setOnClickListener {
            descriptionViewModel.buttonClicked(args?.position ?:1)
        }
        binding.back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initObservers(){
        descriptionViewModel.filmListLiveData.observe(viewLifecycleOwner, Observer {
            adapter.addFilmList(it)
            adapter.notifyDataSetChanged()
        })
        descriptionViewModel.buttonStateLiveData.observe(viewLifecycleOwner, Observer {
            binding.imageButton.setBackgroundResource(it)
        })
        descriptionViewModel.characterDescription.observe(viewLifecycleOwner, Observer {
            binding.textViewDescription.text = it
        })
    }
}