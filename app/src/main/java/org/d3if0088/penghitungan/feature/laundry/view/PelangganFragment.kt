package org.d3if0088.penghitungan.feature.laundry.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.d3if0088.penghitungan.R
import org.d3if0088.penghitungan.databinding.FragmentPelangganBinding
import org.d3if0088.penghitungan.db.LaundryDb
import org.d3if0088.penghitungan.feature.laundry.adapter.PelangganAdapter
import org.d3if0088.penghitungan.feature.laundry.viewmodel.LaundryViewModel
import org.d3if0088.penghitungan.feature.laundry.viewmodel.SimpanViewModelFactory

class PelangganFragment : Fragment() {
    private lateinit var binding: FragmentPelangganBinding
    private lateinit var pelangganAdapter: PelangganAdapter

    private val laundryViewModel: LaundryViewModel by lazy {
        val db = LaundryDb.getInstance(requireContext())
        val factory = SimpanViewModelFactory(db.dao)
        ViewModelProvider(this, factory).get(LaundryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val actionBar = (requireActivity() as AppCompatActivity)
            .supportActionBar
        actionBar?.title = getString(R.string.judul_pelanggan)

        binding = FragmentPelangganBinding.inflate(
            layoutInflater, container, false
        )
        laundryViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) binding.pelangganLoading.visibility = View.VISIBLE
            else {
                binding.pelangganLoading.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        })

        laundryViewModel.users.observe(viewLifecycleOwner, Observer { users ->
            pelangganAdapter.updateData(users.data)
            Log.d("TAG", "onResponse: ${users.data}")
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recyclerView) {
            pelangganAdapter = PelangganAdapter()
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = pelangganAdapter
            setHasFixedSize(true)
        }
    }
}