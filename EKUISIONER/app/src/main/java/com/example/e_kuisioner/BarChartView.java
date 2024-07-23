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

    private int tokopediaWarnaValue;
    private int tokopediaNavigasiValue;
    private int shopeeWarnaValue;
    private int shopeeNavigasiValue;
    private int tokopediaPercentageWarna;
    private int tokopediaPercentageNavigasi;
    private int shopeePercentageWarna;
    private int shopeePercentageNavigasi;

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
        int barWidth = width / 5;

        for (int i = 0; i <= 5; i++) {
            int y = height - (height * i / 5);
            canvas.drawLine(0, y, width, y, linePaint);
            canvas.drawText((i * 25) + "%", 10, y - 10, textPaint);
        }

        barPaint.setColor(Color.GREEN);
        int tokopediaBarHeight = height * tokopediaPercentage / 125;
        canvas.drawRect(barWidth, height - tokopediaBarHeight, barWidth * 2, height, barPaint);
        canvas.drawText(tokopediaPercentage + "%", barWidth + (barWidth / 2) - 30, height - tokopediaBarHeight - 10, textPaint);

        float[] hsv = new float[3];
        Color.RGBToHSV(255, 165, 0, hsv);
        barPaint.setColor(Color.HSVToColor(hsv));
        int shopeeBarHeight = height * shopeePercentage / 125;
        canvas.drawRect(barWidth * 3, height - shopeeBarHeight, barWidth * 4, height, barPaint);
        canvas.drawText(shopeePercentage + "%", barWidth * 3 + (barWidth / 2) - 30, height - shopeeBarHeight - 10, textPaint);


        textPaint.setColor(Color.BLACK);
        canvas.drawText("Tokopedia", barWidth, height - 10, textPaint);
        canvas.drawText("Shopee", barWidth * 3, height - 10, textPaint);
    }
}
