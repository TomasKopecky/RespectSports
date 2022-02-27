package cz.respect.respectsports.ui.tableTennis

import android.R
import android.app.Application
import android.util.Log
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.*
import com.squareup.moshi.JsonDataException
import cz.respect.respectsports.database.getMainDatabase
import cz.respect.respectsports.domain.Match
import cz.respect.respectsports.repository.MatchesRepository
import cz.respect.respectsports.repository.PlayersRepository
import cz.respect.respectsports.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.IOException

class TableTennisNewMatchViewModel(application: Application) : AndroidViewModel(application) {



    private val _text = MutableLiveData<String>().apply {
        value = "Vložení zápasu"
    }

    val text: LiveData<String> = _text

    val homeCounter = MutableLiveData<Int>()

    val visitorCounter = MutableLiveData<Int>()

    val matchDate = MutableLiveData<String>()

    val homePlayerPositionChange = MutableLiveData<Boolean>()

    val visitorPlayerPositionChange = MutableLiveData<Boolean>()

    val homePlayerPosition = MutableLiveData<Int>()

    val visitorPlayerPosition = MutableLiveData<Int>()

    private val _message = MutableLiveData<String>()

    val message : MutableLiveData<String> = _message

    private val userRepository = UserRepository(getMainDatabase(application))

    val loggedUser = userRepository.user

    val playerRepository = PlayersRepository(getMainDatabase(application))

    val players = playerRepository.players

    val matchesRepository = MatchesRepository(getMainDatabase(application),null)

    val matches = matchesRepository.matches

    private val _tokenError = MutableLiveData<Boolean>()

    val tokenError: LiveData<Boolean> = _tokenError

    private val _insertSuccess = MutableLiveData<Boolean>()

    val insertSuccess: LiveData<Boolean> = _insertSuccess

    //val matchesRepository = MatchesRepository(getMainDatabase(application),matchId)

    //val match //= matchesRepository.match

    //private val matchesRepository = MatchesRepository(getMainDatabase(application),matchId)

    //val match = matchesRepository.match

    fun resetValues() {
        homeCounter.value = 0
        visitorCounter.value = 0
        matchDate.value = ""
        homePlayerPosition.value = 0
        visitorPlayerPosition.value = 0
        homePlayerPositionChange.value = false
        visitorPlayerPositionChange.value = false
    }

    init {
        resetValues()
        getLoggedUserFromDatabase()
        //refreshPlayersFromRepository(userToken)
    }

    private fun getLoggedUserFromDatabase() {
        viewModelScope.launch {
            try {
                userRepository.getLoggedUser()
                Log.i("MY_INFO", "LOGGED USER OBTAINED FROM DB")
            }
            catch (dataStructureError: JsonDataException) {
                _tokenError.value = true
                Log.i("MY_INFO", "LOGGED USER OBTAINING FROM DB ERROR")
            }
        }
    }

    fun refreshPlayersFromRepository(token: String) {
        //matchId?.let { Log.i("MY_INFO", it) }
        viewModelScope.launch {
            try {
                playerRepository.refreshPlayers(token)
                Log.i("MY_INFO", "PLAYERS OBTAINED: " + players.value.toString())
                //message.value = "DATA NAČTENA Z INTERNETU"

            } catch (networkError: IOException) {
                message.value = "Chyba při stahování údajů z internetu - offline režim"
                // Show a Toast error message and hide the progress bar.
                if(players.value.isNullOrEmpty()) {
                    //message.value = "CHYBA PŘIPOJENÍ K INTERNETU"
                    Log.i("MY_INFO", "NETWORK CONNECTION AND DATABASE ERROR - NO DATA OBTAINED")
                }
                else {
                    //message.value = "CHYBA PŘIPOJENÍ K INTERNETU - DATA NAČTENA Z DATABÁZE"
                    Log.i("MY_INFO", "NETWORK CONNECTION ERROR - DATA OBTAINED FROM THE DATABASE")
                }
                //_eventNetworkError.value = true
            }
            catch (serverError: retrofit2.HttpException) {
                Log.i("MY_INFO", serverError.message())
                if (serverError.code() == 401) {
                    _tokenError.value = true
                }
                else {
                    message.value = "Chyba při stahování údajů - server vrátil chybu"
                }
            }
            catch (dataStructureError: JsonDataException) {
                //message.value = "Chyba při stahování zápasů - server odpověděl chybně"
                dataStructureError.message?.let { Log.i("MY_INFO", it) }
            }
        }
    }

    fun insertMatch(userToken: String, match: Match) {
        insertMatchRepository(userToken, match)
    }

    fun insertMatchRepository(token: String, match: Match) {
        Log.i("MY_INFO","LAUNCHING")
        //matchId?.let { Log.i("MY_INFO", it) }
        viewModelScope.launch {
            try {
                matchesRepository.insertNewMatch(token, match)
                //message.value = "ZÁPAS ÚSPĚŠNĚ ODESLÁN DO API"
                _insertSuccess.value = true

            } catch (networkError: IOException) {
                message.value = "Chyba při vkládání zápasu - připojte se k internetu"
                //_tokenError.value = true
                // Show a Toast error message and hide the progress bar.
                if(matches.value.isNullOrEmpty()) {
                    //message.value = "CHYBA PŘIPOJENÍ K INTERNETU"
                    Log.i("MY_INFO", "NETWORK CONNECTION AND DATABASE ERROR - NO DATA OBTAINED")
                }
                else {
                    //message.value = "CHYBA PŘIPOJENÍ K INTERNETU - DATA NAČTENA Z DATABÁZE"
                    Log.i("MY_INFO", "NETWORK CONNECTION ERROR - DATA OBTAINED FROM THE DATABASE")
                }
            }
            catch (serverError: retrofit2.HttpException) {
                //_tokenError.value = true
                //message.value = "Chyba při vkládání zápasu - server vrátil chybu"
                //message.value = "Chyba při vkládání zápasu - server vrátil chybu"
                Log.i("MY_INFO", "SERVER_ERROR: " + serverError.message())
                Log.i("MY_INFO", "SERVER_ERROR: " + serverError.response())
                if (serverError.code() == 401) {
                    _tokenError.value = true
                }
                else {
                    message.value = "Chyba při vkládání zápasu - server vrátil chybu"
                }
            }
            catch (dataStructureError: JsonDataException) {
                //_tokenError.value = true
                message.value = "Chyba při vkládání zápasu - server odpověděl chybně"
                dataStructureError.message?.let { Log.i("MY_INFO", "JSON ERROR: " + it) }
            }
        }
    }

    fun onHomeIncrement() {
        homeCounter.value = (homeCounter.value)?.plus(1)
    }

    fun onHomeDecrement() {
        if (homeCounter.value!! > 0) {
            homeCounter.value = (homeCounter.value)?.minus(1)
        }
    }

    fun onVisitorIncrement() {
        visitorCounter.value = (visitorCounter.value)?.plus(1)
    }

    fun onVisitorDecrement() {
        if (visitorCounter.value!! > 0) {
            visitorCounter.value = (visitorCounter.value)?.minus(1)
        }
    }

    fun homeSpinnerPlayerChanged() {
        homePlayerPositionChange.value = true
    }

    fun visitorSpinnerPlayerChanged() {
        visitorPlayerPositionChange.value = true
    }
}