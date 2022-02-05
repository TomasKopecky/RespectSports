package cz.respect.respectsports.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import cz.respect.respectsports.domain.User

@Dao
interface UserDao {
    @Query("select * from users")
    fun getLoggedUser(): LiveData<DatabaseUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoggedUser( user: DatabaseUser)
}

@Entity(tableName = "users")
data class DatabaseUser constructor(
    @PrimaryKey
    val id: String,
    val name: String,
    val token: String)

@Database(entities = [DatabaseUser::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract val userDao: UserDao
}

private lateinit var INSTANCE: UserDatabase

fun getUserDatabase(context: Context): UserDatabase {
    synchronized(UserDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                UserDatabase::class.java,
                "userDb").build()
        }
    }
    return INSTANCE
}


fun DatabaseUser.asDomainModel(): User {
    return User(id = id,name = name,token = token)
}
