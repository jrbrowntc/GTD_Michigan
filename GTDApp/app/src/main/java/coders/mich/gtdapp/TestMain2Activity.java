package coders.mich.gtdapp;

import android.animation.Animator;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.ChangeTransform;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.List;

import coders.mich.gtdapp.animation.EndAnimatorListener;
import coders.mich.gtdapp.data.DummyData;
import coders.mich.gtdapp.data.dao.Bucket;

public class TestMain2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "TestMain2Activity";

    private boolean modalVisible = false;
    private CardView modal;

    private FloatingActionButton fab;
    private AnimatedVectorDrawable avdAddToDone, avdDoneToAdd;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        modal = findViewById(R.id.modal);

        avdAddToDone = (AnimatedVectorDrawable)
                getResources().getDrawable(R.drawable.avd_add_to_done);
        avdDoneToAdd = (AnimatedVectorDrawable)
                getResources().getDrawable(R.drawable.avd_done_to_add);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHideModal();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fillMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_process) {
            Snackbar.make(fab, "Process Action Selected", Snackbar.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // TODO: 2/27/2018 Come up with some way of detecting which of the dynamically created buckets was selected

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showHideModal() {
        modalVisible = !modalVisible;

        // Order here is important:
        // updateModalLayoutParams invokes TransitionManager.beginDelayedTransition
        // which makes the modal fade in and out if called before circularRevealModal
        circularRevealModal();
        updateModalLayoutParams();
        updateFabIcon();
    }

    private void circularRevealModal() {
        int revealStartX = modal.getWidth();
        int revealStartY = modal.getHeight();
        float modalHypot = (float) Math.hypot(modal.getHeight(), modal.getWidth());
        float revealStartRadius, revealEndRadius;

        if (modalVisible) {
            revealStartRadius = modalHypot;
            revealEndRadius = 0;
        } else {
            revealStartRadius = 0;
            revealEndRadius = modalHypot;
        }

        if (modalVisible) modal.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    modal, revealStartX, revealStartY, revealStartRadius, revealEndRadius);
            if (!modalVisible) {
                circularReveal.addListener(new EndAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        modal.setVisibility(View.INVISIBLE);
                    }
                });
            }
            circularReveal.setInterpolator(new AccelerateDecelerateInterpolator());
            circularReveal.start();
        } else {
            if (!modalVisible) modal.setVisibility(View.INVISIBLE);
        }
    }

    public void updateModalLayoutParams() {
        ConstraintLayout.LayoutParams params =
                (ConstraintLayout.LayoutParams) modal.getLayoutParams();

        if (modalVisible) {
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        } else {
            params.topToTop = ConstraintLayout.LayoutParams.UNSET;
        }
        TransitionManager.beginDelayedTransition((ViewGroup) modal.getRootView());
        modal.setLayoutParams(params);
    }

    // Updates the icon from add to done with Animated Vector Drawable if possible
    private void updateFabIcon() {
        // Run animated vector drawable if >= Lollipop, if not, just change the drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (modalVisible) {
                fab.setImageDrawable(avdAddToDone);
                avdAddToDone.start();
            } else {
                fab.setImageDrawable(avdDoneToAdd);
                avdDoneToAdd.start();
            }
        } else {
            fab.setImageResource(
                    modalVisible ? R.drawable.ic_done_black_24dp : R.drawable.ic_add_black_24dp);
        }
    }

    public void fillMenu() {
        Menu menu = navigationView.getMenu();
        SubMenu subMenu = menu.addSubMenu("Buckets");

        // TODO: 2/27/2018 Replace with Room DAO Method to get all buckets
        List<Bucket> buckets = DummyData.getBuckets();

        for (int i = 0; i < buckets.size(); i++) {
            Bucket bucket = buckets.get(i);
            String bucketName = bucket.getName();
            Integer iconId = bucket.getIconId();

            if (iconId == null) iconId = R.drawable.ic_folder_black_24dp;

            subMenu.add(0, Menu.FIRST + i, Menu.FIRST, bucketName)
                    .setCheckable(true)
                    .setIcon(iconId);
        }
    }
}
