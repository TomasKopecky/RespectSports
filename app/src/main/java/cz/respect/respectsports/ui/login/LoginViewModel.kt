package cz.respect.respectsports.ui.login

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import com.squareup.moshi.JsonDataException
import cz.respect.respectsports.data.LoginRepository
import cz.respect.respectsports.data.Result

import cz.respect.respectsports.R
import cz.respect.respectsports.database.getMainDatabase
import cz.respect.respectsports.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel (private val loginRepository: LoginRepository, application: Application) : AndroidViewModel(application) {

    private val _message = MutableLiveData<String>()

    val message : MutableLiveData<String> = _message

    private val userRepository = UserRepository(getMainDatabase(application))
    val loggedUser = userRepository.user

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private fun refreshUserFromRepository(username: String, password: String) {
        viewModelScope.launch {
            try {
                userRepository.refreshUsers(username, password)
                //message.value = "LOGIN OK"
                _loginResult.value = LoginResult(success = LoggedInUserView(displayName = username))
                Log.i("MY_INFO", "SUCCESS3")
                //message.value = "DATA NAČTENA Z INTERNETU"
                //_eventNetworkError.value = false
                //_isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                Log.i("MY_INFO", "ERROR1")
                message.value = "Chyba při přihlašování - server nedostupný"
                // Show a Toast error message and hide the progress bar.
                if (loggedUser.value?.id.isNullOrEmpty()) {
                    //message.value = "CHYBA PŘIPOJENÍ K INTERNETU"
                    Log.i("MY_INFO", "NETWORK CONNECTION AND DATABASE ERROR - NO DATA OBTAINED: " + networkError.message)
                } else {
                    //message.value = "CHYBA PŘIPOJENÍ K INTERNETU - DATA NAČTENA Z DATABÁZE"
                    Log.i("MY_INFO", "NETWORK CONNECTION ERROR - DATA OBTAINED FROM THE DATABASE" +  networkError.message)
                }
                //_eventNetworkError.value = true
            }

            catch (serverError: HttpException) {
                Log.i("MY_INFO", "NETWORK HTTP CONNECTION ERROR - NO DATA OBTAINED: " + serverError.message)
                message.value = "Neplatné přihlašovací údaje"
            }

            catch (dataStructureError: JsonDataException) {
                Log.i("MY_INFO", "JSON PARSING ERROR " + dataStructureError.message)
                message.value = "Chyba při přihlašování - server odpověděl chybně"
            }
        }
    }




    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        refreshUserFromRepository(username, password)
/*
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            //_loginResult.value = LoginResult(error = R.string.login_failed)
        }

 */
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }
}