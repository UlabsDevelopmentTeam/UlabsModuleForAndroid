package com.ulabs.ulabsmodule.hwprinter.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.ulabs.ulabsmodule.R;

public class HWQRCodeView extends View {
    private int width;
    private int height;
    private String code;

    public HWQRCodeView(Context context) {
        super(context);
    }

    public HWQRCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
    }

    public HWQRCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(attrs, defStyleAttr);
    }

    private void getAttrs(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HWQRCodeView);
        applyTypedArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HWQRCodeView, defStyle, 0);
        applyTypedArray(typedArray);
    }

    private void applyTypedArray(TypedArray typedArray){
        code = typedArray.getString(R.styleable.HWQRCodeView_qr_code_data);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode){
            case MeasureSpec.AT_MOST:{
                width = 100;
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
                height = 100;
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

        setMeasuredDimension(width,height);
    }

    private Bitmap generateQRCode(String data){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix matrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width,height);
            Bitmap bitmap = Bitmap.createBitmap(matrix.getWidth(), matrix.getHeight(), Bitmap.Config.ARGB_4444);
            for(int x = 0 ; x < matrix.getWidth() ; x++){
                for(int y = 0 ; y < matrix.getHeight() ; y++){
                    bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap QRCode = generateQRCode(code);
        try{
            canvas.drawBitmap(QRCode, 0, 0, null);
        }catch (NullPointerException e){

        }

    }
}
