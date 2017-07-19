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
 * 目的：了解 Path Pos Tan方法的运用
 */

public class PathPosTanView extends View implements View.OnClickListener {

    private Path mPath;
    //保存曲线上某点的坐标
    private float[] mPos;
    //保存曲线上某点的切线坐标(运动趋势，也就是切线的方向)
    private float[] mTan;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private ValueAnimator mAnimator;
    private float mCurrentValue;

    public PathPosTanView(Context context) {
        super(context);
    }

    public PathPosTanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        mPath.addCircle(0, 0, 200, Path.Direction.CW);
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath, true);

        mPos = new float[2];
        mTan = new float[2];

        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(3000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        setOnClickListener(this);
    }

    public PathPosTanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //该方法使得当前mCurrentValue所对应的点的坐标保存在mPos数组中，以及它的切线保存到了mTan数组中
        mPathMeasure.getPosTan(mCurrentValue * mPathMeasure.getLength(), mPos, mTan);

        //用于获取路径上某点的切线的角度
        float degree = (float) (Math.atan2(mTan[1], mTan[0]) * 180 / Math.PI);

        canvas.save();
        //先将画布移动到一个指定坐标
        canvas.translate(400, 400);
        canvas.drawPath(mPath, mPaint);
        //在对应的点上画上一个小圆
        canvas.drawCircle(mPos[0], mPos[1], 10, mPaint);
        //技巧：可将画布进行旋转可使得每次只需绘制相同的一条切线即可。（弄清楚切线往哪个方向走，那么只需将画布往相反的方向旋转相同的角度即可）
        canvas.rotate(degree);//注意：刚开始的时候由于那个点位于的位置刚好使得其切线的角度为90度，所以刚开始的时候画布旋转了90度。X轴和Y轴颠倒过来了
        canvas.drawLine(0, -200, 300, -200, mPaint);
        //最後要对画布进行还原，此时相当于画了一条真实的切线
        canvas.restore();
    }

    @Override
    public void onClick(View v) {
        mAnimator.start();
    }
}


/**
 * save
 * int save ()
 * 将当前的矩阵和剪辑保存到一个私有的堆栈中。
 * 后续调用翻译、规模、旋转、倾斜、concat或clipRect clipPath都照常工作,但当平衡restore()被调用后,这些调用将被遗忘,save()之前的设置将恢复。
 */