package cz.respect.respectsports.ui.login

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cz.respect.respectsports.R
import cz.respect.respectsports.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading


        fun startLoginLoading() {
            if (username.length() > 0 && password.length() > 5) {
                username.isEnabled = false
                password.isEnabled = false
                login.isEnabled = false
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }

        fun endLoginLoading() {
            username.isEnabled = true
            password.isEnabled = true
            login.isEnabled = true
            loading.visibility = View.INVISIBLE
        }

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(application))
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.message.observe(this, Observer {
            Log.i("MY_INFO", "OBSERVING DONE")
            endLoginLoading()
            showResultMessage(it)
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            Log.i("MY_INFO", "RESULT OBTAINED")

            if (loginResult.error != null) {
                endLoginLoading()
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                endLoginLoading()
                updateUiWithUser(loginResult.success)
                //finish()
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            //finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }



            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        startLoginLoading()
                }
                false
            }

            login.setOnClickListener {
                startLoginLoading()
            }


        }

    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val userWelcomeStart = getString(R.string.user_welcome_start)
        val userWelcomeEnd = getString(R.string.user_welcome_end)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$userWelcomeStart $displayName $userWelcomeEnd",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }


    private fun showResultMessage(apiResponseString:String) {
        Toast.makeText(
            this,
            apiResponseString,
            Toast.LENGTH_LONG
        ).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}