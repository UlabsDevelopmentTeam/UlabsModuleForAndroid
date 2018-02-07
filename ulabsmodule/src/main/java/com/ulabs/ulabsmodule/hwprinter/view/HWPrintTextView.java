package com.ulabs.ulabsmodule.hwprinter.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ulabs.ulabsmodule.R;


/**
 * Created by OH-Biz on 2018-02-01.
 */

public class HWPrintTextView extends View {
    private Paint paint_text;
    private Paint paint_background;

    private String text;
    private String textAlign;
    private int textSize;

    private int width;
    private int height;
    private Paint paint_size_calculating;
    private Rect textBound;

    private int colorMode = 0;
    private int textStyle = 0;

    public HWPrintTextView(Context context) {
        super(context);
        initView();
    }

    public HWPrintTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public HWPrintTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs,defStyleAttr);
    }

    private void initView(){
        paint_text = new Paint();

        paint_background = new Paint();
        paint_background.setColor(Color.WHITE);
        paint_background.setStyle(Paint.Style.FILL);
        paint_size_calculating = new Paint();
        textBound = new Rect();

    }

    private void getAttrs(AttributeSet attributeSet){
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.HWPrintTextView);
        applyTypedArray(typedArray);

    }

    private void getAttrs(AttributeSet attributeSet, int defStyle){
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.HWPrintTextView, defStyle, 0);
        applyTypedArray(typedArray);
    }

    private void applyTypedArray(TypedArray typedArray){
        text = typedArray.getString(R.styleable.HWPrintTextView_text);

        if(text == null){
            text = "";
        }

        textSize = typedArray.getInt(R.styleable.HWPrintTextView_textSize, 50);
        textAlign = typedArray.getString(R.styleable.HWPrintTextView_textAlign);
        if(textAlign == null){
            textAlign = "center";
        }

        if(typedArray.hasValue(R.styleable.HWPrintTextView_textStyle)){
            textStyle = typedArray.getInt(R.styleable.HWPrintTextView_textStyle, 0);
        }

        if(typedArray.hasValue(R.styleable.HWPrintTextView_textColorMode)){
            colorMode = typedArray.getInt(R.styleable.HWPrintTextView_textColorMode, 0);
        }


        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        paint_size_calculating.setTextSize(textSize);
        paint_size_calculating.getTextBounds(text, 0, text.length(), textBound);

        int textWidth = textBound.width();
        int textHeight = textBound.height()+textSize / 5;

        Log.d("ljm2006_HWPrintTextView", "Text width : " + textWidth + ", Text height : " + textHeight);

//        width = MeasureSpec.getSize(widthMeasureSpec);
//        height = MeasureSpec.getSize(heightMeasureSpec);

        Log.d("ljm2006_HWPrintTextView", "width : " + width + ", height : " + height);

        switch (widthMode){
            case MeasureSpec.AT_MOST:{
//                wrap_contents
                width = textWidth;
                break;
            }
            case MeasureSpec.EXACTLY:{
                width = widthSize;
                break;
            }
            case MeasureSpec.UNSPECIFIED:{
                width = widthMeasureSpec;
                break;
            }
        }

        switch (heightMode){
            case MeasureSpec.AT_MOST:{
                height = textHeight;
                break;
            }
            case MeasureSpec.EXACTLY:{
                height = heightSize;
                break;
            }
            case MeasureSpec.UNSPECIFIED:{
                height = heightMeasureSpec;
                break;
            }
        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawPaint(paint_background);
        paint_text.setTextSize(textSize);
//        paint_text.setAntiAlias(true);

        if(textAlign.equals("center")){
            paint_text.setTextAlign(Paint.Align.CENTER);
        }else if(textAlign.equals("left")){
            paint_text.setTextAlign(Paint.Align.LEFT);
        }else if(textAlign.equals("right")){
            paint_text.setTextAlign(Paint.Align.RIGHT);
        }

        switch (textStyle){
            case 0:{
                paint_text.setTypeface(Typeface.create((String)null, Typeface.NORMAL));
                break;
            }
            case 1:{
                paint_text.setTypeface(Typeface.create((String)null, Typeface.BOLD));
                break;
            }
        }

        switch (colorMode){
            case 0:{
                paint_text.setColor(Color.BLACK);
                break;
            }
            case 1:{
                paint_text.setColor(Color.WHITE);
                break;
            }
        }

        canvas.drawText(text, getWidth()/2, getHeight()-(textSize / 5), paint_text);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
        invalidate();
    }


    public void setTextSize(int textSize) {
        this.textSize = textSize;
        invalidate();
    }


    public void setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode.ordinal();
        invalidate();
    }


    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle.ordinal();
        invalidate();
    }

    public enum TextStyle{
        NORMAL, BOLD
    }

    public enum ColorMode{
        BLACK, WHITE
    }

}
