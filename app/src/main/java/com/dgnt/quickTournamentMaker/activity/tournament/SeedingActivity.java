package com.dgnt.quickTournamentMaker.activity.tournament;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dgnt.quickTournamentMaker.activity.R;
import com.dgnt.quickTournamentMaker.eventListener.OnSeedChangeListener;
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.Seeder;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;
import com.dgnt.quickTournamentMaker.util.TournamentUtil;

import java.util.ArrayList;
import java.util.List;

public class SeedingActivity extends AppCompatActivity implements OnSeedChangeListener {

    private ArrayList<Participant> participantList;
    private List<ViewGroup> participantGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeding);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        participantGroupList = new ArrayList<>();

        final String title = getIntent().getStringExtra(TournamentActivity.INTENT_TOURNAMENT_TITLE_KEY);
        final String description = getIntent().getStringExtra(TournamentActivity.INTENT_TOURNAMENT_DESCRIPTION_KEY);
        participantList = getIntent().getParcelableArrayListExtra(TournamentActivity.INTENT_PARTICIPANTS_KEY);
        final Tournament.TournamentType tournamentType = (Tournament.TournamentType) getIntent().getSerializableExtra(TournamentActivity.INTENT_TOURNAMENT_TYPE_KEY);
        final long creationTimeInEpoch = getIntent().getLongExtra(TournamentActivity.INTENT_TOURNAMENT_CREATION_TIME_KEY, Tournament.NULL_TIME_VALUE);
        final long lastModifiedTimeInEpoch = getIntent().getLongExtra(TournamentActivity.INTENT_TOURNAMENT_LAST_MODIFIED_TIME_KEY, Tournament.NULL_TIME_VALUE);

        final Class clazz;
        final Seeder.SeedFillType seedFillType;

        switch (tournamentType) {
            case ELIMINATION:
                clazz = EliminationTournamentActivity.class;
                seedFillType = Seeder.SeedFillType.POWER_OF_2;
                break;
            case DOUBLE_ELIMINATION:
                clazz = DoubleEliminationTournamentActivity.class;
                seedFillType = Seeder.SeedFillType.POWER_OF_2;
                break;
            case ROUND_ROBIN:
                clazz = RoundRobinTournamentActivity.class;
                seedFillType = Seeder.SeedFillType.EVEN;
                break;
            case SWISS:
            default:
                clazz = SwissTournamentActivity.class;
                seedFillType = Seeder.SeedFillType.EVEN;
                break;
        }

        final Seeder seeder = new Seeder(participantList, seedFillType);
        seeder.seed();
        seeder.setOnSeedChangeListener(this);

        final ViewGroup seedingLayout = (ViewGroup) findViewById(R.id.seedingLayout);

        for (int i = 0; i < participantList.size(); i += 2) {
            final Participant participant1 = participantList.get(i);
            final Participant participant2 = participantList.get(i + 1);

            final ViewGroup layout_matchup = (ViewGroup) getLayoutInflater().inflate(R.layout.component_matchup, null);

            final TextView participant1_tv = (TextView) layout_matchup.findViewById(R.id.participant1_tv);
            participant1_tv.setText(TournamentUtil.resolveParticipantDisplayName(this,participant1));
            final TextView participant1Record_tv = (TextView) layout_matchup.findViewById(R.id.participant1Record_tv);
            participant1Record_tv.setVisibility(View.GONE);

            final TextView participant2_tv = (TextView) layout_matchup.findViewById(R.id.participant2_tv);
            participant2_tv.setText(TournamentUtil.resolveParticipantDisplayName(this,participant2));
            final TextView participant2Record_tv = (TextView) layout_matchup.findViewById(R.id.participant2Record_tv);
            participant2Record_tv.setVisibility(View.GONE);

            final int paddingVertical = (int) getResources().getDimension(R.dimen.participant_tv_height);
            layout_matchup.setPadding(0, paddingVertical, 0, paddingVertical);

            final ViewGroup participant1Group = (ViewGroup) layout_matchup.findViewById(R.id.participant1Group);
            participant1Group.setOnClickListener(new ParticipantClickListener(seeder, i));
            final ViewGroup participant2Group = (ViewGroup) layout_matchup.findViewById(R.id.participant2Group);
            participant2Group.setOnClickListener(new ParticipantClickListener(seeder, i + 1));

            participantGroupList.add(participant1Group);
            participantGroupList.add(participant2Group);

            seedingLayout.addView(layout_matchup);

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(getApplicationContext(),  clazz);
                intent.putExtra(TournamentActivity.INTENT_TOURNAMENT_TITLE_KEY, title);
                intent.putExtra(TournamentActivity.INTENT_TOURNAMENT_DESCRIPTION_KEY, description);
                intent.putParcelableArrayListExtra(TournamentActivity.INTENT_PARTICIPANTS_KEY, participantList);
                intent.putExtra(TournamentActivity.INTENT_TOURNAMENT_CREATION_TIME_KEY, creationTimeInEpoch);
                intent.putExtra(TournamentActivity.INTENT_TOURNAMENT_LAST_MODIFIED_TIME_KEY, lastModifiedTimeInEpoch);
                startActivity(intent);
                finish();
            }
        });

    }

    protected void updateParticipantGroupDrawables(final MatchUp.MatchUpStatus matchUpStatus, final ViewGroup participant1Group, final ViewGroup participant2Group) {
        switch (matchUpStatus) {
            case P1_WINNER:
                participant1Group.setBackgroundResource(R.drawable.p1_win);
                participant2Group.setBackgroundResource(R.drawable.p2_default);
                break;
            case P2_WINNER:
                participant1Group.setBackgroundResource(R.drawable.p1_default);
                participant2Group.setBackgroundResource(R.drawable.p2_win);
                break;
            case DEFAULT:
            default:
                participant1Group.setBackgroundResource(R.drawable.p1_default);
                participant2Group.setBackgroundResource(R.drawable.p2_default);
                break;
        }
    }

    @Override
    public void onParticipantListChange(List<Participant> participantList) {

        for (int i = 0; i < participantList.size(); i ++) {

            final ViewGroup participantGroup = participantGroupList.get(i);

            final TextView participant_tv = (TextView) participantGroup.findViewById(i % 2 == 0 ? R.id.participant1_tv : R.id.participant2_tv);
            participant_tv.setText(TournamentUtil.resolveParticipantDisplayName(this,participantList.get(i)));

        }
    }

    @Override
    public void onFirstPickChange(int index) {
        if (index == -1) {

            for (int i = 0; i < participantGroupList.size(); i++) {
                final ViewGroup participantGroup = participantGroupList.get(i);

                if (i % 2 == 0) {
                    participantGroup.setBackgroundResource(R.drawable.p1_default);
                } else {
                    participantGroup.setBackgroundResource(R.drawable.p2_default);
                }
            }
        } else {
            final ViewGroup participantGroup = participantGroupList.get(index);
            if (index % 2 == 0) {
                participantGroup.setBackgroundResource(R.drawable.p1_win);
            } else {
                participantGroup.setBackgroundResource(R.drawable.p2_win);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected class ParticipantClickListener implements View.OnClickListener {

        private Seeder seeder;
        private int participantIndex;

        protected ParticipantClickListener(final Seeder seeder, final int participantIndex) {
            this.seeder = seeder;
            this.participantIndex = participantIndex;
        }

        @Override
        public void onClick(View v) {

            seeder.toggleResult(participantIndex);
        }

    }
}
