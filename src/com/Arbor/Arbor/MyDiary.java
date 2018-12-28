package com.Arbor.Arbor;

import com.Arbor.Arbor.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class MyDiary extends Activity implements OnClickListener {
	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	LinearLayout ll;
	int setTextId = 1;
	TextView tv;
	Intent init;
	String name;
	SQLiteDatabase db;
	MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, "diary.db", null, 1);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydiary);
		Button bt = (Button) findViewById(R.id.writediary);
		bt.setOnClickListener(this);
		Button backkey = (Button)findViewById(R.id.backkey);
		backkey.setOnClickListener(this);
		ll = (LinearLayout) findViewById(R.id.diarylist);
//		params.setMargins(20, 20, 20, 20);
		loadDiary();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.writediary: {
			init = new Intent(this, SetDiary.class);
			startActivity(init);
			finish();
			break;
		}
		case R.id.backkey:
			finish();
			break;
		}
	}

	public void loadDiary() {
		db = helper.getReadableDatabase();
		Cursor c =db.query("diary", null, null, null, null, null, null);
		while(c.moveToNext()){
		name = c.getString(c.getColumnIndex("date"));
		tv = new TextView(this);
		tv.setText(name);
		tv.setLayoutParams(params);
		tv.setTextSize(20);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		tv.setTextColor(Color.BLACK);
		tv.setPadding(20, 20, 20, 20);
		tv.setBackgroundColor(Color.WHITE);
		tv.setId(setTextId);
		tv.setOnClickListener(textClick);
		setTextId++;
		ll.addView(tv);
		}
	}

	View.OnClickListener textClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			name = ((TextView) v).getText().toString();
			init = new Intent(MyDiary.this, ViewDiary.class);
			init.putExtra("textID", name);
			startActivity(init);
			finish();
		}
	};
}
