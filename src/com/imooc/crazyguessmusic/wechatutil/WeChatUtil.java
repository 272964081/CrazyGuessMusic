package com.imooc.crazyguessmusic.wechatutil;

import android.content.Context;
import android.graphics.Bitmap;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WeChatUtil {
	
	public static final String APP_ID = "wxf6e5103feaf39d09";
	
	public static final int THUMB_SIZE = 150;
	
	private IWXAPI mApi;
	
	private static Context mContext;
	
	private static WeChatUtil mWeChatUtil;
	
	
	
	private WeChatUtil(){
		mApi = WXAPIFactory.createWXAPI(mContext, APP_ID, false);
		mApi.registerApp(APP_ID);
	}
	
	public static WeChatUtil getInstance(Context context){
		if(mWeChatUtil==null){
			mContext = context;
			mWeChatUtil = new WeChatUtil();
		}
		return mWeChatUtil;
	}
	
	/**
	 * 发送文本到微信朋友圈
	 * @param text
	 */
	public void sendRequest(String text){
		String msg = text;
		if(msg.length()==0||msg==null){
			return;
		}
		
		WXTextObject textObj = new WXTextObject();
		textObj.text = text;
		
		WXMediaMessage wxMsg = new WXMediaMessage();
		wxMsg.mediaObject = textObj;
		wxMsg.description = text;
		
		//构造Req
		
		SendMessageToWX.Req  req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = wxMsg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		//发送
		mApi.sendReq(req);
	
	}
	/**
	 * 发送图片到微信朋友圈
	 * @param bitmap
	 */
	public void sendRequest(Bitmap bitmap){
		Bitmap myBitmap = bitmap;
		WXImageObject iObj = new WXImageObject(myBitmap);
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = iObj;
		
		//设置缩略图
		Bitmap thumbMap = Bitmap.createScaledBitmap(myBitmap, THUMB_SIZE, THUMB_SIZE, true);
		myBitmap.recycle();
		msg.thumbData = Util.bmpToByteArray(thumbMap, true);
		
		//构造Req
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "img"+String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		
		//调用API发送
		mApi.sendReq(req);
		
	}

}
