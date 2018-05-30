package com.ulabs.ulabsmodule.hwprinter.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.ulabs.ulabsmodule.R;


/**
 * Created by OH-Biz on 2018-02-01.
 */

public class HWPrintTextView extends View {
    private static final String TAG = "HWPrintTextView";
    private Paint paint_text;
    private Paint paint_background;

    private String text;
    private int textAlign = 0;
    private int textSize;

    private int width;
    private int height;
    private Paint paint_size_calculating;
    private Rect textBound;

    private int colorMode = 0;
    private int textStyle = 0;
    private int textWidth;
    private int textHeight;
    private StaticLayout staticLayout;
    private DynamicLayout dynamicLayout;
    private TextPaint textPaint;

    private int currentLine = 1;
    private Rect sidePaddingRect;
    private int paddingX;

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
        sidePaddingRect = new Rect();
        textPaint = new TextPaint();
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

        String[] separation = text.split("\n");
        currentLine = separation.length;

        textSize = typedArray.getInt(R.styleable.HWPrintTextView_textSize, 50);
        //default -> left
        if(typedArray.hasValue(R.styleable.HWPrintTextView_textAlign)){
            textAlign = typedArray.getInt(R.styleable.HWPrintTextView_textAlign, 1);
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

        String regex = ".*[가-힣]+.*";
        if(text.matches(regex)){
            textWidth = textBound.width() + (textBound.width()/20);
        }else{
            textWidth = textBound.width();
        }

//        Text Height Setting
        if(currentLine == 1){
            textHeight = textBound.height() + textSize / 4;
        }else{
            textHeight = textBound.height() * (currentLine + 1);
        }


//        width = MeasureSpec.getSize(widthMeasureSpec);
//        height = MeasureSpec.getSize(heightMeasureSpec);

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

        paddingX = (width - textWidth) / 2;
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
        textPaint.setTextSize(textSize);

        switch (colorMode){
            case 0:{
                textPaint.setColor(Color.BLACK);
                break;
            }
            case 1:{
                textPaint.setColor(Color.WHITE);
                break;
            }
        }

        switch (textStyle){
            case 0:{
                textPaint.setTypeface(Typeface.create((String)null, Typeface.NORMAL));
                break;
            }
            case 1:{
                textPaint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
                break;
            }
        }

        dynamicLayout = new DynamicLayout(text, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        drawText(canvas, textAlign);
    }

    private void drawText(Canvas canvas, int textAlign){
        switch (textAlign){
            case 0:{
//                center
                canvas.save();
                canvas.translate(paddingX,0);
                dynamicLayout.draw(canvas);
                canvas.restore();
                break;
            }
            case 1:{
//                left
                canvas.save();
                canvas.translate(0,0);
                dynamicLayout.draw(canvas);
                canvas.restore();
                break;
            }
            case 2:{
//                right
                canvas.save();
                canvas.translate(paddingX * 2, 0);
                dynamicLayout.draw(canvas);
                canvas.restore();
                break;
            }
        }
    }

    public void setText(String text) {
        this.text = text;

        currentLine = 0;
        String[] separation = text.split("\n");
        currentLine = separation.length;
        requestLayout();
        invalidate();
    }

    public void setTextAlign(AlignMode alignMode) {
        this.textAlign = alignMode.ordinal();
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

    public String getText() {
        return text;
    }

    public enum TextStyle{
        NORMAL, BOLD
    }

    public enum ColorMode{
        BLACK, WHITE
    }

    public enum AlignMode{
        CENTER, LEFT, RIGHT
    }

}
