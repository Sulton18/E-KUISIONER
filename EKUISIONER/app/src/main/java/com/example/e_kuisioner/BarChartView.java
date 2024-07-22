package com.example.e_kuisioner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BarChartView extends View {
    private Paint barPaint;
    private Paint textPaint;
    private Paint linePaint;
    private int tokopediaPercentage;
    private int shopeePercentage;
    private int tokopediaValue;
    private int shopeeValue;

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint = new Paint();
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);
    }

    public void setPercentages(int tokopediaValue, int tokopediaPercentage, int shopeeValue, int shopeePercentage) {
        this.tokopediaValue = tokopediaValue;
        this.tokopediaPercentage = tokopediaPercentage;
        this.shopeeValue = shopeeValue;
        this.shopeePercentage = shopeePercentage;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / 4;

        for (int i = 0; i <= 5; i++) {
            int y = height - (height * i / 5);
            canvas.drawLine(0, y, width, y, linePaint);
            canvas.drawText((i * 25) + "%", 10, y - 10, textPaint);
        }

        barPaint.setColor(Color.GREEN);
        int tokopediaBarHeight = height * tokopediaPercentage / 125;
        canvas.drawRect(barWidth, height - tokopediaBarHeight, barWidth * 2, height, barPaint);

        // Draw Shopee bar with orange color
        float[] hsv = new float[3];
        Color.RGBToHSV(255, 165, 0, hsv); // RGB values for orange
        barPaint.setColor(Color.HSVToColor(hsv));
        int shopeeBarHeight = height * shopeePercentage / 100;
        canvas.drawRect(barWidth * 3, height - shopeeBarHeight, barWidth * 4, height, barPaint);

        // Draw labels for bars
        textPaint.setColor(Color.BLACK);
        canvas.drawText("Tokopedia", barWidth + (barWidth / 2) - 50, height + 60, textPaint);

        textPaint.setColor(Color.HSVToColor(hsv));
        canvas.drawText("Shopee", barWidth * 3 + (barWidth / 2) - 50, height + 60, textPaint);

        // Draw percentage labels on top of bars
        textPaint.setColor(Color.BLACK);
        canvas.drawText(tokopediaPercentage + "%", barWidth + (barWidth / 2) - 30, height - tokopediaBarHeight - 10, textPaint);
        canvas.drawText(shopeePercentage + "%", barWidth * 3 + (barWidth / 2) - 30, height - shopeeBarHeight - 10, textPaint);

        // Draw bottom labels
        canvas.drawLine(0, height + 80, width, height + 80, linePaint);
        textPaint.setColor(Color.BLACK);
        canvas.drawText("TOKOPEDIA", barWidth + (barWidth / 2) - 50, height + 120, textPaint);
        canvas.drawText("SHOPEE", barWidth * 3 + (barWidth / 2) - 50, height + 120, textPaint);
    }
}
