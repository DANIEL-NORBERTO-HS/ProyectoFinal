
package com.google.ar.sceneform.ux;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.google.ar.sceneform.ux.R;


/** This view contains the hand motion instructions with animation. */

public class HandMotionView extends AppCompatImageView {
  private HandMotionAnimation animation;
  private static final long ANIMATION_SPEED_MS = 2500;

  public HandMotionView(Context context) {
    super(context);
  }

  public HandMotionView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    clearAnimation();

    FrameLayout container =
        (FrameLayout) ((Activity) getContext()).findViewById(R.id.sceneform_hand_layout);

    animation = new HandMotionAnimation(container, this);
    animation.setRepeatCount(Animation.INFINITE);
    animation.setDuration(ANIMATION_SPEED_MS);
    animation.setStartOffset(1000);

    startAnimation(animation);
  }
}
