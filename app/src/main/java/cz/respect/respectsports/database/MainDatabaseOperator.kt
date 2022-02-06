package cz.respect.respectsports.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseUser::class, DatabaseMatch::class], version = 1)
abstract class MainDatabase: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val matchDao: MatchDao
}

private lateinit var INSTANCE: MainDatabase

fun getMainDatabase(context: Context): MainDatabase {
    synchronized(MainDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                MainDatabase::class.java,
                "mainDb").build()
        }
    }
    return INSTANCE
}