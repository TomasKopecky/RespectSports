package cz.respect.respectsports.ui.tableTennis

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.squareup.moshi.JsonDataException
import cz.respect.respectsports.database.getMainDatabase
import cz.respect.respectsports.repository.MatchesRepository
import cz.respect.respectsports.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.IOException


class TableTennisMatchDetailViewModel(application: Application, matchId: String) :
    AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "Detail zápasu"
    }
    val text: LiveData<String> = _text

    private val userRepository = UserRepository(getMainDatabase(application))

    val loggedUser = userRepository.user

    private val _message = MutableLiveData<String>()

    val message: MutableLiveData<String> = _message

    private val matchesRepository = MatchesRepository(getMainDatabase(application), matchId)

    val match = matchesRepository.match

    private val _tokenError = MutableLiveData<Boolean>()

    val tokenError: LiveData<Boolean> = _tokenError

    init {
        getLoggedUserFromDatabase()
    }

    private fun getLoggedUserFromDatabase() {
        viewModelScope.launch {
            try {
                userRepository.getLoggedUser()
                //Log.i("MY_INFO", "LOGGED USER OBTAINED FROM DB")
            } catch (dataStructureError: JsonDataException) {
                _tokenError.value = true
                //Log.i("MY_INFO", "LOGGED USER OBTAINING FROM DB ERROR")
            }
        }
    }

    fun refreshMatchDetailFromRepository(token: String, id: String) {
        viewModelScope.launch {
            try {
                //Log.i("MY_INFO", "LAUNCHING MATCH DETAIL");
                matchesRepository.refreshMatchDetail(token, id)
                //Log.i("MY_INFO", "MATCH DETAIL SUCCESS" + match.value)

            } catch (networkError: IOException) {
                message.value = "Chyba při stahování detailu zápasu z internetu - offline režim"
                if (match.value.isNullOrEmpty()) {
                    //Log.i("MY_INFO", "NETWORK CONNECTION AND DATABASE ERROR - NO DATA OBTAINED")
                } else {
                    //Log.i("MY_INFO", "NETWORK CONNECTION ERROR - DATA OBTAINED FROM THE DATABASE")
                }
            } catch (serverError: retrofit2.HttpException) {
                //Log.i("MY_INFO", "SERVER ERROR: " + serverError.message())
                if (serverError.code() == 401) {
                    _tokenError.value = true
                } else {
                    message.value = "Chyba při stahování detailu zápasu - server vrátil chybu"
                }

            } catch (dataStructureError: JsonDataException) {
                message.value = "Chyba při stahování detailu zápasu - server odpověděl chybně"
                //dataStructureError.message?.let { Log.i("MY_INFO", "JSON ERROR: " + it) }
            }
        }
    }
}