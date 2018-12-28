package com.Arbor.Arbor;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {
	private long backKeypressTime = 0;
	private Toast toast;
	
	private Activity activity;
	
	public BackPressCloseHandler(Activity context){
		this.activity = context;
	}
	
	public void onBackPressed(){
		if(System.currentTimeMillis() > backKeypressTime + 2000){
			backKeypressTime = System.currentTimeMillis();
			showGuide();
			return;
		}
		else if(System.currentTimeMillis()<=backKeypressTime + 2000){
			activity.finish();
			toast.cancel();
		}
	}
	public void showGuide(){
		toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
		toast.show();
	}
}
