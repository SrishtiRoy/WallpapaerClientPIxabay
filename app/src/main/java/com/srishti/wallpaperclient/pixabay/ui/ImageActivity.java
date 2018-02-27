package com.srishti.wallpaperclient.pixabay.ui;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.srishti.wallpaperclient.R;
import com.srishti.wallpaperclient.pixabay.event.EventManager;
import com.srishti.wallpaperclient.pixabay.event.ImageDetailsEvent;
import com.srishti.wallpaperclient.pixabay.listener.UpdateFrag;
import com.srishti.wallpaperclient.pixabay.model.Image;
import com.srishti.wallpaperclient.pixabay.transformer.DepthPageTransformer;
import com.srishti.wallpaperclient.pixabay.transformer.ZoomOutPageTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageActivity extends BaseActivity {

    private Image image;
    private List<Image> imageList;
    private WallpaperManager wallpaperManager;
    private ViewPager mViewPager;
    private int pos;
    private UpdateFrag updatfrag;
    GalleryPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        eventManager = EventManager.getInstance();

        ButterKnife.bind(this);
        pagerAdapter = new GalleryPagerAdapter(this);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_set_wallpaper:
                updatfrag.updateWallper();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateWallpaper(UpdateFrag listener) {
        updatfrag = listener;
    }

    private void initViews() {
        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mViewPager = (ViewPager) findViewById(R.id.fullscreen_content);
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onImageDetailsEvent(ImageDetailsEvent imageDetailsEvent) {
        eventManager.clearImageDetailsEvent();
        this.image = imageDetailsEvent.getImage();
        this.imageList = imageDetailsEvent.getImageList();
        pagerAdapter.addAll(imageDetailsEvent.getImageList());
        this.pos = imageDetailsEvent.getPosition();
        mViewPager.setCurrentItem(pos);

    }


}
