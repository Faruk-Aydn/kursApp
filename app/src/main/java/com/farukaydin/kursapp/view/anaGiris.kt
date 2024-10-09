package com.farukaydin.kursapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.farukaydin.kursapp.adapter.OgrenciAdapter
import com.farukaydin.kursapp.databinding.FragmentAnaGirisBinding
import com.farukaydin.kursapp.model.Ogrenci
import com.farukaydin.kursapp.roomdb.OgrenciDAO
import com.farukaydin.kursapp.roomdb.OgrenciDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class anaGiris : Fragment() {

    private var _binding: FragmentAnaGirisBinding? = null

    private val binding get() = _binding!!
    private lateinit var db : OgrenciDatabase
    private lateinit var ogrenciDao : OgrenciDAO
    private val mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(),OgrenciDatabase::class.java,"Ogrenciler").build()
        ogrenciDao = db.ogrenciDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnaGirisBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ogrnciRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.floatingActionButton4.setOnClickListener { yeniEkle(it)}

        verileriAl()
    }
    private fun handleResponse(ogrenciler : List<Ogrenci>){
        val adapter = OgrenciAdapter(ogrenciler)
        binding.ogrnciRecyclerView.adapter = adapter

    }

    private fun verileriAl(){
        mDisposable.add(
            ogrenciDao.getall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( this :: handleResponse)
        )
    }


    fun yeniEkle(view :View){

        val action = anaGirisFragmentDirections.actionAnaGirisToOgrenciBilgi("yeni",0)
        Navigation.findNavController(view).navigate(action)


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }

}