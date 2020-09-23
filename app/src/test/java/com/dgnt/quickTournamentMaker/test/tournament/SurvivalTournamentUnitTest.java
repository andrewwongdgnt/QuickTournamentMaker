package com.dgnt.quickTournamentMaker.test.tournament;

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.SurvivalTournament;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Owner on 3/13/2016.
 */
public class SurvivalTournamentUnitTest implements  TournamentUnitTest {

    private SurvivalTournament tournament;
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
        participants.add(Participant.BYE_PARTICIPANT);
        participants.add(john);
        participants.add(Participant.BYE_PARTICIPANT);
        participants.add(tommy);
        participants.add(Participant.BYE_PARTICIPANT);
        participants.add(william);
        participants.add(Participant.BYE_PARTICIPANT);
        participants.add(kelsey);
        participants.add(Participant.BYE_PARTICIPANT);
        participants.add(ben);
        participants.add(Participant.BYE_PARTICIPANT);
        participants.add(tim);
        participants.add(Participant.BYE_PARTICIPANT);
        participants.add(arthur);
        participants.add(Participant.BYE_PARTICIPANT);


        tournament = new SurvivalTournament();
        tournament.build(participants);
    }


    @Test
    public void testSuccess() throws Exception {
        assertEquals(tournament.isBuilt(), true);
    }

    @Test
    public void testTotalRoundGroups() throws Exception {
        assertEquals(tournament.getTotalRoundGroups(), 1);
    }

    @Test
    public void testTotalRounds() throws Exception {
        assertEquals(tournament.getTotalRounds(0), 7);
    }

    @Test
    public void testTotalMatchUps() throws Exception {
        int totalMatchUps = 0;
        for (int i = 0; i < tournament.getTotalRounds(0); i++) {
            totalMatchUps += tournament.getRoundAt(0, i).getTotalMatchUps();
        }
        assertEquals(totalMatchUps, 7 * 8);

    }

    @Test
    public void testTotalParticipants() throws Exception {
        assertEquals(tournament.getSeededParticipants().size(),16);
    }

    @Test
    public void testParticipants() throws Exception {
        final List<Participant> seededParticipant = tournament.getSeededParticipants();
        final Participant firstParticipant = seededParticipant.get(0);
        assertEquals(firstParticipant,andrew);

        firstParticipant.setNote("note");
        assertEquals(firstParticipant.getNote(),"note");
        firstParticipant.setDisplayName("DGNT");
        assertEquals(firstParticipant.getDisplayName(),"DGNT");

        assertEquals(seededParticipant.get(2),john);
        assertEquals(seededParticipant.get(4),tommy);
        assertEquals(seededParticipant.get(6),william);
        assertEquals(seededParticipant.get(8),kelsey);
        assertEquals(seededParticipant.get(10),ben);
        assertEquals(seededParticipant.get(12),tim);
        assertEquals(seededParticipant.get(14),arthur);
    }


    @Test
    public void testResultToggle() throws Exception {

        //by default, the status is default
        assertEquals(tournament.getMatchUpStatus(0, 0, 0), MatchUp.MatchUpStatus.DEFAULT);

        //participant 1 won
        tournament.toggleResult(0, 0, 0, 0);
        assertEquals(tournament.getMatchUpStatus(0, 0, 0), MatchUp.MatchUpStatus.P1_WINNER);


        //toggling the status of participant 1 twice.  should bring back to default status
        tournament.toggleResult(0, 0, 1, 0);
        tournament.toggleResult(0, 0, 1, 0);
        assertEquals(tournament.getMatchUpStatus(0, 0, 1), MatchUp.MatchUpStatus.DEFAULT);


        //toggle 3 times, participant 1 should be winner
        tournament.toggleResult(0, 0, 2, 0);
        tournament.toggleResult(0, 0, 2, 0);
        tournament.toggleResult(0, 0, 2, 0);
        assertEquals(tournament.getMatchUpStatus(0, 0, 0), MatchUp.MatchUpStatus.P1_WINNER);

    }

    @Test
    public void testParticipantDistribution() throws Exception {

        //Andrew goes to next round. should appear in round 2 match 1 as participant 1
        tournament.toggleResult(0, 0, 0, 0);
        assertEquals(tournament.getParticipant(0, 1, 0, 0), andrew);

        //Tommy goes to next round. should appear in round 2 match 1 as participant 1
        tournament.toggleResult(0, 0, 2, 0);
        assertEquals(tournament.getParticipant(0, 0, 2, 0), tommy);


        //Andrew goes to  round 3 . should appear in round 3 match 1 as participant 1
        tournament.toggleResult(0, 1, 0, 0);
        assertEquals(tournament.getParticipant(0, 2, 0, 0), andrew);

        //Andrew didnt actually get to round 2. round 2 & 3, participant 1 should be null
        tournament.toggleResult(0, 0, 0, 0);
        assertEquals(tournament.getParticipant(0, 1, 0, 0), Participant.NULL_PARTICIPANT);
        assertEquals(tournament.getParticipant(0, 2, 0, 0), Participant.NULL_PARTICIPANT);
    }

}
