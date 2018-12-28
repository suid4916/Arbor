package com.Arbor.Arbor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;

import com.Arbor.Arbor.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity implements OnClickListener {
	private long now = System.currentTimeMillis();
	private BackPressCloseHandler hanlder;
	Date date = new Date(now);
	BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startActivity(new Intent(this, Loading.class));
		Button status = (Button) findViewById(R.id.status);
		status.setOnClickListener(this);
		Button diary = (Button) findViewById(R.id.diary);
		diary.setOnClickListener(this);
		Button plantbook = (Button) findViewById(R.id.plantbook);
		plantbook.setOnClickListener(this);
		countDay();
		hanlder = new BackPressCloseHandler(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.status: {
			if (!bluetooth.isEnabled()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("앱에서 블루투스를 사용하려 합니다.")
						.setPositiveButton("허용",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										bluetooth.enable();
										Intent init = new Intent(
												MainActivity.this,
												MyStatus.class);
										startActivity(init);
									}
								})
						.setNegativeButton("거부",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										Toast.makeText(
												MainActivity.this,
												"블루투스를 사용하지 않으면, \n온도, 습도 정보를 가져올수 없습니다!",
												Toast.LENGTH_SHORT).show();
										dialog.cancel();
									}
								});
				AlertDialog dialog = builder.create();
				dialog.show();
				// dialog.dismiss();
			} else {
				Intent init = new Intent(MainActivity.this, MyStatus.class);
				startActivity(init);
			}
			break;
		}
		case R.id.diary: {
			Intent init = new Intent(this, MyDiary.class);
			startActivity(init);
			break;
		}
		case R.id.plantbook:
		//	Intent init = new Intent(this, PlantBook.class);
		//	startActivity(init);
			AlertDialog.Builder builder = new  AlertDialog.Builder(this);
			builder.setMessage("이 기능을 실행하려면 관리자 권한이 필요합니다.").setPositiveButton("확인", null);
			AlertDialog dialog = builder.create();
			dialog.show();
			break;
		default:
			break;
		}
	}

	public void countDay() {
		try {
			FileInputStream fis = openFileInput("bringdate");
			byte[] buffer = new byte[1024];
			StringBuilder sb = new StringBuilder();
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				sb.append(new String(buffer, 0, len));
			}
			Calendar c = Calendar.getInstance();
			Calendar today = Calendar.getInstance();
			Calendar standard = Calendar.getInstance();
			int Year = c.get(Calendar.YEAR);
			int Month = c.get(Calendar.MONTH);
			int Day = c.get(Calendar.DAY_OF_MONTH);
			today.set(Year, (Month + 1), Day);
			String buf = sb.toString();
			String[] arr = buf.split("-");
			int todayYear = (int) Integer.parseInt(arr[0]);
			int todayMonth = (int) Integer.parseInt(arr[1]);
			int todayDay = (int) Integer.parseInt(arr[2]);
			standard.set(todayYear, todayMonth, todayDay);
			long b = (today.getTimeInMillis() - standard.getTimeInMillis()) / 1000;
			long a = b / (60 * 60 * 24);
			TextView tv = (TextView) findViewById(R.id.countday);
			String result = String.valueOf((a + 1));
			tv.setText(result);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		countDay();
	}

	@Override
	public void onBackPressed() {
		hanlder.onBackPressed();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
