package com.dgnt.quickTournamentMaker.test.tournament;

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.DoubleEliminationTournament;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Owner on 3/13/2016.
 */
public class DoubleEliminationTournamentUnitTest implements TournamentUnitTest{

    private DoubleEliminationTournament tournament_noByes;
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

        tournament_noByes = new DoubleEliminationTournament();
        tournament_noByes.build(participants);
    }


    @Test
    public void testSuccess() throws Exception {
        assertEquals(tournament_noByes.isBuilt(), true);
    }

    @Test
    public void testTotalRoundGroups() throws Exception {
        assertEquals(tournament_noByes.getTotalRoundGroups(), 3);
    }

    @Test
    public void testTotalRounds() throws Exception {
        assertEquals(tournament_noByes.getTotalRounds(0), 3);
        assertEquals(tournament_noByes.getTotalRounds(1), 4);
        assertEquals(tournament_noByes.getTotalRounds(2), 2);
    }

    @Test
    public void testTotalMatchUps() throws Exception {
        int totalMatchUps_winnerBracket = 0;
        for (int i = 0; i < tournament_noByes.getTotalRounds(0); i++) {
            totalMatchUps_winnerBracket += tournament_noByes.getRoundAt(0, i).getTotalMatchUps();
        }
        assertEquals(totalMatchUps_winnerBracket, 7);

        int totalMatchUps_loserBracket = 0;
        for (int i = 0; i < tournament_noByes.getTotalRounds(1); i++) {
            totalMatchUps_loserBracket += tournament_noByes.getRoundAt(1, i).getTotalMatchUps();
        }
        assertEquals(totalMatchUps_loserBracket, 6);


        int totalMatchUps_finalBracket = 0;
        for (int i = 0; i < tournament_noByes.getTotalRounds(2); i++) {
            totalMatchUps_finalBracket += tournament_noByes.getRoundAt(2, i).getTotalMatchUps();
        }
        assertEquals(totalMatchUps_finalBracket, 2);
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

        //Testing the winner bracket

        //toggling the status of participant 2 twice.  should bring back to default status
        tournament_noByes.toggleResult(0, 1, 1, 1);
        tournament_noByes.toggleResult(0, 1, 1, 1);

        assertEquals(tournament_noByes.getMatchUpStatus(0, 1, 1), MatchUp.MatchUpStatus.DEFAULT);


        //toggling the status of participant 1 twice.  should bring back to default status
        tournament_noByes.toggleResult(0, 0, 1, 0);
        tournament_noByes.toggleResult(0, 0, 1, 0);

        assertEquals(tournament_noByes.getMatchUpStatus(0, 0, 1), MatchUp.MatchUpStatus.DEFAULT);

        //toggle participant 2, making him the winner.  Then toggle participant 1, making him the winner
        tournament_noByes.toggleResult(0, 2, 0, 1);
        tournament_noByes.toggleResult(0, 2, 0, 0);

        assertEquals(tournament_noByes.getMatchUpStatus(0, 2, 0), MatchUp.MatchUpStatus.P1_WINNER);


        //toggle participant 1, making him the winner.  Then toggle participant 2, making him the winner
        tournament_noByes.toggleResult(0, 0, 2, 0);
        tournament_noByes.toggleResult(0, 0, 2, 1);

        assertEquals(tournament_noByes.getMatchUpStatus(0, 0, 2), MatchUp.MatchUpStatus.P2_WINNER);

        //First it was participant 2, then it was participant 1, now its default
        tournament_noByes.toggleResult(0, 0, 0, 1);
        tournament_noByes.toggleResult(0, 0, 0, 0);
        tournament_noByes.toggleResult(0, 0, 0, 0);

        assertEquals(tournament_noByes.getMatchUpStatus(0, 0, 0), MatchUp.MatchUpStatus.DEFAULT);

        //First it was participant 1, then it was participant 2, now its default
        tournament_noByes.toggleResult(0, 1, 0, 0);
        tournament_noByes.toggleResult(0, 1, 0, 1);
        tournament_noByes.toggleResult(0, 1, 0, 1);

        assertEquals(tournament_noByes.getMatchUpStatus(0, 1, 0), MatchUp.MatchUpStatus.DEFAULT);
    }

    @Test
    public void testResultToggle_loserBracket() throws Exception {
        //toggling the status of participant 2 twice.  should bring back to default status
        tournament_noByes.toggleResult(1, 1, 1, 1);
        tournament_noByes.toggleResult(1, 1, 1, 1);

        assertEquals(tournament_noByes.getMatchUpStatus(1, 1, 1), MatchUp.MatchUpStatus.DEFAULT);


        //toggling the status of participant 1 twice.  should bring back to default status
        tournament_noByes.toggleResult(1, 0, 1, 0);
        tournament_noByes.toggleResult(1, 0, 1, 0);

        assertEquals(tournament_noByes.getMatchUpStatus(1, 0, 1), MatchUp.MatchUpStatus.DEFAULT);

        //toggle participant 2, making him the winner.  Then toggle participant 1, making him the winner
        tournament_noByes.toggleResult(1, 2, 0, 1);
        tournament_noByes.toggleResult(1, 2, 0, 0);

        assertEquals(tournament_noByes.getMatchUpStatus(1, 2, 0), MatchUp.MatchUpStatus.P1_WINNER);


        //toggle participant 1, making him the winner.  Then toggle participant 2, making him the winner
        tournament_noByes.toggleResult(1, 0, 0, 0);
        tournament_noByes.toggleResult(1, 0, 0, 1);

        assertEquals(tournament_noByes.getMatchUpStatus(1, 0,0), MatchUp.MatchUpStatus.P2_WINNER);

        //First it was participant 2, then it was participant 1, now its default
        tournament_noByes.toggleResult(1, 0, 0, 1);
        tournament_noByes.toggleResult(1, 0, 0, 0);
        tournament_noByes.toggleResult(1, 0, 0, 0);

        assertEquals(tournament_noByes.getMatchUpStatus(1, 0, 0), MatchUp.MatchUpStatus.DEFAULT);

        //First it was participant 1, then it was participant 2, now its default
        tournament_noByes.toggleResult(1, 1, 0, 0);
        tournament_noByes.toggleResult(1, 1, 0, 1);
        tournament_noByes.toggleResult(1, 1, 0, 1);

        assertEquals(tournament_noByes.getMatchUpStatus(1, 1, 0), MatchUp.MatchUpStatus.DEFAULT);
    }

    @Test

    public void testResultToggle_finalBracket() throws Exception {
        //toggling the status of participant 2 twice.  should bring back to default status
        tournament_noByes.toggleResult(2, 0, 0, 1);
        tournament_noByes.toggleResult(2, 0, 0, 1);

        assertEquals(tournament_noByes.getMatchUpStatus(2, 0, 0), MatchUp.MatchUpStatus.DEFAULT);


        //toggling the status of participant 1 twice.  should bring back to default status
        tournament_noByes.toggleResult(2, 0, 0, 0);
        tournament_noByes.toggleResult(2, 0, 0, 0);

        assertEquals(tournament_noByes.getMatchUpStatus(2, 0, 0), MatchUp.MatchUpStatus.DEFAULT);

        //toggle participant 2, making him the winner.  Then toggle participant 1, making him the winner
        tournament_noByes.toggleResult(2, 0, 0, 1);
        tournament_noByes.toggleResult(2, 0, 0, 0);

        assertEquals(tournament_noByes.getMatchUpStatus(2, 0, 0), MatchUp.MatchUpStatus.P1_WINNER);


        //toggle participant 1, making him the winner.  Then toggle participant 2, making him the winner
        tournament_noByes.toggleResult(2, 1, 0, 0);
        tournament_noByes.toggleResult(2, 1, 0, 1);

        assertEquals(tournament_noByes.getMatchUpStatus(2, 1,0), MatchUp.MatchUpStatus.P2_WINNER);

        //First it was participant 2, then it was participant 1, now its default
        tournament_noByes.toggleResult(2, 0, 0, 1);
        tournament_noByes.toggleResult(2, 0, 0, 0);
        tournament_noByes.toggleResult(2, 0, 0, 0);

        assertEquals(tournament_noByes.getMatchUpStatus(2, 0, 0), MatchUp.MatchUpStatus.DEFAULT);

        //First it was participant 1, then it was participant 2, now its default
        tournament_noByes.toggleResult(2, 1, 0, 0);
        tournament_noByes.toggleResult(2, 1, 0, 1);
        tournament_noByes.toggleResult(2, 1, 0, 1);

        assertEquals(tournament_noByes.getMatchUpStatus(2, 1, 0), MatchUp.MatchUpStatus.DEFAULT);
    }


    @Test
    public void testParticipantDistribution() throws Exception {
        //This test will simulate a dbl elim tournament

        //populating round 1 of loser bracket

        tournament_noByes.toggleResult(0, 0, 0, 1);
        assertEquals(tournament_noByes.getParticipant(1, 0, 0, 0), andrew);

        tournament_noByes.toggleResult(0, 0, 1, 0);
        assertEquals(tournament_noByes.getParticipant(1, 0, 0, 1), william);

        tournament_noByes.toggleResult(0, 0, 2, 1);
        assertEquals(tournament_noByes.getParticipant(1, 0, 1, 0), kelsey);

        tournament_noByes.toggleResult(0, 0, 3, 0);
        assertEquals(tournament_noByes.getParticipant(1, 0, 1, 1), arthur);


        //at this point, round 2 is John vs Tommy and Ben vs Tim
        tournament_noByes.toggleResult(0, 1, 0, 0);
        assertEquals(tournament_noByes.getParticipant(1, 1, 1, 0), tommy);

        tournament_noByes.toggleResult(0, 1, 1, 0);
        assertEquals(tournament_noByes.getParticipant(1, 1, 0, 0), tim);


        //at this point, round 3 is John vs Ben
        tournament_noByes.toggleResult(0, 2, 0, 0);
        assertEquals(tournament_noByes.getParticipant(1, 3, 0, 0), ben);
        assertEquals(tournament_noByes.getParticipant(2, 0, 0, 0), john);

        //For the loser bracket, round 1, it is Andrew vs William and Kelsey vs Arthur
        tournament_noByes.toggleResult(1, 0, 0, 1);
        tournament_noByes.toggleResult(1, 0, 1, 0);

        assertEquals(tournament_noByes.getParticipant(1, 1, 0, 1), william);
        assertEquals(tournament_noByes.getParticipant(1, 1, 1, 1), kelsey);


        //For the loser bracket, round 2, it is Tim vs William and Tommy vs Kelsey
        tournament_noByes.toggleResult(1, 1, 0, 1);
        tournament_noByes.toggleResult(1, 1, 1, 0);

        assertEquals(tournament_noByes.getParticipant(1, 2, 0, 0), william);
        assertEquals(tournament_noByes.getParticipant(1, 2, 0, 1), tommy);

        //For the loser bracket, round 3, it is William vs Tommy
        tournament_noByes.toggleResult(1, 2, 0, 1);

        assertEquals(tournament_noByes.getParticipant(1, 3, 0, 1), tommy);

        //For the loser bracket, round 3, it is Ben vs Tommy
        tournament_noByes.toggleResult(1, 3, 0, 1);

        assertEquals(tournament_noByes.getParticipant(2, 0, 0, 1), tommy);

        //For the final bracket, round 1, it is John vs Tommy.
        // If john wins, then round 2 wont exist
        tournament_noByes.toggleResult(2, 0, 0, 0);

        assertEquals(tournament_noByes.getParticipant(2, 1, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(2, 1, 0, 1), Participant.NULL_PARTICIPANT);

        // If tommy wins, then round 2 exists
        tournament_noByes.toggleResult(2, 0, 0, 1);

        assertEquals(tournament_noByes.getParticipant(2, 1, 0, 0), john);
        assertEquals(tournament_noByes.getParticipant(2, 1, 0, 1), tommy);
    }


    @Test
    public void testParticipantDistribution_reverse() throws Exception {
        //This will simulate the same dbl elim tournament as testParticipantDistribution() except the results will be set in reverse order.
        //All participants should still be in the same match up as testBracketPopulation()

        tournament_noByes.toggleResult(2, 0, 0, 1);

        tournament_noByes.toggleResult(1, 3, 0, 1);

        tournament_noByes.toggleResult(1, 2, 0, 1);

        tournament_noByes.toggleResult(1, 1, 1, 0);
        tournament_noByes.toggleResult(1, 1, 0, 1);

        tournament_noByes.toggleResult(1, 0, 1, 0);
        tournament_noByes.toggleResult(1, 0, 0, 1);

        tournament_noByes.toggleResult(0, 2, 0, 0);

        tournament_noByes.toggleResult(0, 1, 1, 0);

        tournament_noByes.toggleResult(0, 1, 0, 0);

        tournament_noByes.toggleResult(0, 0, 3, 0);

        tournament_noByes.toggleResult(0, 0, 2, 1);

        tournament_noByes.toggleResult(0, 0, 1, 0);

        tournament_noByes.toggleResult(0, 0, 0, 1);

        assertEquals(tournament_noByes.getParticipant(1, 0, 0, 0), andrew);

        assertEquals(tournament_noByes.getParticipant(1, 0, 0, 1), william);

        assertEquals(tournament_noByes.getParticipant(1, 0, 1, 0), kelsey);

        assertEquals(tournament_noByes.getParticipant(1, 0, 1, 1), arthur);


        assertEquals(tournament_noByes.getParticipant(1, 1, 1, 0), tommy);

        assertEquals(tournament_noByes.getParticipant(1, 1, 0, 0), tim);

        assertEquals(tournament_noByes.getParticipant(1, 3, 0, 0), ben);
        assertEquals(tournament_noByes.getParticipant(2, 0, 0, 0), john);

        assertEquals(tournament_noByes.getParticipant(1, 1, 0, 1), william);
        assertEquals(tournament_noByes.getParticipant(1, 1, 1, 1), kelsey);


        assertEquals(tournament_noByes.getParticipant(1, 2, 0, 0), william);
        assertEquals(tournament_noByes.getParticipant(1, 2, 0, 1), tommy);

        assertEquals(tournament_noByes.getParticipant(1, 3, 0, 1), tommy);

        assertEquals(tournament_noByes.getParticipant(2, 0, 0, 1), tommy);


        assertEquals(tournament_noByes.getParticipant(2, 1, 0, 0), john);
        assertEquals(tournament_noByes.getParticipant(2, 1, 0, 1), tommy);
    }

}
