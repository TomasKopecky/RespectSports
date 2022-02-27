package cz.respect.respectsports.network

import android.util.Log
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.respect.respectsports.database.DatabaseUser
import cz.respect.respectsports.domain.User
import cz.respect.respectsports.ui.encryption.DataEncryption
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

data class NetworkUserContainer(val user: NetworkUser)

@JsonClass(generateAdapter = true)
data class NetworkUser(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val token: String)

/**
 * Convert Network results to domain objects
 */
fun NetworkUserContainer.asDomainModel(): User {
    val encryption = DataEncryption
    return User(user.id,user.name,user.username,user.email,encryption.encrypt(user.token))
}


/**
 * Convert Network results to database objects
 */
fun NetworkUserContainer.asDatabaseModel(): DatabaseUser {
    val encryption = DataEncryption
    return DatabaseUser(user.id,user.name,user.username,user.email,encryption.encrypt(user.token))
}
/**
 * Main entry point for network access
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface UserService {
    @FormUrlEncoded
    @POST(NetworkConstants.LOGIN_URL)
    suspend fun getUser(@Field("username") username: String, @Field("password") password: String): NetworkUserContainer

    @FormUrlEncoded
    @POST(NetworkConstants.LOGIN_URL)
    suspend fun checkToken(@Field("token") userToken: String): NetworkUserContainer
}

object UserNetwork {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(NetworkConstants.SERVER_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val user: UserService = retrofit.create(UserService::class.java)

}