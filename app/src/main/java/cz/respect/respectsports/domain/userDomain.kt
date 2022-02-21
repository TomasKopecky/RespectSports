package cz.respect.respectsports.domain

data class User(val id: String,
                      val name: String,
                      val token: String?) {

    fun encryptToken() {
        val cipherText: ByteArray = crypto.encrypt(plainText) // https://github.com/facebookarchive/conceal
    }
}
