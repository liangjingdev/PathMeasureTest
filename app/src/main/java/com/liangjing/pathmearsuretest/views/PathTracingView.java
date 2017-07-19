package com.liangjing.pathmearsuretest.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by liangjing on 2017/7/16.
 * 功能：实现路径的绘制动画效果
 */

public class PathTracingView extends View {

    //用来保存路径的变量
    private Path mDst;
    private Paint mPaint;
    //路径长度
    private float mLength;
    private Path mPath;
    private float mAnimValue;

    private PathMeasure mPathMeasure;

    public PathTracingView(Context context) {
        super(context);
    }

    public PathTracingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mPath = new Path();
        mDst = new Path();

        //在路径中添加一个闭合的圆形轮廓线
        mPath.addCircle(400, 400, 100, Path.Direction.CCW);
        mPathMeasure = new PathMeasure();
        //将PathMeasure与Path关联起来
        mPathMeasure.setPath(mPath, true);

        //获取到路径的长度
        mLength = mPathMeasure.getLength();

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    public PathTracingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDst.reset();
        //注意：当使用getSegment()方法的时候，需要让mDst实现lineTo()方法，并设置坐标值为0，否则getSegment()方法可能会失效
        mDst.lineTo(0,0);

        /**
         * 这里实现绘制圆的路径动画
         */
        float start = 0;
        //绘制过程中路径长度在不断的增加
        float stop = mLength * mAnimValue;

        /**
         * 这里实现windows风格的loading(加载)的路径绘制动画效果
         * float start = mLength *  mAnimValue;
         * float stop = (float) (stop - ((0.5 - Math.abs(mAnimValue - 0.5)) * mLength));
         */

        //截取路径片段.将截取出来的路径保存到mDst变量中
        mPathMeasure.getSegment(start,stop,mDst,true);
        canvas.drawPath(mDst,mPaint);
    }
}
