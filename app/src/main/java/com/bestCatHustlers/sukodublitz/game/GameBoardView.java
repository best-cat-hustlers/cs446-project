package com.bestCatHustlers.sukodublitz.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bestCatHustlers.sukodublitz.PuzzleGenerator;
import com.bestCatHustlers.sukodublitz.R;

public class GameBoardView extends CardView {
    //region Properties

    public GameBoardView.Delegate delegate;

    private Paint thickLinePaint;
    private Paint thinLinePaint;
    private Paint textPaint;

    private Constants constants;

    private float cellPixelSize = 0f;

    private int selectedRow = -1;
    private int selectedColumn = -1;

    private int[][] board = new int[PuzzleGenerator.GRID_SIZE][PuzzleGenerator.GRID_SIZE];
    private String[][] cellOwners = new String[PuzzleGenerator.GRID_SIZE][PuzzleGenerator.GRID_SIZE];

    private class Constants {
        final Paint.Style thickLineStyle = Paint.Style.STROKE;
        final int thickLineColor = Color.DKGRAY;
        final float thickLineWidth = 4f;

        final Paint.Style thinLineStyle = Paint.Style.STROKE;
        final int thinLineColor = Color.GRAY;
        final float thinLineWidth = 2f;

        final Paint.Style textStyle = Paint.Style.FILL_AND_STROKE;
        final int defaultTextColor = Color.BLACK;
        final int player1TextColor = getResources().getColor(R.color.primaryDarkColor);
        final int player2TextColor = getResources().getColor(R.color.secondaryDarkColor);
        final float textSizeToCellRatio = 0.5f;

        final int selectedCellColor = Color.GRAY;
        final int relatedCellColor = Color.LTGRAY;
        final int defaultCellColor = Color.WHITE;

        final float boardToParentRatio = 0.9f; // You'll want this to show the drop shadow without clipping.
        final int boardSize = PuzzleGenerator.GRID_SIZE;
        final int boardSqrtSize = (int) Math.round(Math.sqrt((double) PuzzleGenerator.GRID_SIZE));

        final float cardElevation = 15f;
        final float cardRadiusFactor =0.25f;
    }

    //endregion

    //region GameBoardView.Delegate

    interface Delegate {
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

        int maximumSize = (int) Math.round(Math.min(getMeasuredWidth(), getMeasuredHeight()) * constants.boardToParentRatio);

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

    public void selectCell(int row, int column) {
        selectedRow = row;
        selectedColumn = column;

        invalidate();
    }

    public void printBoard(int[][] board, String[][] cellOwners) {
        this.board = board;
        this.cellOwners = cellOwners;

        invalidate();
    }

    //endregion

    //region Private

    private void drawLines(Canvas canvas) {
        final float width = (float)getWidth();

        cellPixelSize = width / constants.boardSize;

        float cornerRadius = cellPixelSize * constants.cardRadiusFactor;

        setRadius(cornerRadius);
        setCardElevation(constants.cardElevation);

        for (int i = 1; i < constants.boardSize; ++i) {
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
        textPaint.setTextSize(cellPixelSize * constants.textSizeToCellRatio);

        for (int row = 0; row < constants.boardSize; ++row) {
            for (int column = 0; column < constants.boardSize; ++column) {
                int value = board[row][column];
                int textColor = getTextColor(cellOwners[row][column]);

                if (value < 1 || value > 9) continue;

                String valueString = String.valueOf(value);
                Rect textBounds = new Rect();

                textPaint.getTextBounds(valueString, 0, valueString.length(), textBounds);
                textPaint.setColor(textColor);

                float textWidth = textPaint.measureText(valueString);
                float textHeight = textBounds.height();

                canvas.drawText(
                        valueString,
                        (column * cellPixelSize) + (cellPixelSize / 2) - (textWidth / 2),
                        (row * cellPixelSize) + (cellPixelSize / 2) + (textHeight / 2),
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

    // TODO: Get cell owner strings get something like an enum (presenter can decide on the enum).
    private int getTextColor(String cellOwner) {
        switch (cellOwner) {
            case "1":
                return constants.player1TextColor;

            case "2":
                return constants.player2TextColor;
        }

        return constants.defaultTextColor;
    }

    //endregion
}
