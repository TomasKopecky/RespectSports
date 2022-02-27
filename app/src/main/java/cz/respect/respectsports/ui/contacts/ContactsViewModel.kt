package cz.respect.respectsports.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Zde budou kontaktní údaje na vývojáře"
    }
    val text: LiveData<String> = _text
}