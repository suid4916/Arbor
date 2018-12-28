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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class Search extends Activity implements OnClickListener{
	String plant = "";
	ArrayList<Integer> image = new ArrayList<Integer>();
	ArrayList<String> items;
	private GridViewAdapter adapter;
	private GridView gv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		plant = getIntent().getStringExtra("plant");
		EditText searchLine = (EditText) findViewById(R.id.secondsearch);
		searchLine.setText(plant);
		Button backkey = (Button)findViewById(R.id.backkey);
		backkey.setOnClickListener(this);
		Append();
		adapter = new GridViewAdapter(this, items, image);
		gv = (GridView)findViewById(R.id.gridView2);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent init = new  Intent(Search.this,Specific.class);
				init.putExtra("specific", items.get(arg2));
				init.putExtra("image", image.get(arg2));
				startActivity(init);
			}
		});
	}

	public void Append() {
		if (plant.equals("난")) {
			items = new ArrayList<String>();
			XmlPullParser orchid = getResources().getXml(R.xml.orchid);
			try {
				while (orchid.getEventType() != XmlPullParser.END_DOCUMENT) {
					// 태그의 첫번째 속성일 때,
					if (orchid.getEventType() == XmlPullParser.START_TAG) {
						// 이름이 "custom" 일때, 첫번째 속성값을 ArrayList에 저장
						if (orchid.getName().equals("orchid")) {
							items.add(orchid.getAttributeValue(0));
						}

					}
					orchid.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			image.add(R.drawable.taeyanggum);
			image.add(R.drawable.sanchunjo);
			image.add(R.drawable.dalmajo);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.backkey:
			finish();
		}
	}
}
