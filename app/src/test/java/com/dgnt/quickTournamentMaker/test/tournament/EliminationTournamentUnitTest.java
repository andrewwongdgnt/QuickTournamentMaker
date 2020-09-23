package com.dgnt.quickTournamentMaker.test.tournament;

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.EliminationTournament;
import com.dgnt.quickTournamentMaker.model.tournament.Tournament;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Owner on 3/13/2016.
 */
public class EliminationTournamentUnitTest implements TournamentUnitTest {

    private EliminationTournament tournament_noByes;
    private Participant andrew;
    private Participant john;
    private Participant tommy;
    private Participant william;
    private Participant kelsey;
    private Participant ben;
    private Participant tim;
    private Participant arthur;

    @Before
    public void setup() throws Exception {

        andrew = new Participant(PersonUtil.ANDREW);
        john = new Participant(PersonUtil.JOHN);
        tommy = new Participant(PersonUtil.TOMMY);
        william = new Participant(PersonUtil.WILLIAM);
        kelsey = new Participant(PersonUtil.KELSEY);
        ben = new Participant(PersonUtil.BEN);
        tim= new Participant(PersonUtil.TIM);
        arthur= new Participant(PersonUtil.ARTHUR);

        final List<Participant> participants = new ArrayList<>();
        participants.add(andrew);
        participants.add(john);
        participants.add(tommy);
        participants.add(william);
        participants.add(kelsey);
        participants.add(ben);
        participants.add(tim);
        participants.add(arthur);

        tournament_noByes = new EliminationTournament();
        tournament_noByes.build(participants);
    }


    @Test
    public void testSuccess() throws Exception {
        assertEquals(tournament_noByes.isBuilt(), true);
    }

    @Test
    public void testTotalRoundGroups() throws Exception {
        assertEquals(tournament_noByes.getTotalRoundGroups(), 1);
    }

    @Test
    public void testTotalRounds() throws Exception {
        assertEquals(tournament_noByes.getTotalRounds(0), 3);
    }

    @Test
    public void testTotalMatchUps() throws Exception {
        int totalMatchUps = 0;
        for (int i = 0; i < tournament_noByes.getTotalRounds(0); i++) {
            totalMatchUps += tournament_noByes.getRoundAt(0, i).getTotalMatchUps();
        }
        assertEquals(totalMatchUps, 4+2+1);

    }

    @Test
    public void testTotalParticipants() throws Exception {
        assertEquals(tournament_noByes.getSeededParticipants().size(),8);
    }

    @Test
    public void testParticipants() throws Exception {
        final List<Participant> seededParticipant = tournament_noByes.getSeededParticipants();
        final Participant firstParticipant = seededParticipant.get(0);
        assertEquals(firstParticipant,andrew);

        firstParticipant.setNote("note");
        assertEquals(firstParticipant.getNote(),"note");
        firstParticipant.setDisplayName("DGNT");
        assertEquals(firstParticipant.getDisplayName(),"DGNT");

        assertEquals(seededParticipant.get(1),john);
        assertEquals(seededParticipant.get(2),tommy);
        assertEquals(seededParticipant.get(3),william);
        assertEquals(seededParticipant.get(4),kelsey);
        assertEquals(seededParticipant.get(5),ben);
        assertEquals(seededParticipant.get(6),tim);
        assertEquals(seededParticipant.get(7),arthur);
    }

