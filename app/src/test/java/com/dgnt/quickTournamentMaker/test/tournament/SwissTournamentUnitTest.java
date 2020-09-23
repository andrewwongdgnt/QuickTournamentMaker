package com.dgnt.quickTournamentMaker.test.tournament;

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.SwissTournament;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Owner on 3/13/2016.
 */
public class SwissTournamentUnitTest implements TournamentUnitTest, RecordKeepingTournamentUnitTest {

    private SwissTournament tournament_noByes;

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
        tim = new Participant(PersonUtil.TIM);
        arthur = new Participant(PersonUtil.ARTHUR);

        final List<Participant> participants = new ArrayList<>();
        participants.add(andrew);
        participants.add(john);
        participants.add(tommy);
        participants.add(william);
        participants.add(kelsey);
        participants.add(ben);
        participants.add(tim);
        participants.add(arthur);

        tournament_noByes = new SwissTournament();
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
        assertEquals(tournament_noByes.getTotalRounds(0), 7);
    }

    @Test
    public void testTotalMatchUps() throws Exception {
        int totalMatchUps = 0;
        for (int i = 0; i < tournament_noByes.getTotalRounds(0); i++) {
            totalMatchUps += tournament_noByes.getRoundAt(0, i).getTotalMatchUps();
        }
        assertEquals(totalMatchUps, 7 * 4);

    }

    @Test
    public void testTotalParticipants() throws Exception {
        assertEquals(tournament_noByes.getSeededParticipants().size(), 8);
    }

    @Test
    public void testParticipants() throws Exception {
        final List<Participant> seededParticipant = tournament_noByes.getSeededParticipants();
        final Participant firstParticipant = seededParticipant.get(0);
        assertEquals(firstParticipant, andrew);

        firstParticipant.setNote("note");
        assertEquals(firstParticipant.getNote(), "note");
        firstParticipant.setDisplayName("DGNT");
        assertEquals(firstParticipant.getDisplayName(), "DGNT");

        assertEquals(seededParticipant.get(1), john);
        assertEquals(seededParticipant.get(2), tommy);
        assertEquals(seededParticipant.get(3), william);
        assertEquals(seededParticipant.get(4), kelsey);
        assertEquals(seededParticipant.get(5), ben);
        assertEquals(seededParticipant.get(6), tim);
        assertEquals(seededParticipant.get(7), arthur);
    }

    @Test
    public void testResultToggle() throws Exception {

        //by default, the status is default
        assertEquals(tournament_noByes.getMatchUpStatus(0, 5, 3), MatchUp.MatchUpStatus.DEFAULT);

        //participant 2 won
        tournament_noByes.toggleResult(0, 4, 0, 1);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 4, 0), MatchUp.MatchUpStatus.P2_WINNER);

        //participant 1 won
        tournament_noByes.toggleResult(0, 5, 1, 0);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 5, 1), MatchUp.MatchUpStatus.P1_WINNER);

        //toggling the status of participant 2 twice.  should bring back to default status
        tournament_noByes.toggleResult(0, 1, 1, 1);
        tournament_noByes.toggleResult(0, 1, 1, 1);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 1, 1), MatchUp.MatchUpStatus.DEFAULT);

        //toggling the status of participant 1 twice.  should bring back to default status
        tournament_noByes.toggleResult(0, 0, 1, 0);
        tournament_noByes.toggleResult(0, 0, 1, 0);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 0, 1), MatchUp.MatchUpStatus.DEFAULT);

        //toggle participant 2, making him the winner.  Then toggle participant 1, making it a tie
        tournament_noByes.toggleResult(0, 2, 1, 1);
        tournament_noByes.toggleResult(0, 2, 1, 0);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 2, 1), MatchUp.MatchUpStatus.TIE);

        //toggle participant 1, making him the winner.  Then toggle participant 2, making it a tie
        tournament_noByes.toggleResult(0, 2, 2, 0);
        tournament_noByes.toggleResult(0, 2, 2, 1);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 2, 2), MatchUp.MatchUpStatus.TIE);

        //toggle until tie.  then toggle participant 1, making participant 2 the winner
        tournament_noByes.toggleResult(0, 0, 0, 1);
        tournament_noByes.toggleResult(0, 0, 0, 0);
        tournament_noByes.toggleResult(0, 0, 0, 0);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 0, 0), MatchUp.MatchUpStatus.P2_WINNER);

        //toggle until tie.  then toggle participant 1, making participant 2 the winner
        tournament_noByes.toggleResult(0, 3, 0, 1);
        tournament_noByes.toggleResult(0, 3, 0, 0);
        tournament_noByes.toggleResult(0, 3, 0, 1);
        assertEquals(tournament_noByes.getMatchUpStatus(0, 3, 0), MatchUp.MatchUpStatus.P1_WINNER);
    }


    @Test
    public void testParticipantDistribution() throws Exception {
        //without any ties

        //Andrew wins in round 1 match 1. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 0, 0, 0);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 1), Participant.NULL_PARTICIPANT);

        //Tommy wins in round 1 match 2. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 0, 1, 0);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 1), Participant.NULL_PARTICIPANT);

//        Kelsey wins in round 1 match 3. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 0, 2, 0);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 1), Participant.NULL_PARTICIPANT);

//        Tim wins in round 1 match 4. Round is complete and a new round is generated
        tournament_noByes.toggleResult(0, 0, 3, 0);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 0), andrew);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 1), kelsey);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 0), tim);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 1), tommy);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 0), arthur);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 1), ben);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 0), john);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 1), william);

