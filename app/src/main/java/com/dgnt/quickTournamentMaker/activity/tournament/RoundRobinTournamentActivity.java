package com.dgnt.quickTournamentMaker.activity.tournament;

import com.dgnt.quickTournamentMaker.model.tournament.RoundRobinTournament;

public class RoundRobinTournamentActivity extends TournamentActivity {

    protected void createTournament() {

        tournament = new RoundRobinTournament();
    }


}
