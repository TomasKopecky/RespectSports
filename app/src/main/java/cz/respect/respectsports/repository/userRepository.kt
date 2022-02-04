package cz.respect.respectsports.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import cz.respect.respectsports.database.UserDatabase
import cz.respect.respectsports.database.asDomainModel
import cz.respect.respectsports.domain.Match
import cz.respect.respectsports.domain.User
import cz.respect.respectsports.network.UserNetwork
import cz.respect.respectsports.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val database: UserDatabase) {

    val users: LiveData<List<User>> = Transformations.map(database.userDao.getLoggedUser()) {
        it.asDomainModel()
    }

    suspend fun refreshUsers() {
        withContext(Dispatchers.IO) {
            val userList = UserNetwork.users.getUser()
            database.userDao.insertLoggedUser(userList.asDatabaseModel())
            Log.i("MY_INFO", "DATA WRITTEN TO THE DATABASE")
        }
    }
}