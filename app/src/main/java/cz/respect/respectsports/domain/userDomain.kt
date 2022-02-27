package cz.respect.respectsports.domain

data class User(val id: String?,
                      val name: String?,
                      val username: String,
                      val email: String,
                      val token: String?) {
}
