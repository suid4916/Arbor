package com.Arbor.Arbor;

import com.Arbor.Arbor.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Specific extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specific);
		String top = getIntent().getStringExtra("specific");
		int image = getIntent().getIntExtra("image", 0);
		TextView tv = (TextView)findViewById(R.id.setitem);
		tv.setText(top);
		ImageView iv = (ImageView)findViewById(R.id.specificimage);
		iv.setImageResource(image);
		Button backkey = (Button)findViewById(R.id.backkey);
		backkey.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
}
