package coders.mich.gtdapp.animation;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import coders.mich.gtdapp.R;
import coders.mich.gtdapp.utils.helpers;

import static coders.mich.gtdapp.utils.helpers.getDimension;
import static coders.mich.gtdapp.utils.helpers.getStatusBarHeight;

/**
 * Created by drew on 2/23/2018.
 */

public class MainFabAnimationManager {

    private static final String TAG = "MainFabAnimationManager";

    public static final int STATE_UNDEFINED = 331;
    public static final int STATE_SCREEN_CORNER = 140;
    public static final int STATE_SCREEN_CENTER = 186;
    public static final int STATE_NEW_TASK = 999;
    public static final int STATE_NEW_TASK_EXTENDED = 2;
    private int currentState = STATE_UNDEFINED;

    private float fabSizeNormal, fabSizeMini, fabShadow, fabMargin;
    private int screenHeight, screenWidth;
    private float startX, startY, endX, endY, startSize, endSize;

    private ImageButton fab;
    private boolean iconUpdateNeeded = false;

    public MainFabAnimationManager(Activity activity, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        this.screenHeight = displayMetrics.heightPixels - getStatusBarHeight(activity);
        this.screenWidth = displayMetrics.widthPixels;

        fabShadow = helpers.getDimension(activity, R.dimen.fab_shadow);
        fabSizeMini = helpers.getDimension(activity, R.dimen.fab_size_mini) + (2 * fabShadow);
        fabSizeNormal = helpers.getDimension(activity, R.dimen.fab_size_normal) + (2 * fabShadow);
        fabMargin = helpers.getDimension(activity, R.dimen.fab_margin);

        startX = (screenWidth - fabSizeNormal) / 2f;
        startY = (screenHeight - fabSizeNormal) / 2f;
        endX = screenWidth - fabSizeNormal - fabMargin;
        endY = screenHeight - fabSizeNormal - fabMargin;

        startSize = fabSizeMini;
        endSize = fabSizeNormal;

        setFab(view);
    }

    public void moveToScreenCenterPosition() {

    }

    public void moveToScreenCornerPosition() {

    }

    public void moveToNewTaskPosition() {

    }

    public void moveToNewTaskExtendedPosition() {

    }

    public void updateWithViewPager(int position, float positionOffset, int positionOffsetPixels) {
        // only animate on first 'capture' page
        if (position == 0) {
            if (positionOffset == 0) {
                fab.setImageResource(R.drawable.ic_done_black_24dp);
                iconUpdateNeeded = true;
            } else if (iconUpdateNeeded) {
                fab.setImageResource(R.drawable.ic_add_black_24dp);
                iconUpdateNeeded = false;
            }
            float xPos = helpers.map(positionOffset, 0, 1, startX, endX);
            float yPos = helpers.map(positionOffset, 0, 1, startY, endY);
            int size = (int) helpers.map(positionOffset, 0, 1, startSize, endSize);

            CoordinatorLayout.LayoutParams params =
                    (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            params.height = size;
            params.width = size;
            fab.setLayoutParams(params);

            fab.setX(xPos);
            fab.setY(yPos);
        }
    }

    public void setStartPosition(float startX, float startY) {
        setStartX(startX);
        setStartY(startY);
        fab.invalidate();
    }

    public void setEndPosition(float endX, float endY) {
        setEndX(endX);
        setEndY(endY);
        fab.invalidate();
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public void setFab(View fab) {
        this.fab = (ImageButton) fab;
    }
}
