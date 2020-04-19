package com.android.ocat.global.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;

import androidx.appcompat.widget.AppCompatImageView;

public class MusicButton extends AppCompatImageView {
    public static final int STATE_PLAYING =1;
    public static final int STATE_PAUSE =2;
    public static final int STATE_STOP =3;
    public int state;

    private float angle;
    private float angle2;
    private int viewWidth;
    private int viewHeight;
    private MusicAnim musicAnim;

    public MusicButton(Context context) {
        super(context);
        init();
    }

    public MusicButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MusicButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        state = STATE_STOP;
        angle = 0;
        angle2 = 0;
        viewWidth = 0;
        viewHeight = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(angle2,viewWidth/2,viewHeight/2);
        super.onDraw(canvas);
    }

    public class MusicAnim extends RotateAnimation {
        public MusicAnim(float fromDegrees, float toDegrees, float pivotX, float pivotY) {
            super(fromDegrees, toDegrees, pivotX, pivotY);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            angle = interpolatedTime * 360;
        }
    }

    public void playMusic(){
        if(state == STATE_PLAYING){
            angle2 = (angle2 + angle)%360;
            musicAnim.cancel();
            state = STATE_PAUSE;
            invalidate();
        }else {
            musicAnim = new MusicAnim(0,360,viewWidth/2,viewHeight/2);
            musicAnim.setDuration(3000);
            musicAnim.setInterpolator(new LinearInterpolator());
            musicAnim.setRepeatCount(ObjectAnimator.INFINITE);
            startAnimation(musicAnim);
            state = STATE_PLAYING;
        }
    }

    public void stopMusic(){
        angle2 = 0;
        clearAnimation();
        state = STATE_STOP;
        invalidate();
    }
}
