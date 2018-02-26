package coders.mich.gtdapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import coders.mich.gtdapp.R;
import coders.mich.gtdapp.utils.helpers;

import static android.support.v4.view.ViewCompat.SCROLL_AXIS_HORIZONTAL;

/**
 * Created by drew on 2/26/2018.
 *
 * FabBehavior to control the position of the fab based on the page/scroll offset of the ViewPager
 * as well as the BottomNavigationView
 */

public abstract class FabBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private static final String TAG = "FabBehavior";

    float fabSizeNormal, fabSizeMini, fabSize;

    public FabBehavior() {

    }

    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        fabSizeNormal = helpers.getDimension(context, R.dimen.fab_size_normal);
        fabSizeMini = helpers.getDimension(context, R.dimen.fab_size_mini);
        fabSize = fabSizeNormal;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        if (dependency instanceof BottomNavigationView ||
                dependency instanceof ViewPager) return true;

        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {

        if (dependency instanceof BottomNavigationView) {
            Log.d(TAG, "onDependentViewChanged: BottomNav");
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int fabDefaultBottomMargin = p.bottomMargin;
            child.setY(dependency.getY() - fabSize - fabDefaultBottomMargin);
            onBottomNavDrawn(dependency);
        }

        return super.onDependentViewChanged(parent, child, dependency);
    }

    public abstract void onBottomNavDrawn(View bottomNav);

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull V child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        // Ensure we react to vertical scrolling
        return axes == SCROLL_AXIS_HORIZONTAL
                || super.onStartNestedScroll(
                        coordinatorLayout, child, directTargetChild, target, axes, type);
    }
}
