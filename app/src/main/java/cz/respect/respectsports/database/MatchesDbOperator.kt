package cz.respect.respectsports.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import cz.respect.respectsports.domain.Match

@Dao
interface MatchDao {
    @Query("select * from matches")
    fun getMatches(): LiveData<List<DatabaseMatch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( matches: List<DatabaseMatch>)
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