    @Test
    public void testResultToggle() throws Exception {
        //by default, the status is default
        assertEquals(tournament_noByes.getMatchUpStatus(0, 0, 0), MatchUp.MatchUpStatus.DEFAULT);

        //participant 2 won
        tournament_noByes.toggleResult(0, 0, 0, 1);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 0, 0), MatchUp.MatchUpStatus.P2_WINNER);

        //participant 1 won
        tournament_noByes.toggleResult(0, 0, 1, 0);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 0, 1), MatchUp.MatchUpStatus.P1_WINNER);

        //toggling the status of participant 2 twice.  should bring back to default status
        tournament_noByes.toggleResult(0, 1, 1, 1);
        tournament_noByes.toggleResult(0, 1, 1, 1);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 1, 1), MatchUp.MatchUpStatus.DEFAULT);

        //toggling the status of participant 1 twice.  should bring back to default status
        tournament_noByes.toggleResult(0, 1, 1, 0);
        tournament_noByes.toggleResult(0, 1, 1, 0);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 1, 1), MatchUp.MatchUpStatus.DEFAULT);

        //toggle participant 2, making him the winner.  Then toggle participant 1, making him the winner
        tournament_noByes.toggleResult(0, 2, 0, 1);
        tournament_noByes.toggleResult(0, 2, 0, 0);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 2, 0), MatchUp.MatchUpStatus.P1_WINNER);

        //toggle participant 1, making him the winner.  Then toggle participant 2, making him the winner
        tournament_noByes.toggleResult(0, 1, 0, 0);
        tournament_noByes.toggleResult(0, 1, 0, 1);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 1, 0), MatchUp.MatchUpStatus.P2_WINNER);

        //First it was participant 2, then it was participant 1, now its default
        tournament_noByes.toggleResult(0, 0, 2, 1);
        tournament_noByes.toggleResult(0, 0, 2, 0);
        tournament_noByes.toggleResult(0, 0, 2, 0);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 0, 2), MatchUp.MatchUpStatus.DEFAULT);

        //First it was participant 1, then it was participant 2, now its default
        tournament_noByes.toggleResult(0, 0, 3, 0);
        tournament_noByes.toggleResult(0, 0, 3, 1);
        tournament_noByes.toggleResult(0, 0, 3, 1);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 0, 3), MatchUp.MatchUpStatus.DEFAULT);
    }

    @Test
    public void testParticipantDistribution() throws Exception {

        //John wins in round 1 match 1. should appear in round 2 match 1 as participant 1
        tournament_noByes.toggleResult(0, 0, 0, 1);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 0), john);

        //set winner of round 2 match 1 to not John. Then set winner of round 1 match 2 to Tommy.  Tommy should appear in round 3 match 1 as participant 1
        tournament_noByes.toggleResult(0, 1, 0, 1);
        tournament_noByes.toggleResult(0, 0, 1, 0);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 0), tommy);

        //set winner of round 1 match 2 to William. William should appear in round 2 match 1 as participant 2 and also in round 3 match 1 as participant 1
        tournament_noByes.toggleResult(0, 0, 1, 1);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 1), william);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 0), william);

        //set match status of round 1 match 2 to Default. No one appears in round 2 match 1 as participant 2 and also in round 3 match 1 as participant 1
        tournament_noByes.toggleResult(0, 0, 1, 1);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 0), Participant.NULL_PARTICIPANT);

    }

    @Test
    public void testRankingForElimination() throws Exception {

        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(0).getParticipant(), andrew);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(1).getParticipant(), arthur);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(2).getParticipant(), ben);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(3).getParticipant(), john);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(4).getParticipant(), kelsey);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(5).getParticipant(), tim);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(6).getParticipant(), tommy);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(7).getParticipant(), william);

        //John wins in round 1 match 1. Andrew should be ranked 4th
        tournament_noByes.toggleResult(0, 0, 0, 1);

        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(0).getParticipant(), andrew);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(0).getRank(), 4);

        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(0).getParticipant(), arthur);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(1).getParticipant(), ben);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(2).getParticipant(), john);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(3).getParticipant(), kelsey);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(4).getParticipant(), tim);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(5).getParticipant(), tommy);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(6).getParticipant(), william);

        //William wins in round 1 match 2. tommy should be ranked 4th
        tournament_noByes.toggleResult(0, 0, 1, 1);

        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(0).getParticipant(), andrew);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(0).getRank(), 4);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(1).getParticipant(), tommy);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(1).getRank(), 4);

        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(0).getParticipant(), arthur);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(1).getParticipant(), ben);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(2).getParticipant(), john);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(3).getParticipant(), kelsey);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(4).getParticipant(), tim);
        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().get(5).getParticipant(), william);

        //Ben wins in round 1 match 3. Kelsey should be ranked 4th
        tournament_noByes.toggleResult(0, 0, 2, 1);
        //Artur wins in round 1 match 4. Tim should be ranked 4th
        tournament_noByes.toggleResult(0, 0, 3, 1);

        //William wins in round 2 match 1. John should be ranked 3rd
        tournament_noByes.toggleResult(0, 1, 0, 1);
        //Artur wins in round 2 match 2. Ben should be ranked 3rd
        tournament_noByes.toggleResult(0, 1, 1, 1);


        //Artur wins in round 3 match 1. William should be ranked 2nd.  Arthur should be ranked 1st
        tournament_noByes.toggleResult(0, 2, 0, 1);

        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(0).getParticipant(), arthur);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(0).getRank(), 1);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(1).getParticipant(), william);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(1).getRank(), 2);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(2).getParticipant(), ben);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(2).getRank(), 3);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(3).getParticipant(), john);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(3).getRank(), 3);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(4).getParticipant(), andrew);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(4).getRank(), 4);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(5).getParticipant(), kelsey);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(5).getRank(), 4);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(6).getParticipant(), tim);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(6).getRank(), 4);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(7).getParticipant(), tommy);
        assertEquals(tournament_noByes.getCurrentRanking().getKnownRankings().get(7).getRank(), 4);

        assertEquals(tournament_noByes.getCurrentRanking().getUnknownRankings().size(), 0);


    }

    @Test
    public void testJson() throws Exception {
        final String json = Tournament.JsonHelper.toJson(tournament_noByes);

    }

}
