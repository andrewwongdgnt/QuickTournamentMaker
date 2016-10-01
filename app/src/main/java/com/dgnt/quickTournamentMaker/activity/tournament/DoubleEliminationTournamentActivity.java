package com.dgnt.quickTournamentMaker.activity.tournament;

import android.graphics.Point;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.model.tournament.DoubleEliminationTournament;
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp;
import com.dgnt.quickTournamentMaker.model.tournament.Participant;
import com.dgnt.quickTournamentMaker.model.tournament.Round;

import java.util.ArrayList;
import java.util.List;

public class DoubleEliminationTournamentActivity extends EliminationTournamentActivity {
    private LinearLayout winnerBracket_ll;
    private LinearLayout loserBracket_ll;
    private LinearLayout finalBracket_ll;

    private LinearLayout winnerBracketRoundHeaders_ll;
    private LinearLayout loserBracketRoundHeaders_ll;
    private LinearLayout finalBracketRoundHeaders_ll;

    protected void createTournament() {

        tournament = new DoubleEliminationTournament();
    }

    protected void setUpTournamentLayout() {
        winnerBracket_ll = new LinearLayout(this);
        winnerBracket_ll.setOrientation(LinearLayout.HORIZONTAL);
        loserBracket_ll = new LinearLayout(this);
        loserBracket_ll.setOrientation(LinearLayout.HORIZONTAL);
        finalBracket_ll = new LinearLayout(this);
        finalBracket_ll.setOrientation(LinearLayout.HORIZONTAL);

        winnerBracketRoundHeaders_ll = new LinearLayout(this);
        winnerBracketRoundHeaders_ll.setOrientation(LinearLayout.HORIZONTAL);
        loserBracketRoundHeaders_ll = new LinearLayout(this);
        loserBracketRoundHeaders_ll.setOrientation(LinearLayout.HORIZONTAL);
        finalBracketRoundHeaders_ll = new LinearLayout(this);
        finalBracketRoundHeaders_ll.setOrientation(LinearLayout.HORIZONTAL);

        final ViewGroup tournamentLayout = (ViewGroup) findViewById(R.id.tournamentLayout);

        final LinearLayout winnerAndLoserBracket = new LinearLayout(this);
        winnerAndLoserBracket.setOrientation(LinearLayout.VERTICAL);

        winnerAndLoserBracket.addView(winnerBracketRoundHeaders_ll);
        winnerAndLoserBracket.addView(winnerBracket_ll);
        winnerAndLoserBracket.addView(loserBracketRoundHeaders_ll);
        winnerAndLoserBracket.addView(loserBracket_ll);

        tournamentLayout.addView(winnerAndLoserBracket);

        final LinearLayout finalBracketWithRoundHeader = new LinearLayout(this);
        finalBracketWithRoundHeader.setOrientation(LinearLayout.VERTICAL);

        finalBracketWithRoundHeader.addView(finalBracketRoundHeaders_ll);
        finalBracketWithRoundHeader.addView(finalBracket_ll);

        tournamentLayout.addView(finalBracketWithRoundHeader);

    }

    protected ViewGroup getProperViewGroup(final int roundGroupIndex) {
        return roundGroupIndex == 0 ? winnerBracket_ll : roundGroupIndex == 1 ? loserBracket_ll : finalBracket_ll;
    }

    protected void displayRoundHeaders() {
        displayRoundHeaders(winnerBracketRoundHeaders_ll, 0);
        displayRoundHeaders(loserBracketRoundHeaders_ll, 1);
        displayRoundHeaders(finalBracketRoundHeaders_ll, 2);
    }

    protected int getMatchUpVerticalPadding(final int roundGroupIndex, final int roundIndex, final int basePaddingInPixel) {
        if (roundGroupIndex == 0)
            return super.getMatchUpVerticalPadding(roundGroupIndex, roundIndex, basePaddingInPixel);
        else if (roundGroupIndex == 1)
            return (new Double(basePaddingInPixel * (Math.pow(2d, ((roundIndex + 1) + 1) / 2) - 1))).intValue();
        else
            return super.getMatchUpVerticalPadding(0, tournament.getTotalRounds(0) - 1, basePaddingInPixel);
    }

