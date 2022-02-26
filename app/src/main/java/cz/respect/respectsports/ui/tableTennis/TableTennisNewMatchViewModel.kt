package cz.respect.respectsports.ui.tableTennis

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.squareup.moshi.JsonDataException
import cz.respect.respectsports.database.getMainDatabase
import cz.respect.respectsports.domain.Match
import cz.respect.respectsports.repository.MatchesRepository
import cz.respect.respectsports.repository.PlayersRepository
import kotlinx.coroutines.launch
import java.io.IOException

class TableTennisNewMatchViewModel(application: Application, userId: String, userToken: String) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "Vložení zápasu"
    }

    private val userToken = userToken

    val text: LiveData<String> = _text

    private val _message = MutableLiveData<String>()

    val message : MutableLiveData<String> = _message

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
    init {
        refreshPlayersFromRepository(userToken)
    }

    fun refreshPlayersFromRepository(token: String) {
        //matchId?.let { Log.i("MY_INFO", it) }
        viewModelScope.launch {
            try {
                playerRepository.refreshPlayers(token)
                Log.i("MY_INFO", "PLAYERS OBTAINED: " + players.value.toString())
                message.value = "DATA NAČTENA Z INTERNETU"

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if(players.value.isNullOrEmpty()) {
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

    fun insertMatch(match: Match) {
        insertMatchRepository(userToken, match)
    }

    fun insertMatchRepository(token: String, match: Match) {
        Log.i("MY_INFO","LAUNCHING")
        //matchId?.let { Log.i("MY_INFO", it) }
        viewModelScope.launch {
            try {
                matchesRepository.insertNewMatch(token, match)
                Log.i("MY_INFO", "PLAYERS OBTAINED: " + matches.value.toString())
                message.value = "ZÁPAS ÚSPĚŠNĚ ODESLÁN DO API"
                _insertSuccess.value = true

            } catch (networkError: IOException) {
                //_tokenError.value = true
                // Show a Toast error message and hide the progress bar.
                if(matches.value.isNullOrEmpty()) {
                    _insertSuccess.value = false
                    message.value = "CHYBA PŘIPOJENÍ K INTERNETU"
                    Log.i("MY_INFO", "NETWORK CONNECTION AND DATABASE ERROR - NO DATA OBTAINED")
                }
                else {
                    message.value = "CHYBA PŘIPOJENÍ K INTERNETU - DATA NAČTENA Z DATABÁZE"
                    Log.i("MY_INFO", "NETWORK CONNECTION ERROR - DATA OBTAINED FROM THE DATABASE")
                }
            }
            catch (serverError: retrofit2.HttpException) {
                _insertSuccess.value = false
                //_tokenError.value = true
                message.value = "Chyba při vkládání zápasu - server vrátil chybu"
                Log.i("MY_INFO", "SERVER_ERROR: " + serverError.message())
                Log.i("MY_INFO", "SERVER_ERROR: " + serverError.response())
            }
            catch (dataStructureError: JsonDataException) {
                _insertSuccess.value = false
                //_tokenError.value = true
                message.value = "Chyba při vkládání zápasu - server odpověděl chybně"
                dataStructureError.message?.let { Log.i("MY_INFO", "JSON ERROR: " + it) }
            }
        }
    }
}