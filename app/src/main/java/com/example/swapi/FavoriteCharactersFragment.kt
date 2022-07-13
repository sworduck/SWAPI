package com.example.swapi

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
import com.example.swapi.data.cache.CharacterDataBaseDao
import com.example.swapi.data.cache.CharacterDataBaseEntity
import com.example.swapi.data.cache.CharacterDb
import com.example.swapi.data.cache.CharacterRoomDataBase
import com.example.swapi.databinding.FavoriteCharactersFragmentBinding
import com.example.swapi.viewmodel.SearchViewModel
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoriteCharactersFragment : Fragment() {



    private lateinit var binding: FavoriteCharactersFragmentBinding



    //private val mainViewModel:SearchViewModel by navGraphViewModels(R.id.navigation)

    var listCharacter: MutableLiveData<List<CharacterDataBaseEntity>> = MutableLiveData()
    var listOfCharacter: List<CharacterDataBaseEntity> = listOf()

    private val onClickListener:SearchFragmentAdapter.OnClickListener = object:SearchFragmentAdapter.OnClickListener{
        override fun onClickName(position: Int) {
            view?.findNavController()?.
            navigate(FavoriteCharactersFragmentDirections
                .actionFavoriteCharactersFragmentToCharacterDescriptionFragment(position,"favorite"))
        }

        override fun onClickFavoriteOnSearchOrFavoritePage(): Boolean {
            return true
        }
        override fun onClickFavoriteButton(type: String,id:Int) {
            val dao = CharacterRoomDataBase.getDataBase(requireActivity()).characterDataBaseDao()
            CoroutineScope(Job()+Dispatchers.IO).launch {
                var character = dao.getCharacter(id)
                if (type == "default")
                    character.type = "favorite"
                else//type=="favorite"
                    character.type = "default"
                dao.update(character)
            }
        }
    }
    private var adapter:SearchFragmentAdapter = SearchFragmentAdapter(arrayListOf(),onClickListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.favorite_characters_fragment,container,false)


        var recyclerView = binding.favoriteRecyclerView

        adapter = SearchFragmentAdapter(arrayListOf(),onClickListener)//,viewModel)
        recyclerView.adapter = adapter

        val bd = CharacterRoomDataBase.getDataBase(requireActivity())
        val dao = bd.characterDataBaseDao()
        /*
        val realm = Realm.getDefaultInstance()
        listCharacter.value = realm.where(CharacterDb::class.java).equalTo("type","favorite").findAll()

        listCharacter.observe(viewLifecycleOwner, Observer {dblist->
            retrieveList(dblist.map { characterDb->
                characterDb.map()
            })
        })

         */
        CoroutineScope(Job()+Dispatchers.IO).launch {
            listOfCharacter = dao.getCharacterListByType("favorite")
            val list = dao.getAllCharacter()
            val size = list.size
            retrieveList(listOfCharacter.map { characterDataBaseEntity ->
                characterDataBaseEntity.mapToCharacterData() })

        }

        listCharacter.observe(viewLifecycleOwner, Observer { dbList->
            retrieveList(dbList.map { characterDataBaseEntity ->
                characterDataBaseEntity.mapToCharacterData() })
        })
        return binding.root
    }

    private fun retrieveList(characterList: List<CharacterData>) {
        adapter.apply {
            this.addCharacterList(characterList)
            this.notifyDataSetChanged()
        }
    }


}