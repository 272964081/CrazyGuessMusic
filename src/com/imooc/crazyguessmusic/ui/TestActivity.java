package com.imooc.crazyguessmusic.ui;

import java.util.ArrayList;

import com.imooc.crazyguessmusic.R;
import com.imooc.crazyguessmusic.modle.WordButton;
import com.imooc.crazyguessmusic.myUi.MyGridView;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {
	private MyGridView v;
	private ArrayList<WordButton> mArrayList = new ArrayList<WordButton>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		v = (MyGridView) findViewById(R.id.gv_test);
		initCurrentStageData();
	}
	
	public void initCurrentStageData(){
		//获取数据
		mArrayList = getButtonList();
		//更新MyGridView
		v.updateData(mArrayList);
	}
	
	public ArrayList<WordButton> getButtonList(){
		//获得所有待选文字 TODO
		ArrayList<WordButton> data = new ArrayList<WordButton>();
		for(int i=0;i<24*3;i++){
			WordButton button = new WordButton();
			button.setmIndex(i);
			button.setmWordString("测");
			data.add(button);
		}
		return data;
	}

}
