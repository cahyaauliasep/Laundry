package org.d3if0088.penghitungan.feature.laundry.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if0088.penghitungan.db.LaundryDao

class SimpanViewModelFactory(
    private val db: LaundryDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LaundryViewModel::class.java)) {
            return LaundryViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}