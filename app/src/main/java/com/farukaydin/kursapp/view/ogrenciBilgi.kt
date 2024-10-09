package com.farukaydin.kursapp.view
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.room.Room
import com.farukaydin.kursapp.databinding.FragmentOgrenciBilgiBinding
import com.farukaydin.kursapp.model.Ogrenci
import com.farukaydin.kursapp.roomdb.OgrenciDAO
import com.farukaydin.kursapp.roomdb.OgrenciDatabase
import java.io.ByteArrayOutputStream
import com.farukaydin.kursapp.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.internal.operators.flowable.FlowableBlockingSubscribe.subscribe
import io.reactivex.rxjava3.schedulers.Schedulers

class ogrenciBilgi : Fragment() {
    private val duaList = arrayOf("Subhaneke","Tahiyyat","Salli","Barik","Kunut1","Kunut2","Amentü","Rabbena Atina","Rabbena Firli","Fatiha","Fil","Kureyş","Maun","Kevser","Kafirun","Nasr","Tebbet","İhlas","Felak","Nas")
    private val cuzList = arrayOf("1.Ders", "2.Ders", "3.Ders", "4.Ders", "5.Ders", "6.Ders", "7.Ders", "8.Ders", "9.Ders", "10.Ders",
        "11.Ders", "12.Ders", "13.Ders", "14.Ders", "15.Ders", "16.Ders", "17.Ders", "18.Ders", "19.Ders", "20.Ders",
        "21.Ders", "22.Ders", "23.Ders", "24.Ders", "25.Ders", "26.Ders", "27.Ders", "28.Ders")
    private val kuranList = arrayOf("Bakara", "Al-i İmran", "Nisa", "Maide")
    private lateinit var checkedItemsDua: BooleanArray
    private lateinit var checkedItemsCuz: BooleanArray
    private lateinit var checkedItemsKuran: BooleanArray
    private lateinit var galleryPermissionManager: GalleryPermissionManager
    private lateinit var galleryImagePicker: GalleryImagePicker
    private lateinit var db: OgrenciDatabase
    private lateinit var ogrenciDao: OgrenciDAO
    private val mDisposable = CompositeDisposable()
    private  var secilenOgrenci  : Ogrenci? = null

    private var _binding: FragmentOgrenciBilgiBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(), OgrenciDatabase::class.java, "Ogrenciler").build()
        ogrenciDao = db.ogrenciDao()
        checkedItemsDua = BooleanArray(duaList.size)
        checkedItemsCuz = BooleanArray(cuzList.size)
        checkedItemsKuran = BooleanArray(kuranList.size)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOgrenciBilgiBinding.inflate(inflater, container, false)
        val view = binding.root

        galleryPermissionManager = GalleryPermissionManager(this) {
            galleryImagePicker.selectImageFromGallery()
        }
        galleryImagePicker = GalleryImagePicker(this) { bitmap ->
            binding.imageView.setImageBitmap(bitmap)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView.setOnClickListener { galleryPermissionManager.requestPermission(it) }
        binding.kaydetButton.setOnClickListener { kaydet(it) }
        binding.silButton.setOnClickListener { sil(it) }
        binding.guncelleButton.setOnClickListener { guncelle(it) }
        binding.duaTextView.setOnClickListener { showDuaList() }
        binding.cuzTextView.setOnClickListener { showCuzList() }
        binding.kuranTextView.setOnClickListener { showKuranList() }

        arguments?.let {
            val bilgi = ogrenciBilgiArgs.fromBundle(it).bilgi
            if (bilgi == "yeni") {
                secilenOgrenci = null
                binding.kaydetButton.isEnabled = true
                binding.silButton.isEnabled = false
                binding.guncelleButton.isEnabled = false
                binding.isimEditText.setText("")
                binding.yasEditText.setText("")
                binding.telefonNoEditText.setText("")
            } else {
                binding.kaydetButton.isEnabled = false
                binding.silButton.isEnabled = true
                binding.guncelleButton.isEnabled = true
                val id =    ogrenciBilgiArgs.fromBundle(it).id
                mDisposable.add(
                    ogrenciDao.findById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse)

                )
            }




        }
    }
    private fun showDuaList() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Dua Seç")
        builder.setMultiChoiceItems(duaList, checkedItemsDua) { _, which, isChecked ->
            checkedItemsDua[which] = isChecked
        }
        builder.setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showCuzList() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Cüz Seç")
        builder.setMultiChoiceItems(cuzList, checkedItemsCuz) { _, which, isChecked ->
            checkedItemsCuz[which] = isChecked
        }
        builder.setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showKuranList() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Kuran Seç")
        builder.setMultiChoiceItems(kuranList, checkedItemsKuran) { _, which, isChecked ->
            checkedItemsKuran[which] = isChecked
        }
        builder.setPositiveButton("Tamam") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
    private  fun handleResponse(ogrenci : Ogrenci){
        val bitmap = BitmapFactory.decodeByteArray(ogrenci.gorsel,0,ogrenci.gorsel.size)
        binding.imageView.setImageBitmap(bitmap)
        binding.isimEditText.setText(ogrenci.isim)
        binding.yasEditText.setText(ogrenci.yas)
        binding.telefonNoEditText.setText(ogrenci.telNo)

        // Dua, Cüz ve Kuran seçimlerini yükle
        checkedItemsDua = BooleanArray(duaList.size) { ogrenci.dua.contains(duaList[it]) }
        checkedItemsCuz = BooleanArray(cuzList.size) { ogrenci.cuz.contains(cuzList[it]) }
        checkedItemsKuran = BooleanArray(kuranList.size) { ogrenci.kuran.contains(kuranList[it]) }

        secilenOgrenci = ogrenci

    }

    fun kaydet(view: View) {
        val isim = binding.isimEditText.text.toString()
        val yas = binding.yasEditText.text.toString()
        val telNo = binding.telefonNoEditText.text.toString()

        // ImageView'de gösterilen Bitmap'i alalım
        val imageViewDrawable = binding.imageView.drawable

        if (isim.isBlank() || yas.isBlank() || telNo.isBlank()) {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageViewDrawable != null) {
            try {
                // ImageView'deki drawable'ı Bitmap olarak alıyoruz
                val secilenBitmap = (imageViewDrawable as BitmapDrawable).bitmap

                // Bitmap'i küçült
                val kucukBitmap = kucukBitmapOlustur(secilenBitmap, 300)

                // ByteArray'e sıkıştırma
                val outputStream = ByteArrayOutputStream()
                kucukBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
                val byteDizisi = outputStream.toByteArray()

                val duaSecimleri = duaList.filterIndexed { index, _ -> checkedItemsDua[index] }
                val cuzSecimleri = cuzList.filterIndexed { index, _ -> checkedItemsCuz[index] }
                val kuranSecimleri = kuranList.filterIndexed { index, _ -> checkedItemsKuran[index] }

                val ogrenci = Ogrenci(isim, yas, telNo, byteDizisi, duaSecimleri, cuzSecimleri, kuranSecimleri)

                mDisposable.add(
                    ogrenciDao.insert(ogrenci)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                handleResponseForInsert()
                                Toast.makeText(requireContext(), "Kayıt başarıyla yapıldı!", Toast.LENGTH_SHORT).show()
                            },
                            { error ->
                                Log.e("OgrenciBilgi", "Error inserting student: ${error.message}")
                                Toast.makeText(requireContext(), "Lütfen Doğru Görsel Seçin ", Toast.LENGTH_SHORT).show()
                            }
                        )
                )
            } catch (e: Exception) {
                Log.e("KaydetHata", "Görsel işlenirken hata: ${e.message}")
                Toast.makeText(requireContext(), "Görseli işlerken bir hata oluştu", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Lütfen bir görsel seçin", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleResponseForInsert (){
        // bir önceki fragmaente dön
        val action = ogrenciBilgiDirections.actionOgrenciBilgiToAnaGiris()
        Navigation.findNavController(requireView()).navigate(action)
    }

    fun sil(view: View) {
        // Silme fonksiyonu burada uygulanacak
        if(secilenOgrenci != null) {
            mDisposable.add(
                ogrenciDao.delete(ogrenci = secilenOgrenci!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            handleResponseForInsert()
                        },
                        { error ->
                            // Handle error, e.g., log the error and show a Toast message
                            Log.e("OgrenciBilgi", "Error inserting student: ${error.message}")
                            Toast.makeText(requireContext(), "Error saving student", Toast.LENGTH_SHORT).show()
                        }
                    )
            )
        }
    }

    fun guncelle(view: View) {
        // Güncelleme fonksiyonu burada uygulanacak
        // Öğrenci var mı kontrol et
        if (secilenOgrenci != null) {
            // EditText'lerden yeni değerleri al
            val yeniIsim = binding.isimEditText.text.toString()
            val yeniYas = binding.yasEditText.text.toString()
            val yeniTelNo = binding.telefonNoEditText.text.toString()

            // ImageView'den görseli al
            val imageViewDrawable = binding.imageView.drawable

            // Yeni bilgileri secilenOgrenci nesnesine ata
            secilenOgrenci!!.isim = yeniIsim
            secilenOgrenci!!.yas = yeniYas
            secilenOgrenci!!.telNo = yeniTelNo

            if (imageViewDrawable != null) {
                // Bitmap'i al ve küçült
                val secilenBitmap = (imageViewDrawable as BitmapDrawable).bitmap
                val kucukBitmap = kucukBitmapOlustur(secilenBitmap, 300)

                // Bitmap'i ByteArray'e çevir
                val outputStream = ByteArrayOutputStream()
                kucukBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
                val byteDizisi = outputStream.toByteArray()

                // secilenOgrenci'nin görselini güncelle
                secilenOgrenci!!.gorsel = byteDizisi
            }
            val duaSecimleri = duaList.filterIndexed { index, _ -> checkedItemsDua[index] }
            val cuzSecimleri = cuzList.filterIndexed { index, _ -> checkedItemsCuz[index] }
            val kuranSecimleri = kuranList.filterIndexed { index, _ -> checkedItemsKuran[index] }

            secilenOgrenci!!.dua = duaSecimleri
            secilenOgrenci!!.cuz = cuzSecimleri
            secilenOgrenci!!.kuran = kuranSecimleri

            // Güncellenmiş bilgileri veritabanına gönder
            mDisposable.add(
                ogrenciDao.update(secilenOgrenci!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                        {
                            val action = ogrenciBilgiDirections.actionOgrenciBilgiToAnaGiris()
                            Navigation.findNavController(requireView()).navigate(action)
                        },
                        { error ->
                            // Handle error, e.g., log the error and show a Toast message
                            Log.e("OgrenciBilgi", "Error inserting student: ${error.message}")
                            Toast.makeText(requireContext(), "Error saving student", Toast.LENGTH_SHORT).show()
                        }
                    )
            )
        } else {
            Toast.makeText(requireContext(), "Güncellenecek öğrenci seçilmedi!", Toast.LENGTH_SHORT).show()
        }
    }

    fun gorselSec(view: View) {
        // Görsel seçim fonksiyonu burada uygulanacak
    }







    private fun kucukBitmapOlustur(kullanicininSectigiBitmap: Bitmap, maximumBoyut: Int): Bitmap {
        var width = kullanicininSectigiBitmap.width
        var height = kullanicininSectigiBitmap.height
        val bitmapOrani = width.toDouble() / height.toDouble()

        if (bitmapOrani < 1) {
            height = maximumBoyut
            width = (height * bitmapOrani).toInt()
        } else {
            width = maximumBoyut
            height = (width / bitmapOrani).toInt()
        }

        return Bitmap.createScaledBitmap(kullanicininSectigiBitmap, width, height, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}