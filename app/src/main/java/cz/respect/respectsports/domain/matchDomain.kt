package cz.respect.respectsports.domain

import java.util.*

data class Match(val id: String?,
                 val date: Long,
                 val homePlayerId: String,
                 val homePlayerName: String?,
                 val homePlayerUsername: String?,
                 val visitorPlayerId: String?,
                 val visitorPlayerName: String?,
                 val visitorPlayerUsername: String?,
                 val result: String) {
}