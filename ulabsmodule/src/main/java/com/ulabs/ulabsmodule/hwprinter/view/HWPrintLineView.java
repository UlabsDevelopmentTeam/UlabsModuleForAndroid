package com.ulabs.ulabsmodule.hwprinter.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ulabs.ulabsmodule.R;


/**
 * Created by OH-Biz on 2018-02-01.
 */

public class HWPrintLineView extends View{
    private int lineThickness;
    private int lineColor;

    private int width;

    private Paint paint_background;

    public HWPrintLineView(Context context) {
        super(context);
        initView();
    }

    public HWPrintLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public HWPrintLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    private void initView(){
        paint_background = new Paint();
        paint_background.setColor(Color.BLACK);
        paint_background.setStyle(Paint.Style.FILL);
    }

    private void getAttrs(AttributeSet attributeSet){
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.HWPrintLineView);
        applyTypedArray(typedArray);
    }

    private void getAttrs(AttributeSet attributeSet, int defStyle){
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.HWPrintLineView, defStyle, 0);
        applyTypedArray(typedArray);
    }

    private void applyTypedArray(TypedArray typedArray){
        lineThickness = typedArray.getInt(R.styleable.HWPrintLineView_Linethickness, 1);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (widthMode){
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:{
                width = widthSize;
                break;
            }
            case MeasureSpec.UNSPECIFIED:{
                width = widthMeasureSpec;
                break;
            }
        }

        setMeasuredDimension(width, lineThickness);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
        invalidate();
    }
}
