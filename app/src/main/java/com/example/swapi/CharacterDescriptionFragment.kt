package com.example.swapi

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.swapi.adapter.DescriptionFilmAdapter
import com.example.swapi.data.CharacterDb
import com.example.swapi.data.FilmDb
import com.example.swapi.databinding.CharacterDescriptionFragmentBinding
import com.example.swapi.viewmodel.CharacterDescriptionViewModel
import com.example.swapi.viewmodel.SearchViewModel
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class CharacterDescriptionFragment : Fragment() {



    private lateinit var viewModel: CharacterDescriptionViewModel
    private lateinit var binding:CharacterDescriptionFragmentBinding
    private val bigViewModel:SearchViewModel by navGraphViewModels(R.id.navigation)

    private var textView:TextView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.character_description_fragment,container,false)
        viewModel = ViewModelProvider(requireActivity())[CharacterDescriptionViewModel::class.java]

        val args = CharacterDescriptionFragmentArgs.fromBundle(requireArguments())
        textView = binding.textViewDescription

        textView!!.text = "alo"

        val scope = CoroutineScope(Job() + Dispatchers.Main)
        val realm = Realm.getDefaultInstance()
        binding.textViewDescription.text = "номер чего-то: ${args.position}"


        var filmsDb: MutableList<FilmDb>? = mutableListOf()
        var filmsId:List<Int>? = null
        var adapter = DescriptionFilmAdapter(arrayListOf(FilmDb()))
        binding.descriptionRecycler.adapter = adapter
        var character: CharacterDb? =
            realm.where(CharacterDb::class.java).equalTo("id",args.position).findFirst()
        var list:List<Int> = character!!.idList!!.split(",").map {
            it.replace("/","").substringAfterLast("films").toInt()
        }


        scope.launch {

            val filmListDb = realm.where(FilmDb::class.java).findAll()
            val a = filmListDb.toString()

            for(i in list){
                filmsDb!!.add(realm.where(FilmDb::class.java).equalTo("id",i-1).findFirst()!!)
            }


            adapter.addFilmList(filmsDb!!)
            adapter.notifyDataSetChanged()
            binding.textViewDescription.text ="Name: ${character!!.name} \nMass: ${character!!.mass}" +
                    "\nHeight: ${character!!.height}"
        }



        binding.button.setOnClickListener {
            if(args.waySearchOrFavorite == "search")
            requireView().findNavController()
                .navigate(CharacterDescriptionFragmentDirections.actionCharacterDescriptionFragmentToSearchFragment())
            else
                requireView().findNavController()
                    .navigate(CharacterDescriptionFragmentDirections.actionCharacterDescriptionFragmentToFavoriteCharactersFragment())

        }
        return binding.root
    }
}