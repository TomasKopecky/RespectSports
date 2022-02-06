package cz.respect.respectsports.ui.logout

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.respect.respectsports.MainActivity
import cz.respect.respectsports.database.getMainDatabase
import cz.respect.respectsports.repository.UserRepository
import kotlinx.coroutines.launch
import java.lang.Exception


class LogoutViewModel (application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(getMainDatabase(application))
    val loggedUser = userRepository.user

    private val _message = MutableLiveData<String>()

    val message : MutableLiveData<String> = _message

    init {
        Log.i("MY_INFO", "LOGOUT INIT")
        logoutUserFromRepository()
    }

    private fun logoutUserFromRepository() {
        viewModelScope.launch {
            try {
                userRepository.deleteUser()
                message.value = "LOGOUT RUN"
            }
            catch (exception: Exception) {
                message.value = "LOGOUT EXCEPTION"
            }
        }
        }
}