package com.dgnt.quickTournamentMaker.model.history;

import com.dgnt.quickTournamentMaker.model.IKeyable;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Owner on 7/6/2016.
 */
public class HistoricalTournament implements IKeyable {

    public enum Sort {
        CREATION_DATE_NEWEST, CREATION_DATE_OLDEST, LAST_MODIFIED_DATE_NEWEST, LAST_MODIFIED_DATE_OLDEST, NAME, NAME_REVERSED, TOURNAMENT_TYPE, TOURNAMENT_TYPE_REVERSED
    }

    public enum View {
        MINIMAL, BASIC, DETAILED
    }

    private long creationTimeInEpoch;
    private long lastModifiedTimeInEpoch;
    private String name;
    private String note;
    private Tournament.TournamentType type;
    private String rankingConfig;
    private List<Participant> participantList;
    private List<HistoricalRound> roundList;
    private List<HistoricalMatchUp> matchUpList;

    public HistoricalTournament(final long creationTimeInEpoch, final long lastModifiedTimeInEpoch, final String name, final String note, final Tournament.TournamentType type, final String rankingConfig, final List<Participant> participantList, final List<HistoricalRound> roundList, final List<HistoricalMatchUp> matchUpList) {
        this.creationTimeInEpoch = creationTimeInEpoch;
        this.lastModifiedTimeInEpoch = lastModifiedTimeInEpoch;
        this.name = name;
        this.note = note;
        this.type = type;
        this.rankingConfig = rankingConfig;
        this.participantList = participantList;
        this.roundList = roundList;
        this.matchUpList = matchUpList;
    }


    final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getCreationTimeAsDate() {
        return sdf.format(new Date(creationTimeInEpoch));
    }

    public long getCreationTimeInEpoch() {
        return creationTimeInEpoch;
    }

    public String getLastModifiedTimeAsDate() {
        return sdf.format(new Date(lastModifiedTimeInEpoch));
    }

    public long getLastModifiedTimeInEpoch() {
        return lastModifiedTimeInEpoch;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public Tournament.TournamentType getType() {
        return type;
    }

    public boolean isRecordKeepingTournament () {
        return getType()== Tournament.TournamentType.SWISS || getType()== Tournament.TournamentType.ROUND_ROBIN;
    }

    public String getRankingConfig() {
        return rankingConfig;
    }

    public List<Participant> getParticipantList() {
        return participantList;
    }

    private List<Participant> normalSortedParticipantList;

    public List<Participant> getNormalSortedParticipantList() {
        if (normalSortedParticipantList == null) {
            normalSortedParticipantList = new ArrayList<>();
            for (final Participant participant : getParticipantList()) {
                if (participant.isNormal())
                    normalSortedParticipantList.add(participant);
            }
            Collections.sort(normalSortedParticipantList);
        }
        return normalSortedParticipantList;
    }

    public List<HistoricalRound> getRoundList() {
        return roundList;
    }

    public List<HistoricalMatchUp> getMatchUpList() {
        return matchUpList;
    }

    @Override
    public String getKey() {
        return Long.toString(getCreationTimeInEpoch());
    }
}
