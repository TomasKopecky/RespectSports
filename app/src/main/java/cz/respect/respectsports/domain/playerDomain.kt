package cz.respect.respectsports.domain

data class Player(val id: String,
                      val name: String) {
    override fun toString(): String {
        return this.name
    }
}