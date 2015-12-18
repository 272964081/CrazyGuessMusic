package com.imooc.crazyguessmusic.myUi;

import java.util.ArrayList;

import com.imooc.crazyguessmusic.R;
import com.imooc.crazyguessmusic.modle.WordButton;
import com.imooc.crazyguessmusic.util.ViewUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class MyGridView extends GridView {
	private ArrayList<WordButton> mButtonList = new ArrayList<WordButton>();
	private MyAdapter mAdapter;
	private Context mContext;
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mAdapter = new MyAdapter();
		this.setAdapter(mAdapter);
	}
	
	/**
	 * 更新数据方法
	 * @param list
	 */
	public void updateData(ArrayList<WordButton> list){
		mButtonList = list;
		setAdapter(mAdapter);
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mButtonList.size();
		}

		@Override
		public Object getItem(int position) {
			return mButtonList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			WordButton holder = null;
			if(view==null){
				view = ViewUtil.getInflatedView(mContext, R.layout.self_ui_gridview_item);
				holder = mButtonList.get(position);
				holder.setmIndex(position);
				holder.setmButton((Button)view.findViewById(R.id.item_button));
				view.setTag(holder);
			}else{
				holder =  (WordButton) view.getTag();
			}
			//给按钮设置他本身的文字
			holder.getmButton().setText(holder.getmWordString());
			
			return view;
		}
	}
}
