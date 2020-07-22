package com.dgnt.quickTournamentMaker.data.tournament

import com.dgnt.quickTournamentMaker.data.IKeyable
import com.dgnt.quickTournamentMaker.util.TournamentUtil


enum class MatchUpStatus {
    DEFAULT, TIE, P1_WINNER, P2_WINNER
}

data class MatchUp (var participant1:Participant, var participant2:Participant) {
    var status:MatchUpStatus = MatchUpStatus.DEFAULT
    var note:String = ""
    var color:Int = TournamentUtil.DEFAULT_DISPLAY_COLOR
}