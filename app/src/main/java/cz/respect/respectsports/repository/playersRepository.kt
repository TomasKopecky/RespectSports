package cz.respect.respectsports.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import cz.respect.respectsports.database.MainDatabase
import cz.respect.respectsports.database.asDomainModel
import cz.respect.respectsports.domain.Player
import cz.respect.respectsports.domain.User
import cz.respect.respectsports.network.PlayerNetwork
import cz.respect.respectsports.network.asDatabaseModel
import cz.respect.respectsports.ui.encryption.DataEncryption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayersRepository(private val database: MainDatabase) {

    val players: LiveData<List<Player>> = Transformations.map(database.playerDao.getPlayers()) {
        it.asDomainModel()
    }

    suspend fun refreshPlayers(token: String) {
        withContext(Dispatchers.IO) {
            val encryptor = DataEncryption
            val players = PlayerNetwork.players.getPlayers(encryptor.decrypt(token))
            database.playerDao.insertAll(players.asDatabaseModel())
            Log.i("MY_INFO", "DATA WRITTEN TO THE DATABASE")
        }
    }

    }