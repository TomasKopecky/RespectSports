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

class TableTennisMatchesViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "Zde bude seznam zápasů uživatele"
    }

    private val _message = MutableLiveData<String>()

    val message: MutableLiveData<String> = _message

    private val userRepository = UserRepository(getMainDatabase(application))

    val loggedUser = userRepository.user

    private val _tokenError = MutableLiveData<Boolean>()

    val tokenError: LiveData<Boolean> = _tokenError

    private val matchesRepository = MatchesRepository(getMainDatabase(application), null)

    val matchesList = matchesRepository.matches

    init {
        getLoggedUserFromDatabase()
    }

    private fun getLoggedUserFromDatabase() {
        viewModelScope.launch {
            try {
                userRepository.getLoggedUser()
                Log.i("MY_INFO", "LOGGED USER OBTAINED FROM DB")
            } catch (dataStructureError: JsonDataException) {
                _tokenError.value = true
                Log.i("MY_INFO", "LOGGED USER OBTAINING FROM DB ERROR")
            }
        }
    }

    fun refreshMatchesFromRepository(userToken: String) {
        viewModelScope.launch {
            try {
                matchesRepository.refreshMatches(userToken)
                //Log.i("MY_INFO", "SUCCESS" + matchesList)

            } catch (networkError: IOException) {
                message.value = "Chyba při stahování zápasů z internetu - offline režim"
                if (matchesList.value.isNullOrEmpty()) {
                    Log.i("MY_INFO", "NETWORK CONNECTION AND DATABASE ERROR - NO DATA OBTAINED")
                } else {
                    Log.i("MY_INFO", "NETWORK CONNECTION ERROR - DATA OBTAINED FROM THE DATABASE")
                }
            } catch (serverError: retrofit2.HttpException) {
                Log.i("MY_INFO", serverError.message())
                Log.i("MY_INFO", "RESPONSE: " + serverError.response().toString())
                if (serverError.code() == 401) {
                    _tokenError.value = true
                } else {
                    message.value = "Chyba při stahování zápasů z internetu"
                }
            } catch (dataStructureError: JsonDataException) {
                message.value = "Chyba při stahování zápasů - server odpověděl chybně"
                dataStructureError.message?.let { Log.i("MY_INFO", "JSON ERROR: " + it) }
            }
        }
    }
}