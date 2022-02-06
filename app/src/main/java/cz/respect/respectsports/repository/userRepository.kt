package cz.respect.respectsports.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import cz.respect.respectsports.database.MainDatabase
import cz.respect.respectsports.database.asDomainModel
import cz.respect.respectsports.domain.User
import cz.respect.respectsports.network.UserNetwork
import cz.respect.respectsports.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val database: MainDatabase) {

    val user: LiveData<User> = Transformations.map(database.userDao.getLoggedUser()) {
        it.asDomainModel()
    }

    suspend fun refreshUsers(username: String, password: String) {
        withContext(Dispatchers.IO) {
            database.userDao.deleteAll()
            val user = UserNetwork.user.getUser(username, password)
            database.userDao.insertLoggedUser(user.asDatabaseModel())
            Log.i("MY_INFO", "DATA WRITTEN TO THE DATABASE")
        }
    }
}