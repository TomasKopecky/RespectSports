package cz.respect.respectsports.ui.tableTennis

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.squareup.moshi.JsonDataException
import cz.respect.respectsports.database.getMainDatabase
import cz.respect.respectsports.domain.Match
import cz.respect.respectsports.repository.MatchesRepository
import kotlinx.coroutines.launch
import java.io.IOException


class TableTennisMatchViewModel(application: Application, id: String) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "Detail zápasu"
    }
    val text: LiveData<String> = _text

    private val _message = MutableLiveData<String>()

    val message : MutableLiveData<String> = _message

    val matchesRepository = MatchesRepository(getMainDatabase(application),id)

    val match = matchesRepository.match

    //val matchesRepository = MatchesRepository(getMainDatabase(application),matchId)

    //val match //= matchesRepository.match

    //private val matchesRepository = MatchesRepository(getMainDatabase(application),matchId)

    //val match = matchesRepository.match
    init {
        Log.i("MY_INFO", "FINALLY: " + id)

        refreshMatchDetailFromRepository(id)
    }
    fun refreshMatchDetailFromRepository(id:String) {




        //matchId?.let { Log.i("MY_INFO", it) }
        viewModelScope.launch {
            try {
                matchesRepository.refreshMatchDetail(id)
                //Log.i("MY_INFO", "MATCH DETAIL SUCCESS" + match.value)
                message.value = "DATA NAČTENA Z INTERNETU"
                //_eventNetworkError.value = false
                //_isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if(match.value.isNullOrEmpty()) {
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