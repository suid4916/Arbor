package com.Arbor.Arbor;

import com.Arbor.Arbor.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

public class Loading extends Activity {
	
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.loading);
		
		Handler hd = new Handler();
		hd.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
			}
		}, 3000);
	}
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_BACK:
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
