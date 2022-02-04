package cz.respect.respectsports.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import cz.respect.respectsports.database.MatchesDatabase
import cz.respect.respectsports.database.VideosDatabase
import cz.respect.respectsports.database.asDomainModel
import cz.respect.respectsports.domain.DevByteVideo
import cz.respect.respectsports.domain.Match
import cz.respect.respectsports.network.DevByteNetwork
import cz.respect.respectsports.network.MatchNetwork
import cz.respect.respectsports.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MatchesRepository(private val database: MatchesDatabase) {

    val matches: LiveData<List<Match>> = Transformations.map(database.matchDao.getMatches()) {
        it.asDomainModel()
    }

    suspend fun refreshMatches() {
        withContext(Dispatchers.IO) {
            val playlist = MatchNetwork.matches.getMatches()
            database.matchDao.insertAll(playlist.asDatabaseModel())
            Log.i("MY_INFO", "DATA WRITTEN TO THE DATABASE")
        }
    }
}