package mx.tec.laventana.utility

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import androidx.room.TypeConverters
import mx.tec.laventana.dao.LocationDao
import mx.tec.laventana.model.Location

@Database(entities = [Location::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao

    companion object { // static instance | singleton
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "LaVentana" // DB name
                ).build()
            }
            return INSTANCE as AppDatabase
        }
    }
}