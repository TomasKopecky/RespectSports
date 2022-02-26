package cz.respect.respectsports.domain

data class Match(val id: String?,
                 val date: String,
                 val homePlayerId: String,
                 val homePlayerName: String,
                 val homePlayerUsername: String,
                 val visitorPlayerId: String?,
                 val visitorPlayerName: String,
                 val visitorPlayerUsername: String,
                 val result: String) {
}