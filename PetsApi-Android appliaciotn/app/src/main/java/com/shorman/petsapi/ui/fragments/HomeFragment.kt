package com.shorman.petsapi.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shorman.petsapi.R
import com.shorman.petsapi.adapters.PetAdapter
import com.shorman.petsapi.others.Resource
import com.shorman.petsapi.others.Status
import com.shorman.petsapi.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.*

@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.home_fragment) {

    private val viewModel:MainViewModel by viewModels()
    private lateinit var petAdapter:PetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        petAdapter = PetAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPets.setHasFixedSize(true)
        rvPets.adapter = petAdapter
        getAllPets()

        btnGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        btnAddNewPet.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sharePetFragment)
        }
    }

    private fun getAllPets(){
        viewModel.allPets.observe(viewLifecycleOwner,{
            when(it.status){
                Status.SUCCESS -> {
                    petAdapter.differ.submitList(it.data)
                    homeProgressBar.isVisible = false
                }
                Status.LOADING -> homeProgressBar.isVisible = true
                else -> homeProgressBar.isVisible = false
            }
        })
    }
}