    protected void buildTournamentAndDisplay() {
        super.buildTournamentAndDisplay();

        drawBracketLines();
    }

    protected void drawBracketLines() {
        final List<List<List<Point>>> point_3dArr = getPoint_3dArr();

        final int matchUpIndicatorWidth = (int) getResources().getDimension(R.dimen.matchUpIndicator_width);
        final int textHeight = (int) getResources().getDimension(R.dimen.participant_tv_height);
        final int textWidth = (int) getResources().getDimension(R.dimen.participant_tv_width) + matchUpIndicatorWidth;

        final int hPadding = textHeight;
        final int extraSpaceForRoundHeader = textHeight;

        for (int roundIndex = 0; roundIndex < tournament.getTotalRounds(1) - 1; roundIndex++) {
            final List<List<Point>> point_2dArr = new ArrayList<>();
            final Round round = tournament.getRoundAt(1, roundIndex);

            for (int matchUpIndex = 0; matchUpIndex < round.getTotalMatchUps(); matchUpIndex++) {

                final List<Point> point_1dArr = new ArrayList<>();

                final int x = hPadding * (2 * roundIndex + 1) + textWidth * (roundIndex + 1);
                final int verticalPadding = (getMatchUpVerticalPadding(1, roundIndex, textHeight) + textHeight);
                final int y = verticalPadding * (2 * matchUpIndex + 1) + tournament.getRoundGroupAt(0).get(0).getTotalMatchUps() * (textHeight * 4) + extraSpaceForRoundHeader * 2;

                point_1dArr.add(new Point(x - matchUpIndicatorWidth, y));
                if (roundIndex % 2 == 0)
                    point_1dArr.add(new Point(x + hPadding * 2, y));
                else
                    point_1dArr.add(new Point(x + hPadding * 2, y + (textHeight * (int) Math.pow(2, (roundIndex / 2 + 1))) * (matchUpIndex % 2 == 0 ? 1 : -1)));
                point_2dArr.add(point_1dArr);
            }
            point_3dArr.add(point_2dArr);
        }

        final int lastRoundIndex_loserBracket = tournament.getTotalRounds(1) - 1;
        final int lastRoundIndex_winnerBracket = tournament.getTotalRounds(0) - 1;

        { //winner to final bracket
            final List<Point> point_1dArr = new ArrayList<>();

            final int x1 = hPadding * (2 * lastRoundIndex_winnerBracket + 1) + textWidth * (lastRoundIndex_winnerBracket + 1) - matchUpIndicatorWidth;
            final int y1 = getMatchUpVerticalPadding(0, lastRoundIndex_winnerBracket, textHeight) + textHeight + extraSpaceForRoundHeader;

            final int x2 = hPadding * (2 * lastRoundIndex_loserBracket + 1) + textWidth * (lastRoundIndex_loserBracket + 1) + hPadding * 2;
            final int y2 = textHeight * (int) (Math.pow(2, tournament.getTotalRounds(0))) + extraSpaceForRoundHeader;

            point_1dArr.add(new Point(x1, y1));
            point_1dArr.add(new Point(x2, y2));

            final List<List<Point>> point_2dArr = new ArrayList<>();

            point_2dArr.add(point_1dArr);
            point_3dArr.add(point_2dArr);
        }

        { //loser to final bracket
            final List<Point> point_1dArr = new ArrayList<>();

            final int x1 = hPadding * (2 * lastRoundIndex_loserBracket + 1) + textWidth * (lastRoundIndex_loserBracket + 1) - matchUpIndicatorWidth;
            final int verticalPadding = (getMatchUpVerticalPadding(1, lastRoundIndex_loserBracket, textHeight) + textHeight);
            final int y1 = verticalPadding + tournament.getRoundGroupAt(0).get(0).getTotalMatchUps() * (textHeight * 4) + extraSpaceForRoundHeader * 2;

            final int x2 = x1 + matchUpIndicatorWidth + hPadding * 2;
            final int y2 = textHeight * (int) (Math.pow(2, tournament.getTotalRounds(0))) + extraSpaceForRoundHeader;
            point_1dArr.add(new Point(x1, y1));
            point_1dArr.add(new Point(x2, y2));

            final List<List<Point>> point_2dArr = new ArrayList<>();

            point_2dArr.add(point_1dArr);
            point_3dArr.add(point_2dArr);
        }

        { //final match up 1 to final match up 2
            final List<Point> point_1dArr = new ArrayList<>();

            final int x1 = hPadding * (2 * lastRoundIndex_loserBracket + 1) + textWidth * (lastRoundIndex_loserBracket + 1) + hPadding * 2 + textWidth - matchUpIndicatorWidth;
            final int y1 = getMatchUpVerticalPadding(0, lastRoundIndex_winnerBracket, textHeight) + textHeight + extraSpaceForRoundHeader;

            final int x2 = x1 + matchUpIndicatorWidth + hPadding * 2;
            final int y2 = y1;
            point_1dArr.add(new Point(x1, y1));
            point_1dArr.add(new Point(x2, y2));

            final List<List<Point>> point_2dArr = new ArrayList<>();

            point_2dArr.add(point_1dArr);
            point_3dArr.add(point_2dArr);
        }
        tournamentLayout.drawEntireTournament(point_3dArr);
    }

