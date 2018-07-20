package com.vedas.weightloss.Transition;

import android.content.Intent;

import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by lgvalle on 12/09/15.
 */
public class BaseDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TYPE = "type";
    public static final int TYPE_PROGRAMMATICALLY = 0;
    public static final int TYPE_XML = 1;


    @SuppressWarnings("unchecked")
    public void transitionTo(Intent i) {
        final android.support.v4.util.Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }
}
