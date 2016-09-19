package com.dgnt.quickTournamentMaker.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Owner on 3/19/2016.
 */
public class TournamentLayout extends LinearLayout {


    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint;
    Context context;
    private Paint mPaint;

    public TournamentLayout(final Context context) {
        super(context);
        init();
    }

    public TournamentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(2);
        setWillNotDraw(false);
    }


    private List<List<List<Point>>> pointArray_3d;

    public void drawEntireTournament(final List<List<List<Point>>> pointArray_3d) {

        this.pointArray_3d = pointArray_3d;

        invalidate();

    }

    public void onDraw(final Canvas canvas) {
        if (pointArray_3d == null)
            return;

        for (int i = 0; i < pointArray_3d.size(); i++) {
            for (int j = 0; j < pointArray_3d.get(i).size(); j++) {

                final Point startPoint = pointArray_3d.get(i).get(j).get(0);
                final Point endPoint = pointArray_3d.get(i).get(j).get(pointArray_3d.get(i).get(j).size() - 1);

                final int midPointX = (startPoint.x + endPoint.x) / 2;
                final int cornerRadius = 15;

                canvas.drawLine(startPoint.x, startPoint.y, midPointX - cornerRadius, startPoint.y, mPaint);

                final Path mPath = new Path();
                mPath.moveTo(midPointX - cornerRadius,startPoint.y);
                mPath.quadTo(midPointX, startPoint.y, midPointX, startPoint.y + cornerRadius * (endPoint.y > startPoint.y ? 1 : endPoint.y < startPoint.y ? -1 : 0));

                canvas.drawLine(midPointX, startPoint.y + cornerRadius * (endPoint.y > startPoint.y ? 1 : endPoint.y < startPoint.y ? -1 : 0), midPointX, endPoint.y, mPaint);
                canvas.drawLine(midPointX, endPoint.y, endPoint.x, endPoint.y, mPaint);

                canvas.drawPath(mPath, mPaint);
            }

        }
    }


}
