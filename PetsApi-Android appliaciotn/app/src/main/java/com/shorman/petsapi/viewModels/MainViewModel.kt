package com.shorman.petsapi.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shorman.petsapi.models.PetItem
import com.shorman.petsapi.others.Resource
import com.shorman.petsapi.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response


class MainViewModel @ViewModelInject constructor(private val repo:Repository):ViewModel() {

    val allPets = MutableLiveData<Resource<ArrayList<PetItem>>>()

    private fun getAllPets() = viewModelScope.launch {
        allPets.postValue(Resource.loading(null))
        val response = repo.getAllPets()
        allPets.postValue(Resource.success(response))
    }

    init {
        getAllPets()
    }
}