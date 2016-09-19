package com.dgnt.quickTournamentMaker.model.tournament;

import com.dgnt.quickTournamentMaker.eventListener.OnRoundUpdateListener;
import com.dgnt.quickTournamentMaker.util.TournamentUtil;

import java.util.List;

/**
 * Created by Owner on 3/13/2016.
 */
public class Round {

    private List<MatchUp> matchUpList;
    private String title;
    private String note;
    private int color;
    public Round(final List<MatchUp> matchUpList){
        this.matchUpList = matchUpList;
        title = "";
        note = "";
        color = TournamentUtil.DEFAULT_DISPLAY_COLOR;
    }

    private OnRoundUpdateListener onRoundUpdateListener;

    public void setOnRoundUpdateListener(OnRoundUpdateListener onRoundUpdateListener) {
        this.onRoundUpdateListener = onRoundUpdateListener;
    }

    public void dispatchRoundUpdate() {
        if (onRoundUpdateListener != null) {
            onRoundUpdateListener.onRoundUpdate(this);
        }
    }

    public MatchUp getMatchUpAt(final int index){
        return matchUpList.get(index);
    }

    public int getTotalMatchUps(){
        return matchUpList.size();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
        dispatchRoundUpdate();
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
        dispatchRoundUpdate();
    }

    public int getColor() {
        return color == 0 ? TournamentUtil.DEFAULT_DISPLAY_COLOR : color;
    }

    public void setColor(int color) {
        this.color = color;
        dispatchRoundUpdate();
    }
}
