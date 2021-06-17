package org.d3if0088.penghitungan.feature.laundry.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.d3if0088.penghitungan.R
import org.d3if0088.penghitungan.databinding.TentangFragmentBinding

class TentangFragment : Fragment() {
    private lateinit var binding: TentangFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val actionBar = (requireActivity() as AppCompatActivity)
            .supportActionBar
        actionBar?.title = getString(R.string.judul_tentang)

        binding = TentangFragmentBinding.inflate(
            layoutInflater, container, false)
        return binding.root
    }
}