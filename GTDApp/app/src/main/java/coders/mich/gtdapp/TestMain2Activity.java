package coders.mich.gtdapp;

import android.arch.lifecycle.Observer;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import coders.mich.gtdapp.data.AppDatabase;
import coders.mich.gtdapp.data.AppViewModel;
import coders.mich.gtdapp.data.DummyData;
import coders.mich.gtdapp.data.TaskDao;
import coders.mich.gtdapp.data.dao.Bucket;
import coders.mich.gtdapp.model.Task;
import coders.mich.gtdapp.ui.TaskAdapter;

public class TestMain2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        RecyclerView rvTaskList = findViewById(R.id.rv_task_list);

        avdAddToDone = (AnimatedVectorDrawable)
                getResources().getDrawable(R.drawable.avd_add_to_done);
        avdDoneToAdd = (AnimatedVectorDrawable)
                getResources().getDrawable(R.drawable.avd_done_to_add);

        final EditText etTaskInput = findViewById(R.id.et_task_input);

        final AppViewModel viewModel = new AppViewModel();
        final AppDatabase appDatabase = AppDatabase.getInstance(this);
        final TaskDao taskDao = appDatabase.taskDao();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // If modal visible, add the task
                if (modalVisible) {
                    // get the task info from an edit text and save into an object
                    String taskName = etTaskInput.getText().toString();
                    etTaskInput.setText("");

                    Task task = new Task();
                    task.setName(taskName);
                    task.setDescription("this is only a test");
                    task.setBucketId(1);

                    if (!taskName.equals("")){
                        viewModel.createTask(task, taskDao);
                    }
                }
                showHideModal();
            }
        });

        final TaskAdapter adapter = new TaskAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        rvTaskList.setAdapter(adapter);
        rvTaskList.setLayoutManager(layoutManager);

        viewModel.getItems(taskDao).observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                adapter.updateTasks(tasks);
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
        ConstraintLayout.LayoutParams params =
                (ConstraintLayout.LayoutParams) modal.getLayoutParams();
        if (modalVisible) {
            params.topToBottom = R.id.view_constrained_bottom;
            params.topToTop = ConstraintLayout.LayoutParams.UNSET;
            params.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;
            params.rightToRight = ConstraintLayout.LayoutParams.UNSET;
            params.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
        } else {
            params.topToBottom = ConstraintLayout.LayoutParams.UNSET;
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
            params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        }
        TransitionManager.beginDelayedTransition((ViewGroup) modal.getRootView());
        modal.setLayoutParams(params);

        // Run animated vector drawable if >= Lollipop, if not, just change the drawable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (modalVisible) {
                fab.setImageDrawable(avdDoneToAdd);
                avdDoneToAdd.start();
            } else {
                fab.setImageDrawable(avdAddToDone);
                avdAddToDone.start();
            }
        } else {
            fab.setImageResource(
                    modalVisible ? R.drawable.ic_add_black_24dp : R.drawable.ic_done_black_24dp);
        }

        modalVisible = !modalVisible;
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
