package cz.respect.respectsports.domain

data class Match(val id: String,
                 val date: String,
                 val homePlayer: String,
                 val visitorPlayer: String,
                 val result: String) {
}