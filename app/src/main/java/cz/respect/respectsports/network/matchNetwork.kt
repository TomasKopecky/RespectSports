package cz.respect.respectsports.network

import android.util.Log
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.respect.respectsports.database.DatabaseMatch
import cz.respect.respectsports.database.DatabaseUser
//import cz.respect.respectsports.database.DatabaseVideo
//import cz.respect.respectsports.domain.DevByteVideo
import cz.respect.respectsports.domain.Match
//import cz.respect.respectsports.domain.userDomain
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.text.SimpleDateFormat
import java.util.*

data class NetworkMatchContainer(val matches: List<NetworkMatch>)

/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkMatch(
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
 * Convert Network results to database objects
 */
fun NetworkMatchContainer.asDomainModel(): List<Match> {
    return matches.map {
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


/**
 * Convert Network results to database objects
 */
fun NetworkMatchContainer.asDatabaseModel(): List<DatabaseMatch> {
    Log.i("MY_INFO", "DATA OBTAINED FROM REST API")
    return matches.map {
        DatabaseMatch(
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

interface MatchService {
    @FormUrlEncoded
    @POST(NetworkConstants.MATCHES_URL)
    suspend fun getMatches(@Field("token") token: String): NetworkMatchContainer

    @FormUrlEncoded
    @POST(NetworkConstants.MATCH_DETAIL_URL/*+"/{id}"*/)
    suspend fun getMatchDetail(@Field("token") token: String, @Field("matchId") matchId: String): NetworkMatchContainer

    @FormUrlEncoded
    @POST(NetworkConstants.MATCH_INSERT_URL)
    suspend fun insertNewMatch(@Field("token") token: String, @Field("homePlayerId") homePLayerId: String, @Field("visitorPlayerId") visitorPlayerId: String,  @Field("result") matchResult: String,  @Field("date") matchDate: Long): NetworkMatchContainer
}

/**
 * Main entry point for network access. Call like `DevByteNetwork.devbytes.getPlaylist()`
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object MatchNetwork {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.SERVER_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val matches: MatchService = retrofit.create(MatchService::class.java)
    val match: MatchService = retrofit.create(MatchService::class.java)

}