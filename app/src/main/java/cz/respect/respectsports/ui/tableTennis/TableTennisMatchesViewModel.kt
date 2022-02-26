package cz.respect.respectsports.ui.tableTennis

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.squareup.moshi.JsonDataException
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.database.getMainDatabase
import cz.respect.respectsports.repository.MatchesRepository
import cz.respect.respectsports.repository.UserRepository
import cz.respect.respectsports.ui.login.LoggedInUserView
import cz.respect.respectsports.ui.login.LoginResult
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class TableTennisMatchesViewModel(application: Application, userId: String, userToken: String) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "Zde bude seznam zápasů uživatele"
    }
    private val userToken = userToken

    val text: LiveData<String> = _text

    private val _message = MutableLiveData<String>()

    val message : MutableLiveData<String> = _message

    private val userRepository = UserRepository(getMainDatabase(application))

    val loggedUser = userRepository.user

    private val _tokenError = MutableLiveData<Boolean>()

    val tokenError: LiveData<Boolean> = _tokenError

    private val matchesRepository = MatchesRepository(getMainDatabase(application),null)

    val matchesList = matchesRepository.matches

    init {
        Log.i("MY_INFO", "USER_ID: " + userId + ", USER_TOKEN: " + userToken)
        refreshMatchesFromRepository(userToken)
        //checkValidToken(userToken)
        //refreshMatchesFromRepository(userId,userToken)
    }


    private fun refreshMatchesFromRepository(userToken: String) {
        viewModelScope.launch {
            try {
                matchesRepository.refreshMatches(userToken)
                //Log.i("MY_INFO", "SUCCESS" + matchesList)
                message.value = "DATA NAČTENA Z INTERNETU"
                //_eventNetworkError.value = false
                //_isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                _tokenError.value = true
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
                _tokenError.value = true
                message.value = "Chyba při stahování zápasů - server vrátil chybu"
                Log.i("MY_INFO", serverError.message())
                Log.i("MY_INFO", "RESPONSE: " + serverError.response().toString())
            }
            catch (dataStructureError: JsonDataException) {
                _tokenError.value = true
                message.value = "Chyba při stahování zápasů - server odpověděl chybně"
                dataStructureError.message?.let { Log.i("MY_INFO", "JSON ERROR: " + it) }
            }
        }
    }
}