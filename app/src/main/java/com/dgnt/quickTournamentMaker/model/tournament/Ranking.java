package com.dgnt.quickTournamentMaker.model.tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Owner on 6/8/2016.
 */
public class Ranking {

    public static final int UNKNOWN_RANK = -1;

    private Set<RankHolder> unknownRankings;
    private Set<RankHolder> knownRankings;

    private List<RankHolder> unknownRankingsList;
    private List<RankHolder> knownRankingsList;

    private boolean isUnknownRankingsSorted;
    private boolean isKnownRankingsSorted;

    public Ranking() {
        unknownRankings = new HashSet<>();
        knownRankings = new HashSet<>();
    }

    public List<RankHolder> getKnownRankings() {
        if (knownRankingsList==null){
            knownRankingsList = new ArrayList<>(knownRankings);
        }

        if (!isKnownRankingsSorted) {
            knownRankingsList.clear();
            knownRankingsList.addAll(knownRankings);
            Collections.sort(knownRankingsList);
            isKnownRankingsSorted = true;
        }
        return knownRankingsList;
    }

    public List<RankHolder> getUnknownRankings() {
        if (unknownRankingsList==null){
            unknownRankingsList = new ArrayList<>(unknownRankings);
        }

        if (!isUnknownRankingsSorted) {
            unknownRankingsList.clear();
            unknownRankingsList.addAll(unknownRankings);
            Collections.sort(unknownRankingsList);
            isUnknownRankingsSorted = true;
        }
        return unknownRankingsList;
    }

    public boolean addToKnownRanking(final int rank, final Participant participant) {
        if (participant.isNormal()) {
            final RankHolder rankHolder = new RankHolder(rank, participant);
            isKnownRankingsSorted = false;
            return knownRankings.add(rankHolder);
        }
        return false;
    }


    public boolean addToUnknownRanking(final Participant participant) {
        if (participant.isNormal()) {
            final RankHolder rankHolder = new RankHolder(UNKNOWN_RANK, participant);
            isUnknownRankingsSorted = false;
            return unknownRankings.add(rankHolder);
        }
        return false;
    }


    public class RankHolder implements Comparable<RankHolder> {
        private int rank;
        private Participant participant;

        public RankHolder(final int rank, final Participant participant) {
            this.rank = rank;
            this.participant = participant;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (other == this) return true;
            if (!(other instanceof RankHolder)) return false;
            RankHolder otherOne = (RankHolder) other;

            if (getParticipant().equals(otherOne.getParticipant())) return true;

            return false;
        }

        @Override
        public int hashCode() {
            return getParticipant().hashCode();
        }

        public int getRank() {
            return rank;
        }

        public Participant getParticipant() {
            return participant;
        }

        @Override
        public int compareTo(RankHolder otherRankHolder) {
            if (rank != otherRankHolder.getRank()) {
                return rank - otherRankHolder.getRank();
            }
            return getParticipant().compareTo(otherRankHolder.getParticipant());
        }
    }

}
