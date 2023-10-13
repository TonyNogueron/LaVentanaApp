package mx.tec.laventana.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mx.tec.laventana.model.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM Location")
    fun getLocations(): MutableList<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun registerLocation(location: Location)
}