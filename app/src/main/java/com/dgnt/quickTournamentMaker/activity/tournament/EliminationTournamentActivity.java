package com.dgnt.quickTournamentMaker.activity.tournament;

import android.graphics.Point;

import com.dgnt.quickTournamentMaker.activity.R;
import com.dgnt.quickTournamentMaker.model.tournament.Round;
import com.dgnt.quickTournamentMaker.model.tournament.EliminationTournament;

import java.util.ArrayList;
import java.util.List;

public class EliminationTournamentActivity extends TournamentActivity {


    protected void createTournament() {

        tournament = new EliminationTournament();
    }

    protected int getMatchUpVerticalPadding(final int roundGroupIndex, final int roundIndex, final int basePaddingInPixel) {
        return (new Double(basePaddingInPixel * (Math.pow(2d, (roundIndex + 1)) - 1))).intValue();
    }

    protected void buildTournamentAndDisplay() {
        super.buildTournamentAndDisplay();

        drawBracketLines();

    }

    protected void drawBracketLines() {
        final List<List<List<Point>>> point_3dArr = getPoint_3dArr();
        tournamentLayout.drawEntireTournament(point_3dArr);
    }

    protected List<List<List<Point>>> getPoint_3dArr() {
        final List<List<List<Point>>> point_3dArr = new ArrayList<>();

        final int matchUpIndicatorWidth = (int) getResources().getDimension(R.dimen.matchUpIndicator_width);
        final int textHeight = (int) getResources().getDimension(R.dimen.participant_tv_height);
        final int textWidth = (int) getResources().getDimension(R.dimen.participant_tv_width) + matchUpIndicatorWidth;

        final int hPadding = textHeight;
        final int extraSpaceForRoundHeader = textHeight ;

        for (int roundIndex = 0; roundIndex < tournament.getTotalRounds(0) - 1; roundIndex++) {
            final List<List<Point>> point_2dArr = new ArrayList<>();
            final Round round = tournament.getRoundAt(0, roundIndex);

            for (int matchUpIndex = 0; matchUpIndex < round.getTotalMatchUps(); matchUpIndex++) {

                final List<Point> point_1dArr = new ArrayList<>();

                final int x = hPadding * (2 * roundIndex + 1) + textWidth * (roundIndex + 1);
                final int y = (getMatchUpVerticalPadding(0, roundIndex, textHeight) + textHeight) * (2 * matchUpIndex + 1)+extraSpaceForRoundHeader;

                point_1dArr.add(new Point(x - matchUpIndicatorWidth, y));
                point_1dArr.add(new Point(x + hPadding * 2, y + (textHeight * (int) Math.pow(2, (roundIndex + 1))) * (matchUpIndex % 2 == 0 ? 1 : -1)));
                point_2dArr.add(point_1dArr);
            }
            point_3dArr.add(point_2dArr);
        }
        return point_3dArr;
    }



}
