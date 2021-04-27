package com.shorman.petsapi.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.shorman.petsapi.R
import com.shorman.petsapi.models.PetNetworkItem
import com.shorman.petsapi.others.SessionManager
import com.shorman.petsapi.retrofit.PetApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.share_pet_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class SharePetFragment:Fragment(R.layout.share_pet_fragment) {

    @Inject
    lateinit var petsApi:PetApi
    lateinit var petImage:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        petImage = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            btnAddImage.setOnClickListener {
                ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start()
            }

            btnShare.setOnClickListener {
                progressBar2.isVisible = true
                val petItemNetwork = PetNetworkItem(
                        petAge = etPetAge.text.toString().toInt(),
                        petBreed = etPetBreed.text.toString(),
                        petColor = etPetColor.text.toString(),
                        petName = etPetName.text.toString(),
                        petImage = petImage
                )
                CoroutineScope(Dispatchers.Main).launch {
                    try{

                        val response = petsApi.postPet(
                                token = "bearer ${SessionManager(requireContext()).fetchAuthToken()}",
                                networkPet = petItemNetwork
                        )

                        progressBar2.visibility = View.INVISIBLE
                        Snackbar.make(requireView(),"Pet shared successfully with this info:${response.petName}",2000).show()
                    }
                    catch (e:Exception){
                        progressBar2.visibility = View.INVISIBLE
                        Snackbar.make(requireView(),"${e.message}",2000).show()
                    }

                }
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data

            val file = ImagePicker.getFile(data)!!

            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)


            val body = MultipartBody.Part.createFormData("imageFile", file.name, requestFile)

            CoroutineScope(Dispatchers.Main).launch {
                petImage = petsApi.uploadImages("bearer ${SessionManager(requireContext()).fetchAuthToken()}",body).replace("/public","")
                btnAddImage.text = "Uploaded successfully"
            }


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}