    protected String getFullRoundTitle(final Round round, final int roundGroupIndex) {
        return getString(R.string.roundTitleForDifferentRoundGroup, round.getTitle(), getRoundBracketTitle(roundGroupIndex));
    }

    protected String getFullMatchUpTitle(final int roundGroupIndex, final int roundIndex, final int matchUpIndex) {
        final Round round = tournament.getRoundAt(roundGroupIndex, roundIndex);
        final MatchUp matchUp = tournament.getMatchUp(roundGroupIndex, roundIndex, matchUpIndex);
        final Participant participant1 = matchUp.getParticipant1();
        final Participant participant2 = matchUp.getParticipant2();


        if (participant1.isNormal() && participant2.isNormal())
            return getString(R.string.participant1VsParticipant2ForDifferentRoundGroup, participant1.getDisplayName(), participant2.getDisplayName(), round.getTitle(), getRoundBracketTitle(roundGroupIndex));
        else if (participant1.isNormal() && participant2.isBye())
            return getString(R.string.participantWithAByeForDifferentRoundGroup, participant1.getDisplayName(), round.getTitle(), getRoundBracketTitle(roundGroupIndex));
        else if (participant1.isBye() && participant2.isNormal())
            return getString(R.string.participantWithAByeForDifferentRoundGroup, participant2.getDisplayName(), round.getTitle(), getRoundBracketTitle(roundGroupIndex));
        else if (participant1.isNormal() && participant2.isNull())
            return getString(R.string.participantVsNoOneForDifferentRoundGroup, participant1.getDisplayName(), round.getTitle(), getRoundBracketTitle(roundGroupIndex));
        else if (participant1.isNull() && participant2.isNormal())
            return getString(R.string.participantVsNoOneForDifferentRoundGroup, participant2.getDisplayName(), round.getTitle(), getRoundBracketTitle(roundGroupIndex));
        return getString(R.string.emptyMatchUpForDifferentRoundGroup, matchUpIndex + 1, round.getTitle(), getRoundBracketTitle(roundGroupIndex));
    }

    private String getRoundBracketTitle(final int roundGroupIndex) {
        switch (roundGroupIndex) {
            case 0:
                return getString(R.string.winnersBracket);
            case 1:
                return getString(R.string.losersBracket);
            case 2:
            default:
                return getString(R.string.finalsBracket);
        }
    }
}
