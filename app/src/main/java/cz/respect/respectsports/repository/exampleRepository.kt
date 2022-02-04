package cz.respect.respectsports.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
//import cz.respect.respectsports.database.User
//import cz.respect.respectsports.database.UserDao
import cz.respect.respectsports.database.VideosDatabase
import cz.respect.respectsports.database.asDomainModel
import cz.respect.respectsports.domain.DevByteVideo
import cz.respect.respectsports.network.DevByteNetwork
import cz.respect.respectsports.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideosRepository(private val database: VideosDatabase) {

    val videos: LiveData<List<DevByteVideo>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val playlist = DevByteNetwork.devbytes.getPlaylist()
            database.videoDao.insertAll(playlist.asDatabaseModel())
        }
    }
}