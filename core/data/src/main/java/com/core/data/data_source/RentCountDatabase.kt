package com.core.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.core.data.data_source.util.Converters

@Database(
    entities = [Blocks::class, Transactions::class, Categories::class, Rents::class, RentsTrack::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RentCountDatabase: RoomDatabase() {

    abstract val rentCountDao: RentCountDao

    companion object {
        const val DATABASE_NAME = "rent_count_db"
    }

}