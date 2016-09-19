package com.dgnt.quickTournamentMaker.model.tournament;

import com.dgnt.quickTournamentMaker.eventListener.OnSeedChangeListener;
import com.dgnt.quickTournamentMaker.util.TournamentUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Owner on 3/26/2016.
 */
public class Seeder {

    public enum SeedFillType {
        POWER_OF_2, EVEN, ALWAYS
    }


    public enum ToggleResult {
        SUCCESS, TWO_BYES_REDUNDANT, TWO_BYES_FAIL
    }

    public enum Type {
        RANDOM, CUSTOM, SAME
    }

    public Seeder(final List<Participant> participantList, final SeedFillType seedFillType) {

        this.participantList = participantList;
        this.seedFillType = seedFillType;
    }


    private SeedFillType seedFillType;

    private List<Participant> participantList;

    private int firstPickIndex = -1;

    private boolean seeded;

    public void seed() {

        switch (seedFillType) {
            case POWER_OF_2:
                fillForPowerOf2();
                break;
            case EVEN:
                fillForEvenNumbers();
                break;
            case ALWAYS:
                fillAlways();
                break;
        }
        seeded = true;
    }

    //take out byes
    public void clean() {
        ArrayList<Participant> copyList = new ArrayList<Participant>(participantList);

        participantList.clear();
        for (final Participant copy : copyList) {
            if (copy.isNormal())
                participantList.add(copy);
        }
    }

    public void sort (){
        Collections.sort(participantList);
    }

    public void randomize() {
        Collections.shuffle(participantList);
    }

    public ToggleResult toggleResult(final int index) {

        if (!seeded) {
            seed();
        }

        if (firstPickIndex == -1)
            firstPickIndex = index;
        else if (firstPickIndex == index)
            firstPickIndex = -1;
        else if (participantList.get(firstPickIndex).isBye() && participantList.get(index).isBye()) {
            return ToggleResult.TWO_BYES_REDUNDANT;
        } else if (
                (participantList.get(index).isBye() && firstPickIndex % 2 == 0 && participantList.get(firstPickIndex + 1).isBye())
                        ||
                        (participantList.get(firstPickIndex).isBye() && index % 2 == 0 && participantList.get(index + 1).isBye())
                ) {

            return ToggleResult.TWO_BYES_FAIL;
        } else {
            Collections.swap(participantList, firstPickIndex, index);
            firstPickIndex = -1;
            fixParticipantList();
            dispatchParticipantListChangeEvent(participantList);

        }

        dispatchFirstPickChangeEvent(firstPickIndex);
        return ToggleResult.SUCCESS;
    }

    //put byes on odd index so that the byes are always as participant 2
    private void fixParticipantList() {
        for (int i = 0; i < participantList.size(); i += 2) {
            if (participantList.get(i).isBye())
                Collections.swap(participantList, i, i + 1);
        }
    }


    private OnSeedChangeListener onSeedChangeListener;

    public void setOnSeedChangeListener(OnSeedChangeListener onSeedChangeListener) {
        this.onSeedChangeListener = onSeedChangeListener;
    }

    public void dispatchFirstPickChangeEvent(final int index) {
        if (onSeedChangeListener != null) {
            onSeedChangeListener.onFirstPickChange(index);
        }
    }

    public void dispatchParticipantListChangeEvent(final List<Participant> participantList) {
        if (onSeedChangeListener != null) {
            onSeedChangeListener.onParticipantListChange(participantList);
        }
    }

    //filling for byes for power of 2
    private void fillForPowerOf2() {

        ArrayList<Participant> copy = new ArrayList<Participant>(participantList);

        final int nextPowerOf2 = TournamentUtil.nextPowerOf2(participantList.size());

        final int startingIndex = nextPowerOf2 - (nextPowerOf2 - participantList.size()) * 2;

        participantList.clear();
        for (int i = 0; i < copy.size(); i++) {
            participantList.add(copy.get(i));
            if (i >= startingIndex)
                participantList.add(Participant.BYE_PARTICIPANT);
        }
    }

    //filling for byes for even numbers
    private void fillForEvenNumbers() {
        if (participantList.size() % 2 == 1) {
            participantList.add(Participant.BYE_PARTICIPANT);
        }
    }

    //always add byes
    private void fillAlways() {

        ArrayList<Participant> copyList = new ArrayList<Participant>(participantList);

        participantList.clear();
        for (final Participant copy : copyList) {
            participantList.add(copy);
            participantList.add(Participant.BYE_PARTICIPANT);
        }
    }
}
