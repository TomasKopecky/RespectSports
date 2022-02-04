package cz.respect.respectsports.ui.home

//import cz.respect.respectsports.database.User
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import cz.respect.respectsports.database.getMainDatabase
import cz.respect.respectsports.repository.MatchesRepository
import kotlinx.coroutines.launch
import java.io.IOException


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    /*
    private val videosRepository = VideosRepository(getDatabase(application))

    val playlist = videosRepository.videos

    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                Log.i("STATUS", "SUCCESS")
                videosRepository.refreshVideos()
                //_eventNetworkError.value = false
                //_isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if(playlist.value.isNullOrEmpty()) {
                    Log.i("STATUS", "ERROR")
                }
                    //_eventNetworkError.value = true
            }
        }
    }

     */
/*
    init {
        insertData()
    }

    fun insertData(){
        GlobalScope.launch {
            //val user = User(54,"fdaslfj","slakdf")
            getDatabase(getApplication()).userDao().getAll()
        }
    }

 */
}