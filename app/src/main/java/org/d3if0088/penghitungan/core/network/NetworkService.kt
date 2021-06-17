package org.d3if0088.penghitungan.core.network

import org.d3if0088.penghitungan.feature.laundry.model.UserEntity
import retrofit2.Call
import retrofit2.http.GET

interface NetworkService {
    companion object {
        const val USER = "users?page=1"
    }

    @GET(USER)
    fun getUsers(): Call<UserEntity>
}