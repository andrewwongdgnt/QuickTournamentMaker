package com.dgnt.quickTournamentMaker.activity.tournament;

import android.preference.PreferenceManager;

import com.dgnt.quickTournamentMaker.model.tournament.RoundRobinTournament;
import com.dgnt.quickTournamentMaker.util.PreferenceUtil;

public class RoundRobinTournamentActivity extends TournamentActivity {

    protected void createTournament() {

        final RoundRobinTournament roundRobinTournament = new RoundRobinTournament();
        PreferenceUtil.setRankingConfig(PreferenceManager.getDefaultSharedPreferences(this), roundRobinTournament, false);

        tournament = roundRobinTournament;
    }


}
