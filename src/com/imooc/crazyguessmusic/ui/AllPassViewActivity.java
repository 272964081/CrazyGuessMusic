package com.imooc.crazyguessmusic.ui;

import com.imooc.crazyguessmusic.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class AllPassViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_pass_view);
		FrameLayout view = (FrameLayout) findViewById(R.id.layout_top_coins);
		view.setVisibility(View.GONE);
	}
}	
