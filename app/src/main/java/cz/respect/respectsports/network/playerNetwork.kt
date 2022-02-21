package cz.respect.respectsports.network

import android.util.Log
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.respect.respectsports.database.DatabasePlayer
import cz.respect.respectsports.domain.Player
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

data class NetworkPlayerContainer(val players: List<NetworkPlayer>)

/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkPlayer(
    val id: String,
    val name: String)

/**
 * Convert Network results to database objects
 */
fun NetworkPlayerContainer.asDomainModel(): List<Player> {
    return players.map {
        Player(
            id = it.id,
            name = it.name)
    }
}


/**
 * Convert Network results to database objects
 */
fun NetworkPlayerContainer.asDatabaseModel(): List<DatabasePlayer> {
    Log.i("MY_INFO", "DATA OBTAINED FROM REST API")
    return players.map {
        DatabasePlayer(
            id = it.id,
            name = it.name)
    }
}

interface PlayerService {
    @GET(NetworkConstants.GET_ALL_PLAYERS_URL)
    suspend fun getPlayers(): NetworkPlayerContainer

}

/**
 * Main entry point for network access. Call like `DevByteNetwork.devbytes.getPlaylist()`
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object PlayerNetwork {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.SERVER_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val players: PlayerService = retrofit.create(PlayerService::class.java)

}