//        Andrew wins in round 2 match 1. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 1, 0, 0);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 1), Participant.NULL_PARTICIPANT);

//        tim wins in round 2 match 2. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 1, 1, 0);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 1), Participant.NULL_PARTICIPANT);

//        Arthur wins in round 2 match 3. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 1, 2, 0);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 1), Participant.NULL_PARTICIPANT);

//        John wins in round 2 match 4. Round is complete and a new round is generated
        tournament_noByes.toggleResult(0, 1, 3, 0);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 0), andrew);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 1), tim);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 0), arthur);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 1), john);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 0), kelsey);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 1), tommy);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 0), ben);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 1), william);

//        Andrew wins in round 3 match 1. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 2, 0, 0);
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 1), Participant.NULL_PARTICIPANT);

//        Arthur wins in round 3 match 2. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 2, 1, 0);
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 1), Participant.NULL_PARTICIPANT);

//        Kelsey wins in round 3 match 3. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 2, 2, 0);
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 1), Participant.NULL_PARTICIPANT);

//        Ben wins in round 3 match 4. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 2, 3, 0);
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 0), andrew);
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 1), arthur);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 0), kelsey);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 1), john);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 0), tim);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 1), william);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 0), ben);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 1), tommy);

        //Toggle Andrew at round 1 match up 1 such that he didnt actually win. Subsequent rounds are reset back to null participants
        tournament_noByes.toggleResult(0, 0, 0, 0);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 1), Participant.NULL_PARTICIPANT);
    }

    @Test
    public void testParticipantDistribution_withTies() throws Exception {
        //without any ties

        //Andrew wins in round 1 match 1. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 0, 0, 0);

        //Tommy wins in round 1 match 2. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 0, 1, 0);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 1), Participant.NULL_PARTICIPANT);

//        Kelsey vs Ben is a tie. Round is not done yet, so next round still have null participants
        tournament_noByes.toggleResult(0, 0, 2, 0);
        tournament_noByes.toggleResult(0, 0, 2, 1);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 1), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 1), Participant.NULL_PARTICIPANT);

