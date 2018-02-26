package coders.mich.gtdapp.navigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import coders.mich.gtdapp.R;

/**
 * Created by drew on 2/21/2018.
 */

public abstract class MainNavManager {

    private Context context;

    private BottomNavigationView navigation;
    private List<Integer> menuButtonIds;
    private ViewPager viewPager;
    private ContentViewPagerAdapter contentViewPagerAdapter;
    private int[] contentLayouts;


    public MainNavManager(Context context, View bottomNavigationView, View contentViewPagerView) {

        this.context = context;

        menuButtonIds = new ArrayList<>();
        menuButtonIds.add(R.id.navigation_capture);
        menuButtonIds.add(R.id.navigation_process);
        menuButtonIds.add(R.id.navigation_organize);
        menuButtonIds.add(R.id.navigation_review);
        menuButtonIds.add(R.id.navigation_engage);

        navigation = (BottomNavigationView) bottomNavigationView;
        navigation.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuPosition = menuButtonIds.indexOf(item.getItemId());
                if (menuPosition == -1) return false;
                viewPager.setCurrentItem(menuPosition);
                return true;
            }
        });


        viewPager = (ViewPager) contentViewPagerView;
        contentLayouts = new int[] {
                R.layout.layout_capture,
                R.layout.layout_process,
                R.layout.layout_organize,
                R.layout.layout_review,
                R.layout.layout_engage
        };

        contentViewPagerAdapter = new ContentViewPagerAdapter();

        viewPager.setAdapter(contentViewPagerAdapter);
        viewPager.addOnPageChangeListener(contentPageChangeListener);

    }

    public abstract void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
    public abstract void onPageSelected(int position);
    public abstract void onPageDrawn(int position);

    private final ViewPager.OnPageChangeListener contentPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            MainNavManager.this.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            navigation.setSelectedItemId(
                    menuButtonIds.get(position));
            MainNavManager.this.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class ContentViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public ContentViewPagerAdapter() {}

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(
                    contentLayouts[position], container, false);

            // Notify when view is drawn
            view.post(new Runnable() {
                @Override
                public void run() {
                    onPageDrawn(position);
                }
            });

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return contentLayouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}