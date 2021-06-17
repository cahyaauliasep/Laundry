package org.d3if0088.penghitungan.feature.laundry.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3if0088.penghitungan.core.network.ApiConfig
import org.d3if0088.penghitungan.core.network.NetworkService
import org.d3if0088.penghitungan.db.LaundryDao
import org.d3if0088.penghitungan.db.LaundryEntity
import org.d3if0088.penghitungan.feature.laundry.model.UserEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LaundryViewModel(private val db: LaundryDao) : ViewModel() {
    private val _users = MediatorLiveData<UserEntity>()
    private val _loading = MediatorLiveData<Boolean>()

    private val _data = db.getLastLaundry()

    val data: LiveData<List <LaundryEntity>>
        get() = _data

    fun hapusData(laundry : LaundryEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.deleteData(laundry) }
    }

    fun updateData(laundry : LaundryEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            db.updateData(laundry) }
    }

    val users: LiveData<UserEntity>
        get() = _users

    val loading: LiveData<Boolean>
        get() = _loading

    init {
        initNetwork()
    }

    private fun initNetwork() {
        val retrofit = ApiConfig.retrofitService()
        val api = retrofit.create(NetworkService::class.java)

        _loading.postValue(true)
        api.getUsers().enqueue(object : Callback<UserEntity> {
            override fun onFailure(call: Call<UserEntity>, t: Throwable) {
                _loading.postValue(false)
                _users.postValue(null)
                Log.d("TAG", "onFailure: ${t}")
            }

            override fun onResponse(call: Call<UserEntity>, response: Response<UserEntity>) {
//        Log.d("TAG", "onResponse: ${response.body()}")
                _loading.postValue(false)
                _users.postValue(response.body())
            }
        })
    }

    fun tambahLaundry(kategori: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val dataLaundry = LaundryEntity(
                    kategori = kategori
                )
                db.insert(dataLaundry)
            }
        }
    }
}