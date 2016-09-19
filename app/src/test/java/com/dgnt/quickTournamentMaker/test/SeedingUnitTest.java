package com.dgnt.quickTournamentMaker.test;

import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.Seeder;
import com.dgnt.quickTournamentMaker.test.tournament.PersonUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Owner on 3/25/2016.
 */
public class SeedingUnitTest {

    private List<Participant>  participantList5;
    private List<Participant>  participantList6;
    private List<Participant>  participantList9;
    private Participant andrew;
    private Participant john;
    private Participant tommy;
    private Participant william;
    private Participant kelsey;
    private Participant ben;
    private Participant tim;
    private Participant arthur;
    private Participant tony;

    private Seeder seeder;

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


         participantList5 = new ArrayList<>();
        participantList5.add(andrew);
        participantList5.add(john);
        participantList5.add(tommy);
        participantList5.add(william);
        participantList5.add(kelsey);

        participantList6 = new ArrayList<>();
        participantList6.add(andrew);
        participantList6.add(john);
        participantList6.add(tommy);
        participantList6.add(william);
        participantList6.add(kelsey);
        participantList6.add(ben);


        participantList9 = new ArrayList<>();
        participantList9.add(andrew);
        participantList9.add(john);
        participantList9.add(tommy);
        participantList9.add(william);
        participantList9.add(kelsey);
        participantList9.add(ben);
        participantList9.add(tim);
        participantList9.add(arthur);
        participantList9.add(tony);


    }


    @Test
    public void testFillForEven_5() throws Exception {
        seeder=new Seeder(participantList5, Seeder.SeedFillType.EVEN);
        seeder.seed();
        assertEquals(participantList5.get(3), william);
        assertEquals(participantList5.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testFillForPowerOf2_5() throws Exception {
        seeder=new Seeder(participantList5, Seeder.SeedFillType.POWER_OF_2);
        seeder.seed();
        assertEquals(participantList5.get(4), william);
        assertEquals(participantList5.get(3), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testFillAlways_5() throws Exception {
        seeder=new Seeder(participantList5, Seeder.SeedFillType.ALWAYS);
        seeder.seed();
        assertEquals(participantList5.get(0), andrew);
        assertEquals(participantList5.get(2), john);
        assertEquals(participantList5.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participantList5.get(5), Participant.BYE_PARTICIPANT);
    }

    @Test
    public void testFillForEven_6() throws Exception {
        seeder=new Seeder(participantList6, Seeder.SeedFillType.EVEN);
        seeder.seed();
        assertEquals(participantList6.get(2), tommy);
    }

    @Test
    public void testFillForPowerOf2_6() throws Exception {
        seeder=new Seeder(participantList6, Seeder.SeedFillType.POWER_OF_2);
        seeder.seed();
        assertEquals(participantList6.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testFillAlways_6() throws Exception {
        seeder=new Seeder(participantList6, Seeder.SeedFillType.ALWAYS);
        seeder.seed();
        assertEquals(participantList6.get(0), andrew);
        assertEquals(participantList6.get(2), john);
        assertEquals(participantList6.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participantList6.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testFillForEven_9() throws Exception {
        seeder=new Seeder(participantList9, Seeder.SeedFillType.EVEN);
        seeder.seed();
        assertEquals(participantList9.get(2), tommy);
        assertEquals(participantList9.get(8), tony);
        assertEquals(participantList9.get(9), Participant.BYE_PARTICIPANT);
    }

    @Test
    public void testFillForPowerOf2_9() throws Exception {
        seeder=new Seeder(participantList9, Seeder.SeedFillType.POWER_OF_2);
        seeder.seed();
        assertEquals(participantList9.get(0), andrew);
        assertEquals(participantList9.get(2), tommy);
        assertEquals(participantList9.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testFillAlways_9() throws Exception {
        seeder=new Seeder(participantList9, Seeder.SeedFillType.ALWAYS);
        seeder.seed();
        assertEquals(participantList9.get(0), andrew);
        assertEquals(participantList9.get(2), john);
        assertEquals(participantList9.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participantList9.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testToggleResult_part1() throws Exception {
        seeder=new Seeder(participantList5, Seeder.SeedFillType.EVEN);
        seeder.seed();
        seeder.toggleResult(0);
        seeder.toggleResult(1);
        assertEquals(participantList5.get(0), john);
        assertEquals(participantList5.get(1), andrew);


        seeder.toggleResult(0);
        seeder.toggleResult(1);
        assertEquals(participantList5.get(1), john);
        assertEquals(participantList5.get(0), andrew);

        seeder.toggleResult(2);
        seeder.toggleResult(5);
        assertEquals(participantList5.get(2), william);
        assertEquals(participantList5.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participantList5.get(5), tommy);
    }

    @Test
    public void testToggleResult_byeFails() throws Exception {
        seeder=new Seeder(participantList9, Seeder.SeedFillType.POWER_OF_2);
        seeder.seed();


        assertEquals(participantList9.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participantList9.get(5), Participant.BYE_PARTICIPANT);

        seeder.toggleResult(3);
        final Seeder.ToggleResult toggleResult = seeder.toggleResult(5);
        assertEquals(toggleResult, Seeder.ToggleResult.TWO_BYES_REDUNDANT);
        assertEquals(participantList9.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participantList9.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testToggleResult_byeFails2() throws Exception {
        seeder=new Seeder(participantList6, Seeder.SeedFillType.POWER_OF_2);
        seeder.seed();



        seeder.toggleResult(5);
        final Seeder.ToggleResult toggleResult = seeder.toggleResult(6);
        assertEquals(toggleResult, Seeder.ToggleResult.TWO_BYES_FAIL);
        seeder.toggleResult(5);

        seeder.toggleResult(4);
        final Seeder.ToggleResult toggleResult2 = seeder.toggleResult(7);
        assertEquals(toggleResult2, Seeder.ToggleResult.TWO_BYES_FAIL);
    }

}
