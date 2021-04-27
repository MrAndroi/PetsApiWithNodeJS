package com.shorman.petsapi.retrofit

import com.shorman.petsapi.models.LoginModel
import com.shorman.petsapi.models.PetItem
import com.shorman.petsapi.models.PetNetworkItem
import com.shorman.petsapi.models.UserLoginModel
import okhttp3.MultipartBody
import retrofit2.http.*

interface PetApi {

    @GET("/pets")
    suspend fun getAllPets():ArrayList<PetItem>

    @POST("/users/login")
    suspend fun login(
            @Body userCredential: UserLoginModel
    ):LoginModel


    @POST("/pets")
    suspend fun postPet(
            @Header("Authorization") token: String,
            @Body networkPet:PetNetworkItem
    ):PetItem

    @Multipart
    @POST("imagesUpload")
    suspend fun uploadImages(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part):String

}