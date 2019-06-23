package com.bestCatHustlers.sukodublitz.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class GameBoardView extends View {
    private Paint thickLinePaint;
    private Paint thinLinePaint;
    private Constants constants;

    private float cellPixelSize = 0f;

    private int selectedRow = -1;
    private int selectedColumn = -1;

    private class Constants {
        final Paint.Style thickLineStyle = Paint.Style.STROKE;
        final int thickLineColor = Color.RED;
        final float thickLineWidth = 4f;

        final Paint.Style thinLineStyle = Paint.Style.STROKE;
        final int thinLineColor = Color.BLUE;
        final float thinLineWidth = 2f;

        final float boardToDisplayRatio = 0.9f;
        final int boardSize = 9;
        final int boardSqrtSize = 3;
    }

    public GameBoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        constants = new Constants();

        thickLinePaint = new Paint();
        thickLinePaint.setStyle(constants.thickLineStyle);
        thickLinePaint.setColor(constants.thickLineColor);
        thickLinePaint.setStrokeWidth(constants.thickLineWidth);

        thinLinePaint = new Paint();
        thinLinePaint.setStyle(constants.thinLineStyle);
        thinLinePaint.setColor(constants.thinLineColor);
        thinLinePaint.setStrokeWidth(constants.thinLineWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLines(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final int displayWidth = size.x;
        final int displayHeight = size.y;
        final int maximumSize = (int)Math.round
                (Math.min(displayWidth, displayHeight)
                * constants.boardToDisplayRatio);

        setMeasuredDimension(maximumSize, maximumSize);
    }

    private void drawLines(Canvas canvas) {
        final float width = (float)getWidth();
        final float height = (float)getHeight();

        cellPixelSize = width / constants.boardSize;

        canvas.drawRect(0f, 0f, width, height, thickLinePaint);

        for (int i = 1; i<=constants.boardSize; ++i) {
            Paint paint = (i % constants.boardSqrtSize == 0) ? thickLinePaint : thinLinePaint;

            // Draw a vertical line.
            canvas.drawLine(
                    i * cellPixelSize,
                    0f,
                    i * cellPixelSize,
                    constants.boardSize * cellPixelSize,
                    paint);

            // Draw a horizontal line.
            canvas.drawLine(
                    0f,
                    i * cellPixelSize,
                    constants.boardSize * cellPixelSize,
                    i * cellPixelSize,
                    paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleTouchEvent(event.getX(), event.getY());
                return true;
        }

        return false;
    }

    private void handleTouchEvent(Float x, Float y) {
        selectedRow = (int)Math.floor(y / cellPixelSize);
        selectedColumn = (int)Math.floor(x / cellPixelSize);
    }
}
