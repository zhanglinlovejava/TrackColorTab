package com.trackcolorview.test.trackcolorview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.trackcolorview.test.R;
import com.trackcolorview.test.util.ScreenUtil;

public class ColorTrackView extends View {

    private static final int DIRECTION_LEFT = 0;
    private static final int DIRECTION_RIGHT = 1;
    //以下定义一些默认的变量
    private int mDirection = DIRECTION_LEFT;
    private int mTextStartX;
    private Paint mPaint;
    private int mTextOriginColor;//字体改变前的颜色
    private int mTextChangeColor;//字体改变中的颜色
    private String mText = "xx";
    private int mTextWidth;
    private float mProgress; // 进度，从0到1之间取值
    private Rect mTextBound = new Rect();

    public ColorTrackView(Context context, int textSize, int changeColor, int originColor) {
        super(context);
        mTextChangeColor = changeColor;
        mTextOriginColor = originColor;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(textSize);
    }


    private void measureText() {
        mTextWidth = (int) mPaint.measureText(mText);
        //这个函数可以获取最小的包裹文字矩形，赋值到mTextBound
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
        measureText();
        int mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mTextStartX = mRealWidth / 2 - mTextWidth / 2;

    }

    private int measureHeight(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mTextBound.height();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result + getPaddingTop() + getPaddingBottom();
    }

    private int measureWidth(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mTextBound.width();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result + getPaddingLeft() + getPaddingRight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDirection == DIRECTION_LEFT) {
            drawChangeLeft(canvas);
            drawOriginLeft(canvas);
        } else if (mDirection == DIRECTION_RIGHT) {
            drawOriginRight(canvas);
            drawChangeRight(canvas);
        } else {
            System.out.println("ColorTrackView direction error");
        }

    }

    private void drawChangeRight(Canvas canvas) {
        drawText(canvas, mTextChangeColor, (int) (mTextStartX + (1 - mProgress)
                * mTextWidth), mTextStartX + mTextWidth);
    }

    private void drawOriginRight(Canvas canvas) {
        drawText(canvas, mTextOriginColor, mTextStartX,
                (int) (mTextStartX + (1 - mProgress) * mTextWidth));
    }

    private void drawChangeLeft(Canvas canvas) {
        drawText(canvas, mTextChangeColor, mTextStartX,
                (int) (mTextStartX + mProgress * mTextWidth));
    }

    private void drawOriginLeft(Canvas canvas) {
        drawText(canvas, mTextOriginColor, (int) (mTextStartX + mProgress
                * mTextWidth), mTextStartX + mTextWidth);
    }

    private void drawText(Canvas canvas, int color, int startX, int endX) {
        mPaint.setColor(color);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());
        canvas.drawText(mText, mTextStartX, getMeasuredHeight()
                / 2 + mTextBound.height() / 2 - 5, mPaint);
        canvas.restore();
    }

//    public int getDirection() {
//        return mDirection;
//    }

    public void setDirection(int mDirection) {
        this.mDirection = mDirection;
        invalidate(); //重新改变方向，需要重绘
    }

//    public int getTextOriginColor() {
//        return mTextOriginColor;
//    }

    public void setTextOriginColor(int mTextOriginColor) {
        this.mTextOriginColor = mTextOriginColor;
        invalidate(); //改变原始颜色，需要重绘
    }

//    public int getTextChangeColor() {
//        return mTextChangeColor;
//    }

    public void setTextChangeColor(int mTextChangeColor) {
        this.mTextChangeColor = mTextChangeColor;
        invalidate();//改变变化中的颜色，需要重绘
    }

//    public String getText() {
//        return mText;
//    }

    public void setText(String mText) {
        this.mText = mText;
    }

//    public int getTextSize() {
//        return mTextSize;
//    }

    public void setTextSize(int mTextSize) {
        requestLayout();//改变字体大小，重新布局
        invalidate();//改变字体大小，需要重绘
    }

//    public float getProgress() {
//        return mProgress;
//    }

    public void setProgress(float mProgress) {
        this.mProgress = mProgress;
        invalidate();//改变进度，需要重绘，实现字体渐变的效果
    }

}
