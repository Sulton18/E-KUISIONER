package com.example.e_kuisioner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BarChartView3 extends View {
    private Paint barPaint;
    private Paint textPaint;
    private Paint textPaintBold;
    private Paint linePaint;

    private int tokopediaWarnaValue;
    private int tokopediaNavigasiValue;
    private int shopeeWarnaValue;
    private int shopeeNavigasiValue;
    private int tokopediaPercentageWarna;
    private int tokopediaPercentageNavigasi;
    private int shopeePercentageWarna;
    private int shopeePercentageNavigasi;

    public BarChartView3(Context context, AttributeSet attrs) {
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

        textPaintBold = new Paint(textPaint);
        textPaintBold.setTypeface(android.graphics.Typeface.create(textPaint.getTypeface(), android.graphics.Typeface.BOLD));

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);
    }

    public void setPercentages(int tokopediaWarnaValue, int tokopediaNavigasiValue, int tokopediaPercentageWarna, int tokopediaPercentageNavigasi, int shopeeWarnaValue, int shopeeNavigasiValue, int shopeePercentageWarna, int shopeePercentageNavigasi) {
        this.tokopediaWarnaValue = tokopediaWarnaValue;
        this.tokopediaNavigasiValue = tokopediaNavigasiValue;
        this.shopeeWarnaValue = shopeeWarnaValue;
        this.shopeeNavigasiValue = shopeeNavigasiValue;
        this.tokopediaPercentageWarna = tokopediaPercentageWarna;
        this.tokopediaPercentageNavigasi = tokopediaPercentageNavigasi;
        this.shopeePercentageWarna = shopeePercentageWarna;
        this.shopeePercentageNavigasi = shopeePercentageNavigasi;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / 12;

        drawPercentageLines(canvas, width, height);
        drawBars(canvas, height, barWidth);
        drawLabels(canvas, height, barWidth);
    }

    private void drawPercentageLines(Canvas canvas, int width, int height) {
        for (int i = 0; i <= 5; i++) {
            int y = height - (height * i / 5);
            canvas.drawLine(0, y, width, y, linePaint);
            canvas.drawText((i * 25) + "%", 10, y - 10, textPaint);
        }
    }

    private void drawBars(Canvas canvas, int height, int barWidth) {
        // Tokopedia Warna bar
        barPaint.setColor(Color.GREEN);
        int tokopediaBarHeightWarna = height * tokopediaPercentageWarna / 125;
        canvas.drawRect(barWidth * 3, height - tokopediaBarHeightWarna, barWidth * 4, height, barPaint);
        canvas.drawText(tokopediaPercentageWarna + "%", barWidth * 3 + (barWidth / 2) - 30, height - tokopediaBarHeightWarna - 10, textPaint);

        // Shopee Warna bar
        barPaint.setColor(Color.rgb(255, 165, 0));
        int shopeeBarHeightWarna = height * shopeePercentageWarna / 125;
        canvas.drawRect(barWidth * 4, height - shopeeBarHeightWarna, barWidth * 5, height, barPaint);
        canvas.drawText(shopeePercentageWarna + "%", barWidth * 4 + (barWidth / 2) - 30, height - shopeeBarHeightWarna - 10, textPaint);

        // Tokopedia Navigasi bar
        barPaint.setColor(Color.rgb(0, 165, 10));
        int tokopediaBarHeightNavigasi = height * tokopediaPercentageNavigasi / 125;
        canvas.drawRect(barWidth * 7, height - tokopediaBarHeightNavigasi, barWidth * 8, height, barPaint);
        canvas.drawText(tokopediaPercentageNavigasi + "%", barWidth * 7 + (barWidth / 2) - 30, height - tokopediaBarHeightNavigasi - 10, textPaint);

        // Shopee Navigasi bar
        barPaint.setColor(Color.rgb(255, 0, 0));
        int shopeeBarHeightNavigasi = height * shopeePercentageNavigasi / 125;
        canvas.drawRect(barWidth * 8, height - shopeeBarHeightNavigasi, barWidth * 9, height, barPaint);
        canvas.drawText(shopeePercentageNavigasi + "%", barWidth * 8 + (barWidth / 2) - 30, height - shopeeBarHeightNavigasi - 10, textPaint);
    }

    private void drawLabels(Canvas canvas, int height, int barWidth) {
        canvas.drawText("Warna", barWidth * 3f, height - 10, textPaintBold);
        canvas.drawText("Navigasi", barWidth * 7f, height - 10, textPaintBold);
    }
}
