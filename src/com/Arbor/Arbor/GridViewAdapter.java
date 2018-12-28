package com.Arbor.Arbor;

import java.util.ArrayList;

import com.Arbor.Arbor.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {

	private ArrayList<String> listCountry;
	private ArrayList<Integer> listFlag;
	private Activity activity;

	public GridViewAdapter(Activity activity, ArrayList<String> listCountry,
			ArrayList<Integer> listFlag) {
		super();
		this.listCountry = listCountry;
		this.listFlag = listFlag;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listCountry.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listCountry.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolder {
		public ImageView imgViewflag;
		public TextView txtViewtitle;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder view;
		LayoutInflater inflator = activity.getLayoutInflater();
		if (convertView == null) {
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.gridview_row, null);
			view.txtViewtitle = (TextView) convertView
					.findViewById(R.id.textview1);
			view.imgViewflag = (ImageView) convertView
					.findViewById(R.id.imageview1);
			convertView.setTag(view);
		} else {
			view = (ViewHolder) convertView.getTag();
		}
		view.txtViewtitle.setText(listCountry.get(position));
		view.imgViewflag.setImageResource(listFlag.get(position));
		return convertView;
	}

}
