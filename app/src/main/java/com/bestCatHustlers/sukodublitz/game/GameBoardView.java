package com.bestCatHustlers.sukodublitz.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class GameBoardView extends View {
    class Cell {
        public int row;
        public int column;

        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    //region Properties

    public GameBoardView.Delegate delegate;

    private Paint thickLinePaint;
    private Paint thinLinePaint;
    private Paint textPaint;

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

        final Paint.Style textStyle = Paint.Style.FILL_AND_STROKE;
        final int textColor = Color.BLACK;
        final float textSize = 72f;

        final int selectedCellColor = Color.GRAY;
        final int relatedCellColor = Color.LTGRAY;
        final int defaultCellColor = Color.WHITE;

        final float boardToDisplayRatio = 0.9f;
        final int boardSize = 9;
        final int boardSqrtSize = 3;
    }

    //endregion

    //region GameBoardView.Delegate

    interface Delegate {
        int valueForCell(int row, int column);

        void gameBoardViewDidClick(int row, int column);
    }

    //endregion

    //region LifeCycle

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

        textPaint = new Paint();
        textPaint.setStyle(constants.textStyle);
        textPaint.setColor(constants.textColor);
        textPaint.setTextSize(constants.textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        highlightCells(canvas);
        drawLines(canvas);
        printCells(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final int displayWidth = size.x;
        final int displayHeight = size.y;
        final int maximumSize = (int)Math.round(
                Math.min(displayWidth, displayHeight) * constants.boardToDisplayRatio);

        setMeasuredDimension(maximumSize, maximumSize);
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

    //endregion

    //region Public

    public Cell getSelectedCell() {
        if (selectedRow >= 0 && selectedColumn >= 0) {
            Cell cell = new Cell(selectedRow, selectedColumn);

            return cell;
        } else {
            return null;
        }
    }

    public void setSelectedCell(int row, int column) {
        selectedRow = row;
        selectedColumn = column;

        invalidate();
    }

    public void deselectCells() {
        selectedRow = -1;
        selectedColumn = -1;

        invalidate();
    }

    //endregion

    //region Private

    private void drawLines(Canvas canvas) {
        final float width = (float)getWidth();
        final float height = (float)getHeight();

        cellPixelSize = width / constants.boardSize;

        canvas.drawRect(0f, 0f, width, height, thickLinePaint);

        for (int i = 1; i <= constants.boardSize; ++i) {
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

    private void highlightCells(Canvas canvas) {
        for (int row = 0; row < constants.boardSize; ++row) {
            for (int column = 0; column < constants.boardSize; ++column) {
                if (row == selectedRow && column == selectedColumn) {
                    fillCell(canvas, row, column, constants.selectedCellColor);
                } else  if (row == selectedRow || column == selectedColumn) {
                    fillCell(canvas, row, column, constants.relatedCellColor);
                } else {
                    fillCell(canvas, row, column, constants.defaultCellColor);
                }
            }
        }
    }

    private void fillCell(Canvas canvas, int row, int column, int color) {
        Paint cellPaint = new Paint();

        cellPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        cellPaint.setColor(color);

        canvas.drawRect(
                column * cellPixelSize,
                row * cellPixelSize,
                (column + 1) * cellPixelSize,
                (row + 1) * cellPixelSize,
                cellPaint);
    }

    private void printCells(Canvas canvas) {
        if (delegate == null) return;

        for (int row = 0; row < constants.boardSize; ++row) {
            for (int column = 0; column < constants.boardSize; ++column) {
                int value = delegate.valueForCell(row, column);

                if (value < 1 || value > 9) continue;

                String valueString = String.valueOf(value);
                Rect textBounds = new Rect();

                textPaint.getTextBounds(valueString, 0, valueString.length(), textBounds);

                float textWidth = textPaint.measureText(valueString);
                float textHeight = textPaint.measureText(valueString);

                canvas.drawText(
                        valueString,
                        (column * cellPixelSize) + (cellPixelSize / 2) - (textWidth / 2),
                        (row * cellPixelSize) + (cellPixelSize) - (textHeight),
                        textPaint);
            }
        }
    }

    private void handleTouchEvent(Float x, Float y) {
        if (delegate != null) {
            int row = (int) Math.floor(y / cellPixelSize);
            int column = (int) Math.floor(x / cellPixelSize);

            delegate.gameBoardViewDidClick(row, column);
        }
    }

    //endregion
}
