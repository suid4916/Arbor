package com.Arbor.Arbor;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import com.Arbor.Arbor.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PlantBook extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plantbook);
		Button backkey = (Button)findViewById(R.id.backkey);
		backkey.setOnClickListener(this);
		ArrayList<String> items = new ArrayList<String>();
		XmlPullParser Plantlist = getResources().getXml(R.xml.plantlist);
		try {
			while (Plantlist.getEventType() != XmlPullParser.END_DOCUMENT) {
				// 태그의 첫번째 속성일 때,
				if (Plantlist.getEventType() == XmlPullParser.START_TAG) {
					// 이름이 "custom" 일때, 첫번째 속성값을 ArrayList에 저장
					if (Plantlist.getName().equals("plant")) {
						items.add(Plantlist.getAttributeValue(0));
					}

				}
				// 다음 태그로 이동
				Plantlist.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.simpleitem, items);
		ListView lv = (ListView) findViewById(R.id.resultView);
		lv.setAdapter(adapter);
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent init = new Intent(getApplicationContext(),
						Search.class);
				init.putExtra("plant", ((TextView) arg1).getText().toString());
				startActivity(init);
			}
		};
		lv.setOnItemClickListener(listener);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backkey:
			finish();
			break;

		default:
			break;
		}
	}
}
