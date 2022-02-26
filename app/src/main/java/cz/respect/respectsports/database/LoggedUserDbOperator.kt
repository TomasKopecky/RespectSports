package cz.respect.respectsports.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import cz.respect.respectsports.domain.User

@Dao
interface UserDao {
    @Query("SELECT * FROM loggedUser")
    fun getLoggedUser(): LiveData<DatabaseUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoggedUser( user: DatabaseUser)

    @Query("DELETE FROM loggedUser")
    fun deleteAll()
}

@Entity(tableName = "loggedUser")
data class DatabaseUser constructor(
    @PrimaryKey
    val id: String,
    val name: String,
    val username: String,
    val token: String)


fun DatabaseUser.asDomainModel(): User {
    return User(id = id,name = name,username=username,token = token)
}
