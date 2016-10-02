package com.dgnt.quickTournamentMaker.activity.tournament;

import android.preference.PreferenceManager;

import com.dgnt.quickTournamentMaker.model.tournament.SwissTournament;
import com.dgnt.quickTournamentMaker.util.PreferenceUtil;

public class SwissTournamentActivity extends TournamentActivity {

    protected void createTournament() {


        final SwissTournament swissTournament = new SwissTournament();
        PreferenceUtil.setRankingConfig(PreferenceManager.getDefaultSharedPreferences(this), swissTournament, true);

        tournament = swissTournament;
    }

}
