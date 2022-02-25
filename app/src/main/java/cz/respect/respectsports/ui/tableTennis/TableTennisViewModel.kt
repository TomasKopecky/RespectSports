package cz.respect.respectsports.ui.tableTennis

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.squareup.moshi.JsonDataException
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.database.getMainDatabase
import cz.respect.respectsports.repository.MatchesRepository
import kotlinx.coroutines.launch
import java.io.IOException

class TableTennisViewModel(application: Application, userId: String, userToken: String) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "Zde bude seznam zápasů uživatele"
    }
    val text: LiveData<String> = _text

    private val _message = MutableLiveData<String>()

    val message : MutableLiveData<String> = _message

    private val matchesRepository = MatchesRepository(getMainDatabase(application),null)

    val matchesList = matchesRepository.matches

    init {
        Log.i("MY_INFO", "USER_ID: " + userId + ", USER_TOKEN: " + userToken)
        refreshMatchesFromRepository(userId,userToken)
    }
    private fun refreshMatchesFromRepository(userId: String, userToken: String) {
        viewModelScope.launch {
            try {
                matchesRepository.refreshMatches(userId, userToken)
                //Log.i("MY_INFO", "SUCCESS" + matchesList)
                message.value = "DATA NAČTENA Z INTERNETU"
                //_eventNetworkError.value = false
                //_isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if(matchesList.value.isNullOrEmpty()) {
                    message.value = "CHYBA PŘIPOJENÍ K INTERNETU"
                    Log.i("MY_INFO", "NETWORK CONNECTION AND DATABASE ERROR - NO DATA OBTAINED")
                }
                else {
                    message.value = "CHYBA PŘIPOJENÍ K INTERNETU - DATA NAČTENA Z DATABÁZE"
                    Log.i("MY_INFO", "NETWORK CONNECTION ERROR - DATA OBTAINED FROM THE DATABASE")
                }
                //_eventNetworkError.value = true
            }
            catch (serverError: retrofit2.HttpException) {
                message.value = "Chyba při stahování zápasů - server vrátil chybu"
                Log.i("MY_INFO", serverError.message())
            }
            catch (dataStructureError: JsonDataException) {
                message.value = "Chyba při stahování zápasů - server odpověděl chybně"
                dataStructureError.message?.let { Log.i("MY_INFO", it) }
            }
        }
    }
}