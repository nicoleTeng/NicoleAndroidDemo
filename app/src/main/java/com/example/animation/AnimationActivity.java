package com.example.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.nicole.R;

public class AnimationActivity extends Activity {
    private View translateView;
    private View scaleView;
    private View rotateView;
    private View alphaView;
    private View frameView;
    private View propertyObjectView;
    private View propertyValueView;
    private View propertySetView;
    private View propertyXMLView;

    private Animation translateAnimation;
    private Animation rotateAnimation;
    private Animation scaleAnimation;
    private Animation alphaAnimation;
    private ValueAnimator colorAnimator;

    private boolean useXMLAnimation = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_animation);

        translateView = findViewById(R.id.translate_id);
        scaleView = findViewById(R.id.scale_id);
        rotateView = findViewById(R.id.rotate_id);
        alphaView = findViewById(R.id.alpha_id);

        frameView = findViewById(R.id.frame_id);

        propertyObjectView = findViewById(R.id.property_object_id);
        propertyValueView = findViewById(R.id.property_value_id);
        propertySetView = findViewById(R.id.property_set_id);
        propertyXMLView = findViewById(R.id.property_xml_id);

        scaleView.post(new Runnable() {
            @Override
            public void run() {
                ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.2, 1, (float) 0.5, 1,
                        scaleView.getWidth() / 2, scaleView.getHeight() / 2);
                scaleAnimation.setDuration(3000);
                scaleAnimation.setRepeatCount(3);
                scaleAnimation.setRepeatMode(Animation.REVERSE);
                scaleView.setAnimation(scaleAnimation);
            }
        });

        if (useXMLAnimation) {
            showViewAnimationXML();
        } else {
            showViewAnimationCode();
        }

        showFrameAnimation();
        showPropertyAnimationXML();
        showPropertyAnimationCode();
    }

    /*
     * View animation with XML
     */
    private void showViewAnimationXML() {
        translateAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_test);
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_test);
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_test);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_test);

        translateView.setAnimation(translateAnimation);
        rotateView.setAnimation(rotateAnimation);
        scaleView.setAnimation(scaleAnimation);
        alphaView.setAnimation(alphaAnimation);
    }

    /*
     * View animation with code
     */
    private void showViewAnimationCode() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 360, 0, 0);
        translateAnimation.setDuration(3000);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360);
        rotateAnimation.setDuration(3000);
        ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.2, 1, (float) 0.5, 1);
        scaleAnimation.setDuration(3000);
        scaleAnimation.setRepeatCount(3);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);

        translateView.setAnimation(translateAnimation);
        rotateView.setAnimation(rotateAnimation);
        scaleView.setAnimation(scaleAnimation);
        alphaView.setAnimation(alphaAnimation);
    }

    /*
     * frame animation
     */
    private void showFrameAnimation() {
        frameView.setBackgroundResource(R.drawable.frame_animation);
        AnimationDrawable drawable = (AnimationDrawable) frameView.getBackground();
        drawable.start();

    }

    /*
     * property animation with XML
     */
    private void showPropertyAnimationXML() {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this,
                R.animator.property_animator);
        set.setTarget(propertyXMLView);
        set.start();
    }

    /*
     * property animation with code
     */
    private void showPropertyAnimationCode() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(propertyObjectView,
                "translationY", -100);
        animator.setDuration(3000).start();
        propertyObjectView.clearAnimation();

        colorAnimator = ObjectAnimator.ofInt(propertyValueView,
                "backgroundColor", 0xffff8080, 0xff8080ff);
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator.setDuration(3000).start();

        AnimatorSet setAnimator = new AnimatorSet();
        setAnimator.playTogether(
                ObjectAnimator.ofFloat(propertySetView, "rotationX", 0, 360),
                ObjectAnimator.ofFloat(propertySetView, "rotationY", 0, 180),
                ObjectAnimator.ofFloat(propertySetView, "rotation", 0, -90),
                ObjectAnimator.ofFloat(propertySetView, "translationX", 0, 90),
                ObjectAnimator.ofFloat(propertySetView, "translationY", 0, 90),
                ObjectAnimator.ofFloat(propertySetView, "scaleX", 1, 1.5f),
                ObjectAnimator.ofFloat(propertySetView, "scaleY", 1, 0.5f),
                ObjectAnimator.ofFloat(propertySetView, "alpha", 1, 0.25f, 1)
        );
        setAnimator.setDuration(5000).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO: should stop repeat animation to avoid memory leak
        colorAnimator.end();
        scaleAnimation.cancel();
    }
}
