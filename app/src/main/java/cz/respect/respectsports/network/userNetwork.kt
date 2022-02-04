package cz.respect.respectsports.network

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.respect.respectsports.database.DatabaseUser
import cz.respect.respectsports.domain.User
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

data class NetworkUserContainer(val users: List<NetworkUser>)

interface UserService {
    @GET("respect_table_tennis/www/98789789789079889789/login")
    suspend fun getUser(): NetworkUserContainer
}

@JsonClass(generateAdapter = true)
data class NetworkUser(
    val id: String,
    val name: String,
    val token: String)

/**
 * Convert Network results to database objects
 */
fun NetworkUserContainer.asDomainModel(): List<User> {
    return users.map {
        User(
            id = it.id,
            name = it.name,
            token = it.token)
    }
}


/**
 * Convert Network results to database objects
 */
fun NetworkUserContainer.asDatabaseModel(): List<DatabaseUser> {
    return users.map {
        DatabaseUser(
            id = it.id,
            name = it.name,
            token = it.token)
    }
}
/**
 * Main entry point for network access. Call like `DevByteNetwork.devbytes.getPlaylist()`
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object UserNetwork {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val users = retrofit.create(UserService::class.java)

}