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

    public Seeder(final List<Participant> participants, final SeedFillType seedFillType) {

        this.participants = participants;
        this.seedFillType = seedFillType;
    }


    private SeedFillType seedFillType;

    private List<Participant> participants;

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
        ArrayList<Participant> copyList = new ArrayList<Participant>(participants);

        participants.clear();
        for (final Participant copy : copyList) {
            if (copy.isNormal())
                participants.add(copy);
        }
    }

    public void sort() {
        Collections.sort(participants);
    }

    public void randomize() {
        Collections.shuffle(participants);
    }

    public ToggleResult toggleResult(final int index) {

        if (!seeded) {
            seed();
        }

        if (firstPickIndex == -1)
            firstPickIndex = index;
        else if (firstPickIndex == index)
            firstPickIndex = -1;
        else if (participants.get(firstPickIndex).isBye() && participants.get(index).isBye()) {
            return ToggleResult.TWO_BYES_REDUNDANT;
        } else if (
                (participants.get(index).isBye() && firstPickIndex % 2 == 0 && participants.get(firstPickIndex + 1).isBye())
                        ||
                        (participants.get(firstPickIndex).isBye() && index % 2 == 0 && participants.get(index + 1).isBye())
        ) {

            return ToggleResult.TWO_BYES_FAIL;
        } else {
            Collections.swap(participants, firstPickIndex, index);
            firstPickIndex = -1;
            fixParticipantList();
            dispatchParticipantListChangeEvent(participants);

        }

        dispatchFirstPickChangeEvent(firstPickIndex);
        return ToggleResult.SUCCESS;
    }

    //put byes on odd index so that the byes are always as participant 2
    private void fixParticipantList() {
        for (int i = 0; i < participants.size(); i += 2) {
            if (participants.get(i).isBye())
                Collections.swap(participants, i, i + 1);
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

    public void dispatchParticipantListChangeEvent(final List<Participant> participants) {
        if (onSeedChangeListener != null) {
            onSeedChangeListener.onParticipantListChange(participants);
        }
    }

    //filling for byes for power of 2
    private void fillForPowerOf2() {

        ArrayList<Participant> copy = new ArrayList<Participant>(participants);

        final int nextPowerOf2 = TournamentUtil.nextPowerOf2(participants.size());

        final int startingIndex = nextPowerOf2 - (nextPowerOf2 - participants.size()) * 2;

        participants.clear();
        for (int i = 0; i < copy.size(); i++) {
            participants.add(copy.get(i));
            if (i >= startingIndex)
                participants.add(Participant.BYE_PARTICIPANT);
        }
    }

    //filling for byes for even numbers
    private void fillForEvenNumbers() {
        if (participants.size() % 2 == 1) {
            participants.add(Participant.BYE_PARTICIPANT);
        }
    }

    //always add byes
    private void fillAlways() {

        ArrayList<Participant> copyList = new ArrayList<Participant>(participants);

        participants.clear();
        for (final Participant copy : copyList) {
            participants.add(copy);
            participants.add(Participant.BYE_PARTICIPANT);
        }
    }
}
