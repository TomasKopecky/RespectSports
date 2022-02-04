package cz.respect.respectsports.ui.gallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import cz.respect.respectsports.database.getMainDatabase
import cz.respect.respectsports.repository.MatchesRepository
import kotlinx.coroutines.launch
import java.io.IOException

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text

    private val _message = MutableLiveData<String>()

    val message : MutableLiveData<String> = _message

    private val matchesRepository = MatchesRepository(getMainDatabase(application))

    val matchesList = matchesRepository.matches

    init {
        refreshMatchesFromRepository()
    }
    private fun refreshMatchesFromRepository() {
        viewModelScope.launch {
            try {
                matchesRepository.refreshMatches()
                //Log.i("MY_INFO", "SUCCESS" + matchesList)
                message.value = "DATA NAČTENA Z INTERNETU"
                //_eventNetworkError.value = false
                //_isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if(matchesList.value.isNullOrEmpty()) {
                    message.value = "CHYBA PŘIPOJENÍ K INTERNETU"
                    //Log.i("MY_INFO", "NETWORK CONNECTION AND DATABASE ERROR - NO DATA OBTAINED")
                }
                else {
                    message.value = "CHYBA PŘIPOJENÍ K INTERNETU - DATA NAČTENA Z DATABÁZE"
                    //Log.i("MY_INFO", "NETWORK CONNECTION ERROR - DATA OBTAINED FROM THE DATABASE")
                }
                //_eventNetworkError.value = true
            }
        }
    }
}