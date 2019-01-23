package com.ulabs.ulabsmodule.hwprinter.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.ulabs.ulabsmodule.R;


/**
 * Created by OH-Biz on 2018-02-02.
 */

public class HWBarcodeView extends View {
    private int barcodeWidth;
    private int barcodeHeight;
    private String code;
    private boolean dependOnViewSize = false;

    private int width;
    private int height;

    public HWBarcodeView(Context context) {
        super(context);
    }

    public HWBarcodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
    }

    public HWBarcodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(attrs, defStyleAttr);
    }

    private void initView(){

    }

    private void getAttrs(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HWBarcodeView);
        applyTypedArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HWBarcodeView, defStyle, 0);
        applyTypedArray(typedArray);
    }

    private void applyTypedArray(TypedArray typedArray){
        code = typedArray.getString(R.styleable.HWBarcodeView_code);
        barcodeWidth = typedArray.getInt(R.styleable.HWBarcodeView_barcodeWidth, 100);
        barcodeHeight = typedArray.getInt(R.styleable.HWBarcodeView_barcodeHeight, 50);
        dependOnViewSize = typedArray.getBoolean(R.styleable.HWBarcodeView_dependOnViewSize, false);

        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(dependOnViewSize){
            barcodeWidth = widthSize;
            barcodeHeight = heightSize;
        }

        switch (widthMode){
            case MeasureSpec.AT_MOST:{
//                wrap_contents
                width = barcodeWidth;

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
                height = barcodeHeight;
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

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap barcode = createBarcode(code, barcodeWidth, barcodeHeight);
        try{
            canvas.drawBitmap(barcode, 0, 0 , null);
        }catch (NullPointerException e){

        }

    }

    private Bitmap createBarcode(String code, int width, int height){
        Bitmap bitmap = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.CODE_128, width, height);
            bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_4444);
            for(int i = 0 ; i < width ; i++){
                for(int j = 0 ; j < height ; j++){
                    bitmap.setPixel(i,j,bitMatrix.get(i,j) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (NullPointerException e){

        }

        return bitmap;
    }

    public void setBarcodeCode(String code){
        this.code = code;
        invalidate();
    }

    public void setBarcodeWidth(int barcodeWidth) {
        this.barcodeWidth = barcodeWidth;
        invalidate();
    }

    public void setBarcodeHeight(int barcodeHeight) {
        this.barcodeHeight = barcodeHeight;
        invalidate();
    }

    public void setDependOnViewSize(boolean dependOnViewSize) {
        this.dependOnViewSize = dependOnViewSize;
        invalidate();
    }
}
