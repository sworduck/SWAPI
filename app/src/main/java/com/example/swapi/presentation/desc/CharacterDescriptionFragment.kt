package com.example.swapi.presentation.desc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.swapi.databinding.CharacterDescriptionFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDescriptionFragment : Fragment() {
    private lateinit var binding: CharacterDescriptionFragmentBinding
    private val args: CharacterDescriptionFragmentArgs by navArgs()
    private val descriptionViewModel: CharacterDescriptionViewModel by viewModels()
    private val adapter = DescriptionFilmAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = CharacterDescriptionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
        descriptionViewModel.viewCreated(args.position)
    }

    private fun initView() {
        binding.descriptionRecycler.adapter = adapter
        binding.imageButton.setOnClickListener {
            descriptionViewModel.buttonClicked(args.position)
        }
        binding.back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initObservers() {
        descriptionViewModel.filmListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        descriptionViewModel.buttonStateLiveData.observe(viewLifecycleOwner) {
            binding.imageButton.setBackgroundResource(it)
        }
        descriptionViewModel.characterDescription.observe(viewLifecycleOwner) {
            binding.textViewDescription.text = it
        }
    }
}