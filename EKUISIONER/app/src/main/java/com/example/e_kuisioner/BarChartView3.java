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
        int barWidth = width / 8; // Divide the canvas into 8 sections, 2 sections per bar

        // Draw horizontal percentage lines and labels
        for (int i = 0; i <= 5; i++) {
            int y = height - (height * i / 5);
            canvas.drawLine(0, y, width, y, linePaint);
            canvas.drawText((i * 25) + "%", 10, y - 10, textPaint);
        }

        // Tokopedia Warna bar
        barPaint.setColor(Color.GREEN);
        int tokopediaBarHeightWarna = height * tokopediaPercentageWarna / 125;
        canvas.drawRect(barWidth, height - tokopediaBarHeightWarna, barWidth * 2, height, barPaint);
        canvas.drawText(tokopediaPercentageWarna + "%", barWidth + (barWidth / 2) - 30, height - tokopediaBarHeightWarna - 10, textPaint);

        // Tokopedia Navigasi bar
        float[] tokopedia = new float[3];
        Color.RGBToHSV(0, 165, 10, tokopedia);
        barPaint.setColor(Color.HSVToColor(tokopedia));
        int tokopediaBarHeightNavigasi = height * tokopediaPercentageNavigasi / 125;
        canvas.drawRect(barWidth * 3, height - tokopediaBarHeightNavigasi, barWidth * 4, height, barPaint);
        canvas.drawText(tokopediaPercentageNavigasi + "%", barWidth * 3 + (barWidth / 2) - 30, height - tokopediaBarHeightNavigasi - 10, textPaint);

        // Shopee Warna bar
        float[] hsv = new float[3];
        Color.RGBToHSV(255, 165, 0, hsv);
        barPaint.setColor(Color.HSVToColor(hsv));
        int shopeeBarHeightWarna = height * shopeePercentageWarna / 125;
        canvas.drawRect(barWidth * 5, height - shopeeBarHeightWarna, barWidth * 6, height, barPaint);
        canvas.drawText(shopeePercentageWarna + "%", barWidth * 5 + (barWidth / 2) - 30, height - shopeeBarHeightWarna - 10, textPaint);

        // Shopee Navigasi bar
        float[] shopee = new float[3];
        Color.RGBToHSV(255, 0, 0, shopee);
        barPaint.setColor(Color.HSVToColor(shopee));
        int shopeeBarHeightNavigasi = height * shopeePercentageNavigasi / 125;
        canvas.drawRect(barWidth * 7, height - shopeeBarHeightNavigasi, barWidth * 8, height, barPaint);
        canvas.drawText(shopeePercentageNavigasi + "%", barWidth * 7 + (barWidth / 2) - 30, height - shopeeBarHeightNavigasi - 10, textPaint);

        // Draw labels
        textPaint.setColor(Color.BLACK);
        canvas.drawText("Warna", barWidth, height - 10, textPaint);
        canvas.drawText("Navigasi", barWidth * 3, height - 10, textPaint);
        canvas.drawText("Warna", barWidth * 5, height - 10, textPaint);
        canvas.drawText("Navigasi", barWidth * 7, height - 10, textPaint);
    }
}
