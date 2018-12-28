package com.Arbor.Arbor;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.Arbor.Arbor.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewDiary extends Activity implements OnClickListener {
	SQLiteDatabase db;
	MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, "diary.db", null,
			1);
	String result;
	Intent init;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewdiary);
		result = getIntent().getStringExtra("textID");
		TextView day = (TextView) findViewById(R.id.view_day);
		TextView title = (TextView) findViewById(R.id.view_title);
		TextView content = (TextView) findViewById(R.id.view_content);
		ImageView iv = (ImageView) findViewById(R.id.viewpicture_diary);
		Button delete = (Button) findViewById(R.id.viewdiaryremover);
		Button backkey = (Button)findViewById(R.id.backkey);
		backkey.setOnClickListener(this);
		db = helper.getReadableDatabase();
		delete.setOnClickListener(this);
		String query = "SELECT * FROM diary WHERE date = '" + result + "';";
		Cursor c = db.rawQuery(query, null);
		while (c.moveToNext()) {
			String date = c.getString(c.getColumnIndex("date"));
			String name = c.getString(c.getColumnIndex("title"));
			String cont = c.getString(c.getColumnIndex("content"));
			String uri = c.getString(c.getColumnIndex("uri"));
			day.setText(date);
			title.setText(name);
			content.setText(cont);

			if (c.getString(c.getColumnIndex("uri")) != null) {
				try {
					Uri url = Uri.parse(uri);
					Bitmap bm = Images.Media.getBitmap(getContentResolver(),
							url);
					iv.setImageBitmap(bm);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.viewdiaryremover:
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("정말로 현재 다이어리를 지우시겠습니까?\n" + "현재다이어리는 " + result
					+ "입니다.");
			alert.setCancelable(true);
			alert.setNegativeButton("취소", null);
			alert.setPositiveButton("확인",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							delete(result);
							init = new Intent(ViewDiary.this, MyDiary.class);
							startActivity(init);
							finish();
						}
					});
			alert.show();
			break;
		case R.id.backkey:
			init = new Intent(ViewDiary.this, MyDiary.class);
			startActivity(init);
			finish();
			break;
		}
	}

	public void delete(String date) {
		db = helper.getWritableDatabase();
		db.delete("diary", "date =?", new String[] { date });
	}
}
