package cz.respect.respectsports.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import cz.respect.respectsports.domain.DevByteVideo
import cz.respect.respectsports.domain.Match

@Dao
interface MatchDao {
    @Query("select * from matches")
    fun getMatches(): LiveData<List<DatabaseMatch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( matches: List<DatabaseMatch>)
}

@Database(entities = [DatabaseMatch::class], version = 1)
abstract class MatchesDatabase: RoomDatabase() {
    abstract val matchDao: MatchDao
}

private lateinit var INSTANCE: MatchesDatabase

fun getMainDatabase(context: Context): MatchesDatabase {
    synchronized(MatchesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                MatchesDatabase::class.java,
                "mainDb2").build()
        }
    }
    return INSTANCE
}

@Entity(tableName = "matches")
data class DatabaseMatch constructor(
    @PrimaryKey
    val id: String,
    val date: String)


/**
 * Map DatabaseVideos to domain entities
 */
fun List<DatabaseMatch>.asDomainModel(): List<Match> {
    return map {
        Match(
            id = it.id,
            date = it.date)
    }
}