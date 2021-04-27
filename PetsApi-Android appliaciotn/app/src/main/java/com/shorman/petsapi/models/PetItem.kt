package com.shorman.petsapi.models

data class PetItem(
    val _id: String,
    val comments: List<Any>,
    val createdAt: String,
    val ownerId: OwnerId,
    val petAge: Int,
    val petBreed: String,
    val petColor: String,
    val petName: String,
    val updatedAt: String,
    val petImage:String
)