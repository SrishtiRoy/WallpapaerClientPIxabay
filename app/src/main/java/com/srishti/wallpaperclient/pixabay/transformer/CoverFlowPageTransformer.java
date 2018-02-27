package com.srishti.wallpaperclient.pixabay.transformer;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CoverFlowPageTransformer implements android.support.v4.view.ViewPager.PageTransformer {

	public void transformPage(View view, float position) {
		view.setRotationY(position * -30);
	}
}