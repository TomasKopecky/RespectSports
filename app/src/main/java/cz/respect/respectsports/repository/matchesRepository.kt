package cz.respect.respectsports.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import cz.respect.respectsports.database.MainDatabase
//import cz.respect.respectsports.database.VideosDatabase
import cz.respect.respectsports.database.asDomainModel
//import cz.respect.respectsports.domain.DevByteVideo
import cz.respect.respectsports.domain.Match
//import cz.respect.respectsports.network.DevByteNetwork
import cz.respect.respectsports.network.MatchNetwork
import cz.respect.respectsports.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MatchesRepository(private val database: MainDatabase, private val matchId: String?) {

    val matches: LiveData<List<Match>> = Transformations.map(database.matchDao.getMatches()) {
        Log.i("MY_INFO", "get all matches:")
        it.asDomainModel()
    }

    val match: LiveData<List<Match>> = Transformations.map(database.matchDao.getMatchDetail(matchId)) {
        Log.i("MY_INFO", "get one match with id: :" + matchId)
        it.asDomainModel()
    }


    suspend fun refreshMatches() {
        withContext(Dispatchers.IO) {
            val matches = MatchNetwork.matches.getMatches()
            database.matchDao.insertAll(matches.asDatabaseModel())
            Log.i("MY_INFO", "DATA OF ALL MATCHES WRITTEN TO THE DATABASE")
        }
    }

    suspend fun refreshMatchDetail(id:String) {
        withContext(Dispatchers.IO) {
            val matches = MatchNetwork.match.getMatchDetail(id)
            database.matchDao.insertAll(matches.asDatabaseModel())
            Log.i("MY_INFO", "DATA OF SINGLE MATCH WITH ID $id WRITTEN TO THE DATABASE")

        }
    }

    suspend fun insertNewMatch(match: Match) {
        withContext(Dispatchers.IO) {
            //val matches = MatchNetwork.match.getMatchDetail(id)
            MatchNetwork.match.insertNewMatch(match.homePlayer,match.visitorPlayer)
            val matches = MatchNetwork.matches.getMatches()
            database.matchDao.insertAll(matches.asDatabaseModel())
            Log.i("MY_INFO", "NEW MATCH WRITTEN TO DATABASE")

        }
    }
}