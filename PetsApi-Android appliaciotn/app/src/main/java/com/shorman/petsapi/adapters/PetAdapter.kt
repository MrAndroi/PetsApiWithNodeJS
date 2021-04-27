package com.shorman.petsapi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.shorman.petsapi.R
import com.shorman.petsapi.models.PetItem
import kotlinx.android.synthetic.main.pet_item.view.*

class PetAdapter:RecyclerView.Adapter<PetAdapter.PetViewHolder>() {


    class PetViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    private val diffUtil = object : DiffUtil.ItemCallback<PetItem>(){
        override fun areItemsTheSame(oldItem: PetItem, newItem: PetItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PetItem, newItem: PetItem): Boolean {
            return oldItem._id == newItem._id
        }

    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        return PetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pet_item,parent,false))
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = differ.currentList[position]

        holder.itemView.apply {
            ivPet.load(pet.petImage){
                placeholder(R.drawable.pet_place_holder)
                error(R.drawable.pet_place_holder)
            }
            tvPetName.text = pet.petName
            tvPetBreed.text = pet.petBreed
            tvPetColor.text = pet.petColor
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}