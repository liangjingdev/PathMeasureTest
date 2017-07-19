package com.liangjing.pathmearsuretest.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by liangjing on 2017/7/16.
 * 功能：实现路径的绘制动画效果-Dash
 */

public class PathPaintView extends View {

    private Paint mPaint;
    //路径长度
    private float mLength;
    private Path mPath;
    private float mAnimValue;
    //实现不同的绘制风格
    private PathEffect mPathEffect;

    private PathMeasure mPathMeasure;

    public PathPaintView(Context context) {
        super(context);
    }

    public PathPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mPath = new Path();

        //画一个等边三角形
        mPath.moveTo(100, 100);
        mPath.lineTo(100, 500);
        mPath.lineTo(400, 300);
        mPath.close();
        mPathMeasure = new PathMeasure();
        //将PathMeasure与Path关联起来
        mPathMeasure.setPath(mPath, true);

        //获取到路径的长度
        mLength = mPathMeasure.getLength();

        //1f到0f使得虚线慢慢的减少而实线慢慢的出来
        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimValue = (float) animation.getAnimatedValue();
                //将DashPathEffect作为路径的绘制效果（其实也就是让虚线和实线进行交替的变化） 最後一个参数为偏移量，通过它来控制显示的路径
                mPathEffect = new DashPathEffect(new float[]{mLength, mLength}, mLength * mAnimValue);
                mPaint.setPathEffect(mPathEffect);
                invalidate();
            }
        });
        animator.start();
    }

    public PathPaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }
}
