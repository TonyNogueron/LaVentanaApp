package mx.tec.laventana.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import mx.tec.laventana.model.Location

@Dao
interface LocationDao {
    @Query("SELECT * FROM Location")
    fun getLocations(): List<Location>

    @Insert
    fun registerLocation(location: Location)
}