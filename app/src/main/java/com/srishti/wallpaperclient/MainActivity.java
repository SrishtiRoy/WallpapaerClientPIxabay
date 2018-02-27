package com.srishti.wallpaperclient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.srishti.wallpaperclient.pixabay.adapter.ImageRecyclerViewAdapter;
import com.srishti.wallpaperclient.pixabay.event.ErrorEvent;
import com.srishti.wallpaperclient.pixabay.event.EventManager;
import com.srishti.wallpaperclient.pixabay.event.ImageDetailsEvent;
import com.srishti.wallpaperclient.pixabay.model.PixabayResponse;
import com.srishti.wallpaperclient.pixabay.service.PixabayServiceProvider;
import com.srishti.wallpaperclient.pixabay.ui.BaseActivity;
import com.srishti.wallpaperclient.pixabay.ui.ImageActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private PixabayServiceProvider pixabayServiceProvider;
    private EventManager eventManager;
    private ImageRecyclerViewAdapter recyclerViewAdapter;
    private ActionBarDrawerToggle drawerToggle;

    @BindView(R.id.recycler_view)
    RecyclerView imageRecyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toolbar.setTitle("Wallpaper");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);
        // Tie DrawerLayout events to the ActionBarToggle
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        eventManager = EventManager.getInstance();
        pixabayServiceProvider = PixabayServiceProvider.getInstance();
        pixabayServiceProvider.editorsChoice();

        recyclerViewAdapter = new ImageRecyclerViewAdapter(this, eventManager);
        imageRecyclerView.setAdapter(recyclerViewAdapter);
        imageRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.offset);
        imageRecyclerView.addItemDecoration(itemDecoration);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pixabayServiceProvider.editorsChoice();
            }
        });

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.SET_WALLPAPER};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        setUpNavMenu();

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPixabayResponse(PixabayResponse response) {
        if (response.getImages().size() > 0) {
            recyclerViewAdapter.clear();
            recyclerViewAdapter.addAll(response.getImages());
            swipeRefreshLayout.setRefreshing(false);
        } else {
            Toast.makeText(this, "No images available", Toast.LENGTH_SHORT).show();
        }

        progressBar.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent errorEvent) {
        Toast.makeText(this, errorEvent.getMessage(), Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onImageDetailsEvent(ImageDetailsEvent imageDetailsEvent) {
        Intent intent = new Intent(this, ImageActivity.class);
        startActivity(intent);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.
        // ActionBarDrawToggle() does not require it and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Uri uri = Uri.parse("mailto:sreeshtyray@gmail.com");
                Intent myActivity2 = new Intent(Intent.ACTION_SENDTO, uri);
                myActivity2.putExtra(Intent.EXTRA_SUBJECT,
                        "Customer comments/questions");
                startActivity(myActivity2);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }




    void setUpNavMenu() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Handle navigation view item clicks here.
                        int id = menuItem.getItemId();

                        switch (id) {
                            case R.id.flower:
                                pixabayServiceProvider.flower();
                                break;
                            case R.id.home:
                                pixabayServiceProvider.editorsChoice();
                                break;
                            case R.id.painting:
                                pixabayServiceProvider.painting();
                                break;
                            case R.id.nature:
                                pixabayServiceProvider.nature();
                                break;
                            case R.id.art:
                                pixabayServiceProvider.abstractArt();
                                break;

                        }

                        menuItem.setChecked(true);

                        mDrawer.closeDrawers();

                        return true;
                    }
                }
        );


    }



    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
}
