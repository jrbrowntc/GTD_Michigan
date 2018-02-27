package coders.mich.gtdapp;

import android.animation.FloatArrayEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;

import coders.mich.gtdapp.animation.MainFabAnimationManager;
import coders.mich.gtdapp.navigation.MainNavManager;
import coders.mich.gtdapp.ui.FabBehavior;
import coders.mich.gtdapp.utils.helpers;

/**
 * Created by drew on 2/20/2018.
 */

public class TestMainActivity extends AppCompatActivity {

    private static final String TAG = "TestMainActivity";

    private MainNavManager navManager;
    private boolean pageAlreadyDrawn = false;

    private View cardView;

    private float fabSize, fabSizeNormal, fabSizeMini, fabShadow, fabMargin;
    private ImageButton fakeFab;
    private MainFabAnimationManager fabAnimationManager;
    private FabBehavior fabBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        fabShadow = helpers.getDimension(this, R.dimen.fab_shadow);
        fabSizeMini = helpers.getDimension(this, R.dimen.fab_size_mini) + (2 * fabShadow);
        fabSizeNormal = helpers.getDimension(this, R.dimen.fab_size_normal) + (2 * fabShadow);
        fabMargin = helpers.getDimension(this, R.dimen.fab_margin);

        fakeFab = findViewById(R.id.fab_resize);
        fakeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navManager.goToPage(0);
            }
        });
        fabAnimationManager = new MainFabAnimationManager(this, fakeFab);
        fabBehavior = new FabBehavior() {
            @Override
            public void onBottomNavDrawn(View bottomNav) {
                float endX = bottomNav.getRight() - fabSizeNormal - fabMargin;
                float endY = bottomNav.getTop() - fabSizeNormal - fabMargin;
                Log.d(TAG, "onBottomNavDrawn: " + endX + ", " + endY);
                fabAnimationManager.setEndPosition(endX, endY);
            }
        };
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) fakeFab.getLayoutParams();
        params.setBehavior(fabBehavior);


        navManager = new MainNavManager(this,
                findViewById(R.id.navigation), findViewById(R.id.view_pager_content)) {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                fabAnimationManager.updateWithViewPager(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageDrawn(int position) {
                handlePageDraw(position);
            }
        };

    }

    public void resizeFab(final View view) {
        view.setX(0);
        boolean shrink = view.getTag() == null;
        float newSize = (shrink ?
                getResources().getDimension(R.dimen.fab_size_mini):
                getResources().getDimension(R.dimen.fab_size_normal)) +
                (2 * getResources().getDimension(R.dimen.fab_shadow));
        ValueAnimator heightAnim = ValueAnimator.ofInt(view.getMeasuredHeight(), (int) newSize);
        heightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
            }
        });
        heightAnim.setDuration(300);
        ValueAnimator widthAnim = ValueAnimator.ofInt(view.getMeasuredWidth(), (int) newSize);
        widthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = val;
                view.setLayoutParams(layoutParams);
            }
        });
        widthAnim.setDuration(300);
        heightAnim.start();
        widthAnim.start();
        if (shrink) {
            view.setTag("Not Null");
        } else {
            view.setTag(null);
        }
    }

    private void handlePageDraw(int pagePosition) {
        if (pagePosition == 0 && !pageAlreadyDrawn) {
            View cardView = findViewById(R.id.card_new_task);

            float fabShadow = helpers.getDimension(this, R.dimen.fab_shadow);
            float startX = cardView.getRight() -
                    (helpers.getDimension(this, R.dimen.fab_size_mini) / 2) - fabShadow;
            float startY = cardView.getTop() - fabShadow;
            fabAnimationManager.setStartPosition(startX, startY);

            pageAlreadyDrawn = true;
        }
    }

}
