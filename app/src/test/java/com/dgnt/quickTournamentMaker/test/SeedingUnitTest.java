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

    private List<Participant>  participants5;
    private List<Participant>  participants6;
    private List<Participant>  participants9;
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


         participants5 = new ArrayList<>();
        participants5.add(andrew);
        participants5.add(john);
        participants5.add(tommy);
        participants5.add(william);
        participants5.add(kelsey);

        participants6 = new ArrayList<>();
        participants6.add(andrew);
        participants6.add(john);
        participants6.add(tommy);
        participants6.add(william);
        participants6.add(kelsey);
        participants6.add(ben);


        participants9 = new ArrayList<>();
        participants9.add(andrew);
        participants9.add(john);
        participants9.add(tommy);
        participants9.add(william);
        participants9.add(kelsey);
        participants9.add(ben);
        participants9.add(tim);
        participants9.add(arthur);
        participants9.add(tony);


    }


    @Test
    public void testFillForEven_5() throws Exception {
        seeder=new Seeder(participants5, Seeder.SeedFillType.EVEN);
        seeder.seed();
        assertEquals(participants5.get(3), william);
        assertEquals(participants5.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testFillForPowerOf2_5() throws Exception {
        seeder=new Seeder(participants5, Seeder.SeedFillType.POWER_OF_2);
        seeder.seed();
        assertEquals(participants5.get(4), william);
        assertEquals(participants5.get(3), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testFillAlways_5() throws Exception {
        seeder=new Seeder(participants5, Seeder.SeedFillType.ALWAYS);
        seeder.seed();
        assertEquals(participants5.get(0), andrew);
        assertEquals(participants5.get(2), john);
        assertEquals(participants5.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participants5.get(5), Participant.BYE_PARTICIPANT);
    }

    @Test
    public void testFillForEven_6() throws Exception {
        seeder=new Seeder(participants6, Seeder.SeedFillType.EVEN);
        seeder.seed();
        assertEquals(participants6.get(2), tommy);
    }

    @Test
    public void testFillForPowerOf2_6() throws Exception {
        seeder=new Seeder(participants6, Seeder.SeedFillType.POWER_OF_2);
        seeder.seed();
        assertEquals(participants6.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testFillAlways_6() throws Exception {
        seeder=new Seeder(participants6, Seeder.SeedFillType.ALWAYS);
        seeder.seed();
        assertEquals(participants6.get(0), andrew);
        assertEquals(participants6.get(2), john);
        assertEquals(participants6.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participants6.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testFillForEven_9() throws Exception {
        seeder=new Seeder(participants9, Seeder.SeedFillType.EVEN);
        seeder.seed();
        assertEquals(participants9.get(2), tommy);
        assertEquals(participants9.get(8), tony);
        assertEquals(participants9.get(9), Participant.BYE_PARTICIPANT);
    }

    @Test
    public void testFillForPowerOf2_9() throws Exception {
        seeder=new Seeder(participants9, Seeder.SeedFillType.POWER_OF_2);
        seeder.seed();
        assertEquals(participants9.get(0), andrew);
        assertEquals(participants9.get(2), tommy);
        assertEquals(participants9.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testFillAlways_9() throws Exception {
        seeder=new Seeder(participants9, Seeder.SeedFillType.ALWAYS);
        seeder.seed();
        assertEquals(participants9.get(0), andrew);
        assertEquals(participants9.get(2), john);
        assertEquals(participants9.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participants9.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testToggleResult_part1() throws Exception {
        seeder=new Seeder(participants5, Seeder.SeedFillType.EVEN);
        seeder.seed();
        seeder.toggleResult(0);
        seeder.toggleResult(1);
        assertEquals(participants5.get(0), john);
        assertEquals(participants5.get(1), andrew);


        seeder.toggleResult(0);
        seeder.toggleResult(1);
        assertEquals(participants5.get(1), john);
        assertEquals(participants5.get(0), andrew);

        seeder.toggleResult(2);
        seeder.toggleResult(5);
        assertEquals(participants5.get(2), william);
        assertEquals(participants5.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participants5.get(5), tommy);
    }

    @Test
    public void testToggleResult_byeFails() throws Exception {
        seeder=new Seeder(participants9, Seeder.SeedFillType.POWER_OF_2);
        seeder.seed();


        assertEquals(participants9.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participants9.get(5), Participant.BYE_PARTICIPANT);

        seeder.toggleResult(3);
        final Seeder.ToggleResult toggleResult = seeder.toggleResult(5);
        assertEquals(toggleResult, Seeder.ToggleResult.TWO_BYES_REDUNDANT);
        assertEquals(participants9.get(3), Participant.BYE_PARTICIPANT);
        assertEquals(participants9.get(5), Participant.BYE_PARTICIPANT);
    }


    @Test
    public void testToggleResult_byeFails2() throws Exception {
        seeder=new Seeder(participants6, Seeder.SeedFillType.POWER_OF_2);
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
