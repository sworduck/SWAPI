package com.example.swapi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.swapi.adapter.DescriptionFilmAdapter
import com.example.swapi.data.cache.CharacterDb
import com.example.swapi.data.cache.CharacterRoomDataBase
import com.example.swapi.data.cache.FilmDataBaseEntity
import com.example.swapi.data.cache.FilmDb
import com.example.swapi.databinding.CharacterDescriptionFragmentBinding
import com.example.swapi.viewmodel.SearchViewModel
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CharacterDescriptionFragment : Fragment() {
    private lateinit var binding:CharacterDescriptionFragmentBinding
    //private val mainViewModel:SearchViewModel by navGraphViewModels(R.id.navigation)

    //private var textView:TextView?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.character_description_fragment,container,false)
        val args = CharacterDescriptionFragmentArgs.fromBundle(requireArguments())
        val textView = binding.textViewDescription



        val scope = CoroutineScope(Job() + Dispatchers.IO)
        //val realm = Realm.getDefaultInstance()


        var filmsDb: MutableList<FilmDataBaseEntity> = mutableListOf()
        var filmsId:List<Int>? = null
        var adapter = DescriptionFilmAdapter(arrayListOf())
        binding.descriptionRecycler.adapter = adapter
        val characterDao = CharacterRoomDataBase.getDataBase(requireActivity()).characterDataBaseDao()
        val filmDao = CharacterRoomDataBase.getDataBase(requireActivity()).filmDataBaseDao()

        //var character: CharacterDb? = realm.where(CharacterDb::class.java).equalTo("id",args.position).findFirst()




        scope.launch {
            val character = characterDao.getCharacter(args.position)
            var list:List<Int> = character!!.idList!!.split(",").map {
                it.replace("/","").substringAfterLast("films").toInt()
            }

            val filmListDb = filmDao.getAllFilm()//realm.where(FilmDb::class.java).findAll()
            val a = filmListDb.toString()

            for(i in list){
                filmsDb.add(filmDao.getFilm(i-1))//realm.where(FilmDb::class.java).equalTo("id",i-1).findFirst()!!)
            }


            adapter.addFilmList(filmsDb)
            adapter.notifyDataSetChanged()
            textView.text ="Name: ${character!!.name} \nMass: ${character!!.mass}" +
                    "\nHeight: ${character!!.height}"
            if (character.type == "default") {
                //переключение персонажа с default на favorite
                binding.imageButton
                    .setBackgroundResource(R.drawable.ic_baseline_star_border_24)
            } else {
                //переключение персонажа с favorite на default
                binding.imageButton
                    .setBackgroundResource(R.drawable.ic_baseline_star_rate_24)
            }
        }

        binding.imageButton.setOnClickListener {
            CoroutineScope(Job()+Dispatchers.IO).launch {
                var character = characterDao.getCharacter(args.position)
                if (character.type == "default") {
                    binding.imageButton
                        .setBackgroundResource(R.drawable.ic_baseline_star_rate_24)
                    character.type = "favorite"
                } else{//type=="favorite"
                    binding.imageButton
                        .setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                    character.type = "default"
                }
                characterDao.update(character)
            }
            /*
            if (character.type == "default") {
                //переключение персонажа с default на favorite
                binding.imageButton
                    .setBackgroundResource(R.drawable.ic_baseline_star_rate_24)
                realm.executeTransaction { r ->

                    character.type = "favorite"
                }

            } else {
                //переключение персонажа с favorite на default
                binding.imageButton
                    .setBackgroundResource(R.drawable.ic_baseline_star_border_24)
                realm.executeTransaction { r ->

                    character.type = "default"
                }
            }

             */
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