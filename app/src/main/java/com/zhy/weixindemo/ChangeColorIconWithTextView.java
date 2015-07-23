package com.zhy.weixindemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class ChangeColorIconWithTextView extends View {

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;

    //颜色
    private int color = 0xFF45C01A;
    //透明度
    private float alpha = 0f;
    //图标
    private Bitmap icon;
    //限制绘制icon的范围
    private Rect iconRect;
    //icon底部文本
    private String text = "微信";
    //字体大小
    private int textSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
    private Paint textPaint;
    private Rect textBound = new Rect();

    public ChangeColorIconWithTextView(Context context) {
        super(context);
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attrs
     */
    public ChangeColorIconWithTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorIconWithTextView);

        int indexCount = ta.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeColorIconWithTextView_icon:
                    BitmapDrawable drawable = (BitmapDrawable) ta.getDrawable(attr);
                    icon = drawable.getBitmap();
                    break;
                case R.styleable.ChangeColorIconWithTextView_color:
                    color = ta.getColor(attr, 0x45C01A);
                    break;
                case R.styleable.ChangeColorIconWithTextView_text:
                    text = ta.getString(attr);
                    break;
                case R.styleable.ChangeColorIconWithTextView_textSize:
                    textSize = (int) ta.getDimension(attr, TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }

        ta.recycle();
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(0xff555555);

        //获取text的绘制范围
        textPaint.getTextBounds(text, 0, text.length(), textBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - textBound.height());
        int left = (getMeasuredWidth() - iconWidth) / 2;
        int top = (getMeasuredHeight() - iconWidth - textBound.height()) / 2;

        iconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
        Log.i("TAG", "iconRect = " + iconRect.toString());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //1、绘制原图
        canvas.drawBitmap(icon, null, iconRect, null);
        int alphaInt = (int) Math.ceil(255 * alpha);
        //2、内存去准备bitmap
        setupTargetBitmap(alphaInt);
        //3、绘制原文本
        drawSourceText(canvas, alphaInt);
        //4、绘制目标文本
        drawDestText(canvas, alphaInt);
        canvas.drawBitmap(bitmap, 0, 0, null);

    }

    private void drawDestText(Canvas canvas, int alphaInt) {
        textPaint.setTextSize(textSize);
        textPaint.setColor(color);
        textPaint.setAlpha(alphaInt);
        canvas.drawText(text,
                iconRect.left + (iconRect.width() - textBound.width()) / 2,
                iconRect.bottom + textBound.height(), textPaint);
    }

    private void drawSourceText(Canvas canvas, int alphaInt) {
        textPaint.setTextSize(textSize);
        textPaint.setColor(0xff333333);
        textPaint.setAlpha(255 - alphaInt);
        canvas.drawText(text,
                iconRect.left + (iconRect.width() - textBound.width()) / 2,
                iconRect.bottom + textBound.height(), textPaint);
    }

    private void setupTargetBitmap(int alphaInt) {
        bitmap = Bitmap.createBitmap(getMeasuredWidth(),
                getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(alphaInt);
        //绘制纯色
        canvas.drawRect(iconRect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setAlpha(255);
        //会诊图标
        canvas.drawBitmap(icon, null, iconRect, paint);

    }

    public void setIconAlpha(float alpha) {
        this.alpha = alpha;
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}
