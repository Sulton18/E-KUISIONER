package com.example.e_kuisioner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class BarChartView2 extends View {
    private Paint barPaint;
    private Paint textPaint;
    private Paint linePaint;
    private List<UserPercentage> userPercentages;

    public BarChartView2(Context context, AttributeSet attrs) {
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
        textPaint.setTextSize(30);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);
    }

    public void setUserPercentages(List<UserPercentage> userPercentages) {
        this.userPercentages = userPercentages;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int userCount = userPercentages != null ? userPercentages.size() : 0;
        int barWidth = width / (userCount * 6);

        for (int i = 0; i <= 5; i++) {
            int y = height - (height * i / 5);
            canvas.drawLine(0, y, width, y, linePaint);
            canvas.drawText((i * 25) + "%", 10, y - 10, textPaint);
        }

        if (userPercentages != null) {
            for (int i = 0; i < userCount; i++) {
                UserPercentage userPercentage = userPercentages.get(i);
                int tokopediaBarHeight = height * userPercentage.tokopediaPercentage / 125;
                int shopeeBarHeight = height * userPercentage.shopeePercentage / 125;

                barPaint.setColor(Color.GREEN);
                canvas.drawRect((i * 6 + 1) * barWidth, height - tokopediaBarHeight, (i * 6 + 3) * barWidth, height, barPaint);
                canvas.drawText(userPercentage.tokopediaPercentage + "%", (i * 6 + 1) * barWidth + barWidth / 2 - 20, height - tokopediaBarHeight - 10, textPaint);

                barPaint.setColor(Color.parseColor("#FFA500"));
                canvas.drawRect((i * 6 + 3) * barWidth, height - shopeeBarHeight, (i * 6 + 5) * barWidth, height, barPaint);
                canvas.drawText(userPercentage.shopeePercentage + "%", (i * 6 + 3) * barWidth + barWidth / 2 - 20, height - shopeeBarHeight - 10, textPaint);

                textPaint.setColor(Color.BLACK);
                canvas.drawText(userPercentage.userName, (i * 6 + 2) * barWidth + barWidth / 2 - 30, height - 10, textPaint);
            }
        }
    }
}
