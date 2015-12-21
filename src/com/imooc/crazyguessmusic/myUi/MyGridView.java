package com.imooc.crazyguessmusic.myUi;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.imooc.crazyguessmusic.R;
import com.imooc.crazyguessmusic.modle.IWordClickListener;
import com.imooc.crazyguessmusic.modle.WordButton;
import com.imooc.crazyguessmusic.util.ViewUtil;

public class MyGridView extends GridView{
	private ArrayList<WordButton> mButtonList = new ArrayList<WordButton>();
	private MyAdapter mAdapter;
	private Context mContext;
	private Animation mScaleAnimation;
	private IWordClickListener mWordClickListener;

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mAdapter = new MyAdapter();
		this.setAdapter(mAdapter);
	}

	/**
	 * 更新数据方法
	 * 
	 * @param list
	 */
	public void updateData(ArrayList<WordButton> list) {
		mButtonList = list;
		setAdapter(mAdapter);
	}

	class MyAdapter extends BaseAdapter  {

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
			final WordButton holder ;
			if (view == null) {
				view = ViewUtil.getInflatedView(mContext,
						R.layout.self_ui_gridview_item);
				holder = mButtonList.get(position);
				holder.setmIndex(position);
				Button btn = (Button) view.findViewById(R.id.item_button);
				//设置按钮动画
				mScaleAnimation = AnimationUtils.loadAnimation(mContext, R.anim.scale_button);
				//设置动画延迟
				mScaleAnimation.setStartOffset(position*100);
				holder.setmButton(btn);
				
				view.setTag(holder);
			} else {
				holder = (WordButton) view.getTag();
			}
			// 给按钮设置他本身的文字
			Button button = holder.getmButton();
			button.setText(holder.getmWordString());
			//设置View点击事件(观察者模式)
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mWordClickListener!=null){
						mWordClickListener.onWordClick(holder);
					}
				}
			});
			//开启动画
			view.startAnimation(mScaleAnimation);
			//点击事件
			
			return view;
		}

	}
	/**
	 * Button监听点击事件接口
	 * @param listener
	 */
	public void setOnWordClickListener(IWordClickListener listener){
		mWordClickListener = listener;
	}
}
