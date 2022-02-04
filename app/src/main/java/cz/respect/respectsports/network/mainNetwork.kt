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
import retrofit2.http.GET

data class NetworkMatchContainer(val matches: List<NetworkMatch>)

/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkMatch(
    val id: String,
    val date: String)

/**
 * Convert Network results to database objects
 */
fun NetworkMatchContainer.asDomainModel(): List<Match> {
    return matches.map {
        Match(
            id = it.id,
            date = it.date)
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
            date = it.date)
    }
}

interface MatchService {
    @GET("respect_table_tennis/www/98789789789079889789/test")
    suspend fun getMatches(): NetworkMatchContainer
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
        .baseUrl("http://10.0.2.2/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val matches = retrofit.create(MatchService::class.java)

}