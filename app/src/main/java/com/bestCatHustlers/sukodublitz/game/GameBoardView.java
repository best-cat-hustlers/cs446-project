package com.bestCatHustlers.sukodublitz.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GameBoardView extends View {
    private Paint thickLinePaint;
    private Constants constants;

    private class Constants {
        final Paint.Style thickLineStyle = Paint.Style.STROKE;
        final int thickLineColor = Color.BLACK;
        final float thickLineWidth = 4f;
    }

    public GameBoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        constants = new Constants();

        thickLinePaint = new Paint();
        thickLinePaint.setStyle(constants.thickLineStyle);
        thickLinePaint.setColor(constants.thickLineColor);
        thickLinePaint.setStrokeWidth(constants.thickLineWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final float right = (float)this.getMeasuredWidth();
        final float bottom = (float)this.getMeasuredHeight();

        canvas.drawRect(0f, 0f, right, bottom, thickLinePaint);
    }
}
