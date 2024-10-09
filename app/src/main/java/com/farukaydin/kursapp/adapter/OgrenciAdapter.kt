package com.farukaydin.kursapp.adapter

import android.app.DirectAction
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.farukaydin.kursapp.databinding.RecyclerRowBinding
import com.farukaydin.kursapp.model.Ogrenci
import com.farukaydin.kursapp.view.anaGirisFragmentDirections

class OgrenciAdapter(val ogrenciList : List<Ogrenci>) : RecyclerView.Adapter<OgrenciAdapter.OgrenciHolder>() {

    class OgrenciHolder(val binding :RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OgrenciHolder {
        val RecyclerRowBinding  = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OgrenciHolder(RecyclerRowBinding )

    }

    override fun getItemCount(): Int {
        return  ogrenciList.size

    }


    override fun onBindViewHolder(holder: OgrenciHolder, position: Int) {
        holder.binding.recyclerViewTextView.text = ogrenciList[position].isim
        holder.itemView.setOnClickListener {
            val action = anaGirisFragmentDirections.actionAnaGirisToOgrenciBilgi("eski",ogrenciList[position].id)
            Navigation.findNavController(it).navigate(action)
        }


    }

}