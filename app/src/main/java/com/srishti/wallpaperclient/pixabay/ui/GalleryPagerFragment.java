/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.srishti.wallpaperclient.pixabay.ui;

import android.app.ActionBar;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.srishti.wallpaperclient.pixabay.event.EventManager;
import com.srishti.wallpaperclient.pixabay.event.ImageDetailsEvent;
import com.srishti.wallpaperclient.pixabay.listener.TouchImageView;
import com.srishti.wallpaperclient.pixabay.listener.UpdateFrag;
import com.srishti.wallpaperclient.pixabay.model.Image;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;


public class GalleryPagerFragment extends Fragment  implements UpdateFrag{
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    /**
     * The argument key for the image path that must be loaded.
     */
    public static final String ARG_PATH = "path";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * The fragment's image path, which is set to the argument value for {@link #ARG_PATH}.
     */
    private String mImagePath;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
	 * @param imagePath
	 */

    private Image image;
    private TouchImageView rootView;
    private WallpaperManager wallpaperManager;
    public static GalleryPagerFragment create(int pageNumber, Image image) {
    	GalleryPagerFragment fragment = new GalleryPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putString(ARG_PATH, image.getWebformatURL().toString());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void displaySetWallpaperError() {
        Toast.makeText(getActivity(), "Unable to set wallpaper.", Toast.LENGTH_SHORT).show();
    }

    private void displaySetWallpaperSuccess() {
        Toast.makeText(getActivity(), "Wallpaper has been changed.", Toast.LENGTH_SHORT).show();
    }
    @Override
    @SuppressLint("NewApi")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        wallpaperManager = WallpaperManager.getInstance(getActivity());


        mPageNumber = getArguments().getInt(ARG_PAGE);
        mImagePath = getArguments().getString(ARG_PATH);
        rootView = new TouchImageView(getActivity());
		rootView.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
				.LayoutParams.MATCH_PARENT));
        //Toast.makeText(getActivity(), "Wallpaper has been changed."+mPageNumber, Toast.LENGTH_SHORT).show();


        Picasso.with(getActivity()).load(mImagePath).into(rootView);
        ((ImageActivity)getActivity()).updateWallpaper(this);

        return rootView;
    }


    @Override
    public void updateWallper() {
        onClickSetWallpaperMenu();
    }

    private void onClickSetWallpaperMenu() {
        if (mImagePath != null) {
            Picasso.with(getActivity()).load(mImagePath).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    try {
                        wallpaperManager.setBitmap(bitmap);

                        displaySetWallpaperSuccess();
                    } catch (IOException e) {
                        displaySetWallpaperError();
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    displaySetWallpaperError();
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        }
    }


}
