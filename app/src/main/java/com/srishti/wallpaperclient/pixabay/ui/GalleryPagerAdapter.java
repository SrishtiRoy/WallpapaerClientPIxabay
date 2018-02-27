package com.srishti.wallpaperclient.pixabay.ui;
import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

import com.srishti.wallpaperclient.pixabay.event.EventManager;
import com.srishti.wallpaperclient.pixabay.event.ImageDetailsEvent;
import com.srishti.wallpaperclient.pixabay.model.Image;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {

	private List<Image> imageList;
	private EventManager eventManager;
	private Context ctx;


	public GalleryPagerAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
		this.ctx=activity;
		this.imageList = new ArrayList<>();

	}

	@Override
	public Fragment getItem(int position) {
		try {

			return GalleryPagerFragment.create(position, imageList.get(position));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int getCount() {
			return imageList != null ? imageList.size() : 0;

	}

	public void addAll(List<Image> imageList) {
		this.imageList.addAll(imageList);
		notifyDataSetChanged();
	}


}