package com.shorman.petsapi.models

data class LoginModel(
        val success:Boolean,
        val token:String,
        val status:String
)
