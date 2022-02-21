package cz.respect.respectsports.database

import androidx.lifecycle.LiveData
import androidx.room.*
import cz.respect.respectsports.domain.Player

@Dao
interface PlayerDao {
    @Query("select * from players")
    fun getPlayers(): LiveData<List<DatabasePlayer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( players: List<DatabasePlayer>)
}

@Entity(tableName = "players")
data class DatabasePlayer constructor(
    @PrimaryKey
    val id: String,
    val name: String)

fun List<DatabasePlayer>.asDomainModel(): List<Player> {
    return map {
        Player(
            id = it.id,
            name = it.name)
    }
}
