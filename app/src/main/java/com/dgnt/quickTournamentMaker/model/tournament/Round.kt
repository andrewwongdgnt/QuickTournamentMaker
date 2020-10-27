package com.dgnt.quickTournamentMaker.model.tournament

import com.dgnt.quickTournamentMaker.util.TournamentUtil

data class Round(var matchUps:List<MatchUp>) {
    var title:String = ""
    var note:String=""
    var color:Int = TournamentUtil.DEFAULT_DISPLAY_COLOR

}