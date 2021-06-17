package org.d3if0088.penghitungan

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.d3if0088.penghitungan.data.KuantitasLaundry
import org.d3if0088.penghitungan.databinding.PenghitunganFragmentBinding
import org.d3if0088.penghitungan.db.LaundryDb
import org.d3if0088.penghitungan.feature.laundry.viewmodel.LaundryViewModel
import org.d3if0088.penghitungan.feature.laundry.viewmodel.SimpanViewModelFactory


const val KEY_JUMLAH = "jumlah_key"
const val KEY_JENIS = "jenis_key"
const val KEY_KATEGORI = "kategori_key"

class PenghitunganFragment : Fragment() {
    private lateinit var binding: PenghitunganFragmentBinding

    private var kuantitasLaundry: KuantitasLaundry = KuantitasLaundry.EMPTY
    private var jumlah = 0
    private var jenis = "-"
    private var kategori = "-"

    private val laundryViewModel: LaundryViewModel by lazy {
        val db = LaundryDb.getInstance(requireContext())
        val factory = SimpanViewModelFactory(db.dao)
        ViewModelProvider(this, factory).get(LaundryViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.pilihan_menu, menu)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_JUMLAH, jumlah)
        outState.putString(KEY_JENIS, jenis)
        if (kuantitasLaundry != KuantitasLaundry.EMPTY) {
            if (kuantitasLaundry == KuantitasLaundry.CUCI_SEDIKIT) {
                outState.putString(KEY_KATEGORI, "Sedikit")
            } else
                outState.putString(KEY_KATEGORI, "Banyak")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_tentang -> {
                findNavController().navigate(
                    R.id.action_penghitunganFragment_to_tentangFragment
                )
                return true
            }
            R.id.menu_kategori -> {
                findNavController().navigate(
                    R.id.action_penghitunganFragment_to_kategoriFragment
                )
                return true
            }
            R.id.menu_pelanggan -> {
                findNavController().navigate(
                    R.id.action_penghitunganFragment_to_pelangganFragment
                )
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.judul_laundry)
        binding = PenghitunganFragmentBinding.inflate(layoutInflater, container, false)

        if (savedInstanceState != null) {
            jumlah = savedInstanceState.getInt(KEY_JUMLAH, 0)
            jenis = savedInstanceState.getString(KEY_JENIS, "-")
            kategori = savedInstanceState.getString(KEY_KATEGORI, "-")

            binding.laundryTextView.text = getString(R.string.jumlah_x, jumlah)
            binding.buttonGroup.visibility = View.VISIBLE
            val kategoriCucian = if (jumlah > 50000) "Banyak" else "Sedikit"
            binding.kategoriTextView.text = getString(R.string.kategori_x, kategoriCucian)
            binding.jenisTextView.text = getString(R.string.jenis_x, jenis)
            binding.kategoriTextView.text = getString(R.string.kategori_x, kategori)

            kuantitasLaundry = when {
                savedInstanceState.getString(KEY_KATEGORI) == "Sedikit" -> {
                    KuantitasLaundry.CUCI_SEDIKIT
                }
                savedInstanceState.getString(KEY_KATEGORI) == "Banyak" -> {
                    KuantitasLaundry.CUCI_BANYAK
                }
                else -> {
                    KuantitasLaundry.EMPTY
                }
            }
        }

        binding.button.setOnClickListener { hitungLaundry() }
        binding.lengkapButton.setOnClickListener { view: View ->
            if (kuantitasLaundry == KuantitasLaundry.EMPTY) {
                Toast.makeText(context, "Data Invalid", Toast.LENGTH_LONG).show()
            } else {
                view.findNavController().navigate(
                    PenghitunganFragmentDirections.actionPenghitunganFragmentToLengkapFragment(
                        kuantitasLaundry,
                        binding.pelangganSpinner.getItemAtPosition(binding.pelangganSpinner.selectedItemPosition)
                            .toString(),
                        binding.kategoriSpinner.getItemAtPosition(binding.kategoriSpinner.selectedItemPosition)
                            .toString()
                    )
                )
            }
        }
        binding.resetButton.setOnClickListener { kosongkanLaundry() }
        binding.bagikanButton.setOnClickListener {
            if (kuantitasLaundry == KuantitasLaundry.EMPTY) {
                Toast.makeText(context, R.string.lengkap_invalid, Toast.LENGTH_LONG).show()
            } else {
                bagikanData()
            }
        }

        laundryViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) binding.loadingPelangganSpinner.visibility = View.VISIBLE
            else {
                binding.loadingPelangganSpinner.visibility = View.GONE
                binding.pelangganSpinner.visibility = View.VISIBLE
            }
        })

        laundryViewModel.users.observe(viewLifecycleOwner, Observer { users ->
            val dropDownUsers: ArrayList<String> = arrayListOf()
            if (users == null) {
                Toast.makeText(context, "Gagal Mengambil Data Pelanggan", Toast.LENGTH_SHORT).show()
                dropDownUsers.add("Anonymous")
            } else users.data.forEach {
                dropDownUsers.add("${it.first_name} ${it.last_name}")
            }
            val dropDownAdapter = ArrayAdapter(
                requireActivity().baseContext,
                android.R.layout.simple_spinner_item,
                dropDownUsers
            )
            dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.pelangganSpinner.adapter = dropDownAdapter
        })

        laundryViewModel.data.observe(viewLifecycleOwner, Observer { kategori ->
            val dropDownCategory: ArrayList<String> = arrayListOf()
            if (kategori.isEmpty()) {
                dropDownCategory.add("-")
            } else kategori.forEach {
                dropDownCategory.add(it.kategori)
            }
            val dropDownAdapter = ArrayAdapter(
                requireActivity().baseContext,
                android.R.layout.simple_spinner_item,
                dropDownCategory
            )
            dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.kategoriSpinner.adapter = dropDownAdapter
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun kosongkanLaundry() {
        binding.celanaEditText.text.clear()
        binding.bajuEditText.text.clear()
        binding.radioGroup.clearCheck()
        binding.buttonGroup.visibility = View.GONE

        binding.kategoriTextView.text = "-"
        binding.jenisTextView.text = "-"
        binding.laundryTextView.text = "0"

    }

    private fun bagikanData() {
        val selectedId = binding.radioGroup.checkedRadioButtonId
        val gender = if (selectedId == R.id.basahRadioButton)
            getString(R.string.basah)
        else
            getString(R.string.kering)
        val message = getString(
            R.string.bagikan_template,
            binding.celanaEditText.text,
            binding.bajuEditText.text,
            gender,
            binding.laundryTextView.text,
            binding.kategoriTextView.text
        )
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
        if (shareIntent.resolveActivity(
                requireActivity().packageManager
            ) != null
        ) {
            startActivity(shareIntent)
        }
    }

    private fun hitungLaundry() {
        val celana = binding.celanaEditText.text.toString()
        if (TextUtils.isEmpty(celana)) {
            Toast.makeText(context, R.string.celana_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val baju = binding.bajuEditText.text.toString()
        if (TextUtils.isEmpty(baju)) {
            Toast.makeText(context, R.string.baju_invalid, Toast.LENGTH_LONG).show()
            return
        }
        val selectedId = binding.radioGroup.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(context, R.string.jenis_cucian_invalid, Toast.LENGTH_LONG).show()
            return
        }

        val isBasah = selectedId == R.id.basahRadioButton
        val totalLaundry = baju.toInt() + celana.toInt()
        val jenisLaundry = getKategori(totalLaundry, isBasah)
        val kategoriCucian = if (totalLaundry > 50000) "Banyak" else "Sedikit"

        jumlah = totalLaundry
        jenis = jenisLaundry
        binding.buttonGroup.visibility = View.VISIBLE
        binding.laundryTextView.text = getString(R.string.jumlah_x, totalLaundry)
        binding.jenisTextView.text = getString(R.string.jenis_x, jenisLaundry)
        binding.kategoriTextView.text = getString(R.string.kategori_x, kategoriCucian)
    }

    private fun getKategori(laundry: Int, isBasah: Boolean): String {
        kuantitasLaundry =
            when {
                laundry > 50000 -> KuantitasLaundry.CUCI_BANYAK
                else -> KuantitasLaundry.CUCI_SEDIKIT
            }
        val stringRes = when {
            isBasah -> R.string.basah
            else -> R.string.kering
        }
        return getString(stringRes)
    }
}