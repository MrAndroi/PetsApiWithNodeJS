package com.shorman.petsapi.ui.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.shorman.petsapi.R
import com.shorman.petsapi.models.UserLoginModel
import com.shorman.petsapi.others.SessionManager
import com.shorman.petsapi.retrofit.PetApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment:Fragment(R.layout.login_fragment) {

    @Inject
    lateinit var petsApi:PetApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener {
            val userLoginModel = UserLoginModel(etUserName.text.toString(),etPassword.text.toString())

            CoroutineScope(Dispatchers.Main).launch {
                progressBar.visibility = View.VISIBLE

                try{
                    val response = petsApi.login(userLoginModel)

                    if(response.success){
                        val editor = SessionManager(requireContext())
                        editor.saveAuthToken(response.token)

                        progressBar.visibility = View.INVISIBLE
                        Snackbar.make(requireView(),"Logged in successfully",2000).show()
                    }
                    else{
                        progressBar.visibility = View.INVISIBLE
                        Snackbar.make(requireView(),"Error:${response.status}",2000).show()
                    }
                }
                catch (e:Exception){
                    progressBar.visibility = View.INVISIBLE
                    Snackbar.make(requireView(),"Error:${e.message}",2000).show()
                }

            }
        }
    }
}