package org.d3if0088.penghitungan.feature.laundry.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.d3if0088.penghitungan.R
import org.d3if0088.penghitungan.data.KuantitasLaundry
import org.d3if0088.penghitungan.databinding.LengkapFragmentBinding

class LengkapFragment  : Fragment() {

    private val args: LengkapFragmentArgs by navArgs()
    private lateinit var binding: LengkapFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = LengkapFragmentBinding.inflate(
            layoutInflater, container, false)
        updateUI(args.kuantitas, args.namaPelanggan, args.namaKategori)
        return binding.root
    }

    private fun updateUI(kuantitas: KuantitasLaundry, namaPelanggan: String, namaKategori: String) {
        val actionBar = (requireActivity() as AppCompatActivity)
            .supportActionBar
        binding.namaPelanggan.text = "Pelanggan : $namaPelanggan"
        binding.namaKategori.text = "Paket : $namaKategori"
        when (kuantitas) {
            KuantitasLaundry.CUCI_BANYAK -> {
                actionBar?.title = getString(R.string.judul_banyak)
                binding.imageView.setImageResource(R.drawable.basah)
                binding.textView.text = getString(R.string.saran_banyak)
            }
            KuantitasLaundry.CUCI_SEDIKIT -> {
                actionBar?.title = getString(R.string.judul_sedikit)
                binding.imageView.setImageResource(R.drawable.kering)
                binding.textView.text = getString(R.string.saran_sedikit)
            }
            else -> {
                actionBar?.title = "Error"
                binding.textView.text = getString(R.string.error)
            }
        }
    }
}