//        Tim wins in round 1 match 4. Round is complete and a new round is generated
        tournament_noByes.toggleResult(0, 0, 3, 0);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 0), andrew);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 1), tim);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 0), tommy);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 1), ben);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 0), kelsey);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 1), arthur);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 0), john);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 1), william);

    }

    @Test
    public void testRecord() throws Exception {

        //Andrew at round 1 match up 1 won
        tournament_noByes.toggleResult(0, 0, 0, 0);
        assertEquals(andrew.getWins(), 1);
        assertEquals(andrew.getLosses(), 0);
        assertEquals(andrew.getTies(), 0);
        assertEquals(john.getWins(), 0);
        assertEquals(john.getLosses(), 1);
        assertEquals(john.getTies(), 0);

        //Toggle john at round 1 match up 1 so that andrew and john are tied
        tournament_noByes.toggleResult(0, 0, 0, 1);
        assertEquals(andrew.getWins(), 0);
        assertEquals(andrew.getLosses(), 0);
        assertEquals(andrew.getTies(), 1);
        assertEquals(john.getWins(), 0);
        assertEquals(john.getLosses(), 0);
        assertEquals(john.getTies(), 1);

        //Toggle Andrew at round 1 match up 1 so that andrew loses and john wins
        tournament_noByes.toggleResult(0, 0, 0, 0);
        assertEquals(andrew.getWins(), 0);
        assertEquals(andrew.getLosses(), 1);
        assertEquals(andrew.getTies(), 0);
        assertEquals(john.getWins(), 1);
        assertEquals(john.getLosses(), 0);
        assertEquals(john.getTies(), 0);

        //Toggle John at round 1 match up 1 so that Andrew vs John is back to the default state
        tournament_noByes.toggleResult(0, 0, 0, 1);
        assertEquals(andrew.getWins(), 0);
        assertEquals(andrew.getLosses(), 0);
        assertEquals(andrew.getTies(), 0);
        assertEquals(john.getWins(), 0);
        assertEquals(john.getLosses(), 0);
        assertEquals(john.getTies(), 0);

        //Tommy and William at round 1 match up 2 tie
        tournament_noByes.toggleResult(0, 0, 1, 1);
        tournament_noByes.toggleResult(0, 0, 1, 0);
        assertEquals(tommy.getWins(), 0);
        assertEquals(tommy.getLosses(), 0);
        assertEquals(tommy.getTies(), 1);
        assertEquals(william.getWins(), 0);
        assertEquals(william.getLosses(), 0);
        assertEquals(william.getTies(), 1);

        //Reset tommy vs william
        tournament_noByes.toggleResult(0, 0, 1, 1);
        tournament_noByes.toggleResult(0, 0, 1, 0);
        assertEquals(tommy.getWins(), 0);
        assertEquals(tommy.getLosses(), 0);
        assertEquals(tommy.getTies(), 0);
        assertEquals(william.getWins(), 0);
        assertEquals(william.getLosses(), 0);
        assertEquals(william.getTies(), 0);

        //Copy testParticipantDistribution()
        tournament_noByes.toggleResult(0, 0, 0, 0);
        tournament_noByes.toggleResult(0, 0, 1, 0);
        tournament_noByes.toggleResult(0, 0, 2, 0);
        tournament_noByes.toggleResult(0, 0, 3, 0);
        tournament_noByes.toggleResult(0, 1, 0, 0);
        tournament_noByes.toggleResult(0, 1, 1, 0);
        tournament_noByes.toggleResult(0, 1, 2, 0);
        tournament_noByes.toggleResult(0, 1, 3, 0);
        tournament_noByes.toggleResult(0, 2, 0, 0);
        tournament_noByes.toggleResult(0, 2, 1, 0);
        tournament_noByes.toggleResult(0, 2, 2, 0);
        tournament_noByes.toggleResult(0, 2, 3, 0);
        assertEquals(andrew.getWins(), 3);
        assertEquals(andrew.getLosses(), 0);
        assertEquals(andrew.getTies(), 0);
        assertEquals(arthur.getWins(), 2);
        assertEquals(arthur.getLosses(), 1);
        assertEquals(arthur.getTies(), 0);
        assertEquals(kelsey.getWins(), 2);
        assertEquals(kelsey.getLosses(), 1);
        assertEquals(kelsey.getTies(), 0);
        assertEquals(john.getWins(), 1);
        assertEquals(john.getLosses(), 2);
        assertEquals(john.getTies(), 0);
        assertEquals(tim.getWins(), 2);
        assertEquals(tim.getLosses(), 1);
        assertEquals(tim.getTies(), 0);
        assertEquals(william.getWins(), 0);
        assertEquals(william.getLosses(), 3);
        assertEquals(william.getTies(), 0);
        assertEquals(ben.getWins(), 1);
        assertEquals(ben.getLosses(), 2);
        assertEquals(ben.getTies(), 0);
        assertEquals(tommy.getWins(), 1);
        assertEquals(tommy.getLosses(), 2);
        assertEquals(tommy.getTies(), 0);

        //Toggle Andrew at round 1 match up 1 such that he didnt actually win. Subsequent rounds are reset back to null participants and records should indicate that
        tournament_noByes.toggleResult(0, 0, 0, 0);
        assertEquals(andrew.getWins(), 0);
        assertEquals(andrew.getLosses(), 0);
        assertEquals(andrew.getTies(), 0);
        assertEquals(arthur.getWins(), 0);
        assertEquals(arthur.getLosses(), 1);
        assertEquals(arthur.getTies(), 0);
        assertEquals(kelsey.getWins(), 1);
        assertEquals(kelsey.getLosses(), 0);
        assertEquals(kelsey.getTies(), 0);
        assertEquals(john.getWins(), 0);
        assertEquals(john.getLosses(), 0);
        assertEquals(john.getTies(), 0);
        assertEquals(tim.getWins(), 1);
        assertEquals(tim.getLosses(), 0);
        assertEquals(tim.getTies(), 0);
        assertEquals(william.getWins(), 0);
        assertEquals(william.getLosses(), 1);
        assertEquals(william.getTies(), 0);
        assertEquals(ben.getWins(), 0);
        assertEquals(ben.getLosses(), 1);
        assertEquals(ben.getTies(), 0);
        assertEquals(tommy.getWins(), 1);
        assertEquals(tommy.getLosses(), 0);
        assertEquals(tommy.getTies(), 0);
    }



}
