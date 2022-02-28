package cz.respect.respectsports.database

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.respect.respectsports.domain.Match
import java.text.SimpleDateFormat
import java.util.*

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches ORDER BY date DESC, id ASC")
    fun getMatches(): LiveData<List<DatabaseMatch>>

    @Query("SELECT * FROM matches WHERE id=:id")
    fun getMatchDetail(id: String?): LiveData<List<DatabaseMatch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(matches: List<DatabaseMatch>)
}

@Entity(tableName = "matches")
data class DatabaseMatch constructor(
    @PrimaryKey
    val id: String,
    val date: Long,
    val homePlayerId: String,
    val homePlayerName: String?,
    val homePlayerUsername: String?,
    val visitorPlayerId: String,
    val visitorPlayerName: String?,
    val visitorPlayerUsername: String?,
    val result: String)

/**
 * Map Database matches to domain entities
 */
fun List<DatabaseMatch>.asDomainModel(): List<Match> {

    return map {
        Match(
            id = it.id,
            date = it.date,
            homePlayerId = it.homePlayerId,
            homePlayerName = it.homePlayerName,
            homePlayerUsername = it.homePlayerUsername,
            visitorPlayerId = it.visitorPlayerId,
            visitorPlayerName = it.visitorPlayerName,
            visitorPlayerUsername = it.visitorPlayerUsername,
            result = it.result)
    }
}
