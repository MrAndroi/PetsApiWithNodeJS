package com.shorman.petsapi.repo

import com.shorman.petsapi.retrofit.PetApi
import javax.inject.Inject

class Repository @Inject constructor(private val petApi: PetApi) {

    suspend fun getAllPets() = petApi.getAllPets()

}