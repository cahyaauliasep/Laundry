package org.d3if0088.penghitungan.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LaundryDao {
    @Insert
    fun insert(laundry: LaundryEntity)

    @Query("SELECT * FROM laundry ORDER BY id DESC")
    fun getLastLaundry(): LiveData<List <LaundryEntity>>

    @Delete
    fun deleteData(laundry: LaundryEntity)

    @Update
    fun updateData(laundry: LaundryEntity)
}