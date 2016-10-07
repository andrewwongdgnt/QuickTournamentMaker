package com.dgnt.quickTournamentMaker.activity.tournament;

import android.preference.PreferenceManager;

import com.dgnt.quickTournamentMaker.model.tournament.SwissTournament;
import com.dgnt.quickTournamentMaker.util.PreferenceUtil;

public class SwissTournamentActivity extends TournamentActivity {

    protected void createTournament() {


        final SwissTournament swissTournament = new SwissTournament();
        swissTournament.setRankingConfig(getIntent().getStringExtra(INTENT_TOURNAMENT_RANK_CONFIG_KEY));
        tournament = swissTournament;
    }

}
