package com.Arbor.Arbor;

import java.util.Calendar;

import com.Arbor.Arbor.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

public class SetDiary extends Activity implements OnClickListener {
	
	static final int Date_Dialog = 0;//날짜 변경 변수
	private int Year, Month, Day;//날짜 변경 변수
	private ImageButton diarybutton;
	protected static final int REQ_CODE_PICK_IMAGE = 0;
	private Button writediary;
	String filename,path,editTitle,editContent;
	private TextView date;
	private TextView title;
	private TextView content;
	SQLiteDatabase db;
	MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this,"diary.db",null, 1);
//	date = 날짜 설정, title = 제목 설정, content = 내용 설정
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setdiary);
		diarybutton = (ImageButton) findViewById(R.id.selectpicture_diary);
		diarybutton.setOnClickListener(this);
		date = (TextView) findViewById(R.id.edit_day);
		date.setOnClickListener(this);
		writediary = (Button) findViewById(R.id.writediary);
		writediary.setOnClickListener(this);
		Button backkey = (Button)findViewById(R.id.backkey);
		backkey.setOnClickListener(this);
		title = (TextView)findViewById(R.id.edit_title);
		content = (TextView)findViewById(R.id.edit_content);
		final Calendar cal = Calendar.getInstance();
		Year = cal.get(Calendar.YEAR);
		Month = cal.get(Calendar.MONTH);
		Day = cal.get(Calendar.DAY_OF_MONTH);
	}
	//날짜 설정
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case Date_Dialog:
			return new DatePickerDialog(this, mDateSetListener, Year, Month,
					Day);
		}
		return null;
	}
	//날짜 설정
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			Year = year;
			Month = monthOfYear;
			Day = dayOfMonth;
			updateDisplay();
		}
	};
	//날짜 설정
	private void updateDisplay() {
		date.setText(new StringBuilder().append(Year).append("-")
				.append(Month + 1).append("-").append(Day).append(""));
	}
	//저장, 날짜, 이미지 선택 버튼 구현
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent init = new Intent(this,MyDiary.class);
		switch (v.getId()) {
		case R.id.edit_day: {
			showDialog(Date_Dialog);
			break;
		}
		case R.id.selectpicture_diary: {
			init = new Intent(Intent.ACTION_PICK);
			init.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
			init.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(init, REQ_CODE_PICK_IMAGE);
			break;
		}
		case R.id.writediary:
			saveData();
			startActivity(init);
			finish();
			break;
		case R.id.backkey:
			startActivity(init);
			finish();
			break;
		}
		
	}
	//갤러리에서 불러오기
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_CODE_PICK_IMAGE) {
			if (resultCode == Activity.RESULT_OK) {
				diarybutton.setImageURI(data.getData());
				path = data.getDataString();
			}
		}
	}
	//데이터 db에 저장
	public void saveData() {
		filename = date.getText().toString();
		editTitle = title.getText().toString();
		editContent = content.getText().toString();
		insert(filename,editTitle,editContent,path);
	}
	public void insert(String date, String title, String content, String uri){
		db = helper.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put("date",date);
		value.put("title",title);
		value.put("content",content);
		value.put("uri",uri);
		db.insert("diary",null, value);
	}
}
