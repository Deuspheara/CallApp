package fr.deuspheara.callapp.data.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import fr.deuspheara.callapp.data.database.dao.UserDao
import fr.deuspheara.callapp.data.database.model.LocalUserEntity

/**
 * _CallApp_
 *
 * fr.deuspheara.callapp.data.database.room.CallAppDatabase
 *
 * ### Information
 * - __Author__ Deuspheara
 *
 * ### Description
 * Room definition of [CallAppDatabase]
 *
 *
 */
@Database(
    entities = [LocalUserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class CallAppDatabase : RoomDatabase() {
    internal companion object {
        const val DATABASE_NAME = "call_app.db"
    }

    abstract val userDao: UserDao
}

class StringListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}
