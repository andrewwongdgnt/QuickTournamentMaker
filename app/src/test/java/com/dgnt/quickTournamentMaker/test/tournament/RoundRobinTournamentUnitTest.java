package com.dgnt.quickTournamentMaker.test.tournament;

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.RoundRobinTournament;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Owner on 3/13/2016.
 */
public class RoundRobinTournamentUnitTest implements TournamentUnitTest, RecordKeepingTournamentUnitTest {

    private RoundRobinTournament tournament_noByes;

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

        final List<Participant> participantList = new ArrayList<>();
        participantList.add(andrew);
        participantList.add(john);
        participantList.add(tommy);
        participantList.add(william);
        participantList.add(kelsey);
        participantList.add(ben);
        participantList.add(tim);
        participantList.add(arthur);

        tournament_noByes = new RoundRobinTournament();
        tournament_noByes.build(participantList);
    }

    @After
    public void teardown() throws Exception {

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
        assertEquals(totalMatchUps, 7*4);
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
        //round 1 match ups
        assertEquals(tournament_noByes.getParticipant(0, 0, 0, 0), andrew);
        assertEquals(tournament_noByes.getParticipant(0, 0, 0, 1), john);
        assertEquals(tournament_noByes.getParticipant(0, 0, 1, 0), tommy);
        assertEquals(tournament_noByes.getParticipant(0, 0, 1, 1), william);
        assertEquals(tournament_noByes.getParticipant(0, 0, 2, 0), kelsey);
        assertEquals(tournament_noByes.getParticipant(0, 0, 2, 1), ben);
        assertEquals(tournament_noByes.getParticipant(0, 0, 3, 0), tim);
        assertEquals(tournament_noByes.getParticipant(0, 0, 3, 1), arthur);

        //round 2 match ups
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 0), andrew);
        assertEquals(tournament_noByes.getParticipant(0, 1, 0, 1), tommy);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 0), kelsey);
        assertEquals(tournament_noByes.getParticipant(0, 1, 1, 1), john);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 0), tim);
        assertEquals(tournament_noByes.getParticipant(0, 1, 2, 1), william);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 0), arthur);
        assertEquals(tournament_noByes.getParticipant(0, 1, 3, 1), ben);

        //round 3 match ups
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 0), andrew);
        assertEquals(tournament_noByes.getParticipant(0, 2, 0, 1), kelsey);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 0), tim);
        assertEquals(tournament_noByes.getParticipant(0, 2, 1, 1), tommy);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 0), arthur);
        assertEquals(tournament_noByes.getParticipant(0, 2, 2, 1), john);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 0), ben);
        assertEquals(tournament_noByes.getParticipant(0, 2, 3, 1), william);

        //round 4 match ups
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 0), andrew);
        assertEquals(tournament_noByes.getParticipant(0, 3, 0, 1), tim);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 0), arthur);
        assertEquals(tournament_noByes.getParticipant(0, 3, 1, 1), kelsey);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 0), ben);
        assertEquals(tournament_noByes.getParticipant(0, 3, 2, 1), tommy);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 0), william);
        assertEquals(tournament_noByes.getParticipant(0, 3, 3, 1), john);

        //round 5 match ups
        assertEquals(tournament_noByes.getParticipant(0, 4, 0, 0), andrew);
        assertEquals(tournament_noByes.getParticipant(0, 4, 0, 1), arthur);
        assertEquals(tournament_noByes.getParticipant(0, 4, 1, 0), ben);
        assertEquals(tournament_noByes.getParticipant(0, 4, 1, 1), tim);
        assertEquals(tournament_noByes.getParticipant(0, 4, 2, 0), william);
        assertEquals(tournament_noByes.getParticipant(0, 4, 2, 1), kelsey);
        assertEquals(tournament_noByes.getParticipant(0, 4, 3, 0), john);
        assertEquals(tournament_noByes.getParticipant(0, 4, 3, 1), tommy);

        //round 6 match ups
        assertEquals(tournament_noByes.getParticipant(0, 5, 0, 0), andrew);
        assertEquals(tournament_noByes.getParticipant(0, 5, 0, 1), ben);
        assertEquals(tournament_noByes.getParticipant(0, 5, 1, 0), william);
        assertEquals(tournament_noByes.getParticipant(0, 5, 1, 1), arthur);
        assertEquals(tournament_noByes.getParticipant(0, 5, 2, 0), john);
        assertEquals(tournament_noByes.getParticipant(0, 5, 2, 1), tim);
        assertEquals(tournament_noByes.getParticipant(0, 5, 3, 0), tommy);
        assertEquals(tournament_noByes.getParticipant(0, 5, 3, 1), kelsey);

        //round 7 match ups
        assertEquals(tournament_noByes.getParticipant(0, 6, 0, 0), andrew);
        assertEquals(tournament_noByes.getParticipant(0, 6, 0, 1), william);
        assertEquals(tournament_noByes.getParticipant(0, 6, 1, 0), john);
        assertEquals(tournament_noByes.getParticipant(0, 6, 1, 1), ben);
        assertEquals(tournament_noByes.getParticipant(0, 6, 2, 0), tommy);
        assertEquals(tournament_noByes.getParticipant(0, 6, 2, 1), arthur);
        assertEquals(tournament_noByes.getParticipant(0, 6, 3, 0), kelsey);
        assertEquals(tournament_noByes.getParticipant(0, 6, 3, 1), tim);
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

    }
}
