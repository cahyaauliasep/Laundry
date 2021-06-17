package org.d3if0088.penghitungan.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laundry")
data class LaundryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var kategori: String
)