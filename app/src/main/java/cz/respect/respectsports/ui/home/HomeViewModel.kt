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

}