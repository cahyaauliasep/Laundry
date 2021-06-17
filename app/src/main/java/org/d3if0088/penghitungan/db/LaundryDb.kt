package org.d3if0088.penghitungan.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LaundryEntity::class], version = 1, exportSchema = false)
abstract class LaundryDb : RoomDatabase() {
    abstract val dao: LaundryDao

    companion object {
        @Volatile
        private var INSTANCE: LaundryDb? = null

        fun getInstance(context: Context): LaundryDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LaundryDb::class.java,
                        "laundry.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}