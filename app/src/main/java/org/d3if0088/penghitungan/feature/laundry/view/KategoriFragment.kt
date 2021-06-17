package org.d3if0088.penghitungan.feature.laundry.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.d3if0088.penghitungan.R
import org.d3if0088.penghitungan.databinding.FragmentKategoriBinding
import org.d3if0088.penghitungan.databinding.PenghitunganFragmentBinding
import org.d3if0088.penghitungan.db.LaundryDb
import org.d3if0088.penghitungan.db.LaundryEntity
import org.d3if0088.penghitungan.feature.laundry.adapter.KategoriAdapter
import org.d3if0088.penghitungan.feature.laundry.adapter.LaundryClickAction
import org.d3if0088.penghitungan.feature.laundry.adapter.LaundryListener
import org.d3if0088.penghitungan.feature.laundry.adapter.PelangganAdapter
import org.d3if0088.penghitungan.feature.laundry.viewmodel.LaundryViewModel
import org.d3if0088.penghitungan.feature.laundry.viewmodel.SimpanViewModelFactory

class KategoriFragment : Fragment() {
    private lateinit var binding: FragmentKategoriBinding
    private lateinit var kategoriAdapter: KategoriAdapter
    private var kategoriId = -1L;

    private val laundryViewModel: LaundryViewModel by lazy {
        val db = LaundryDb.getInstance(requireContext())
        val factory = SimpanViewModelFactory(db.dao)
        ViewModelProvider(this, factory).get(LaundryViewModel::class.java)
    }

    private fun simpanLaundry() {
        val kategori = binding.kategoriEditText.text.toString()
        if (TextUtils.isEmpty(kategori)) {
            Toast.makeText(context, R.string.kategori_invalid, Toast.LENGTH_LONG).show()
            return
        }
        if (kategoriId > -1) {
            laundryViewModel.updateData(LaundryEntity(kategoriId, kategori))
            kategoriId = -1L
        } else {
            laundryViewModel.tambahLaundry(kategori)
        }
        binding.kategoriEditText.text.clear()
    }

    private fun hapusData(laundryEntity: LaundryEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Anda yakin ingin menghapus kategori ini?")
            .setPositiveButton("Ya") { _, _ ->
                laundryViewModel.hapusData(laundryEntity)
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val actionBar = (requireActivity() as AppCompatActivity)
            .supportActionBar
        actionBar?.title = getString(R.string.judul_kategori)

        binding = FragmentKategoriBinding.inflate(layoutInflater, container, false)
        binding.buttonKategori.setOnClickListener { simpanLaundry() }

        kategoriAdapter = KategoriAdapter(LaundryListener { laundry, action ->
            when (action) {
                LaundryClickAction.DELETE -> hapusData(laundry)
                LaundryClickAction.UPDATE -> {
                    binding.kategoriEditText.setText(laundry.kategori)
                    kategoriId = laundry.id
                }
            }
        })

        laundryViewModel.data.observe(viewLifecycleOwner, Observer { kategori ->
            kategoriAdapter.updateData(kategori)
            Log.d("TAG", "onResponse: ${kategori}")
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = kategoriAdapter
            setHasFixedSize(true)
        }
    }
}