package com.Arbor.Arbor;

import java.util.ArrayList;
import java.util.List;

import com.Arbor.Arbor.R;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SelectArbor extends ListActivity {
	private Handler handle = new Handler();
	private BluetoothAdapter blue = BluetoothAdapter.getDefaultAdapter();
	private boolean finish;
	private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	private Runnable discover = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			blue.startDiscovery();
			for (;;) {
				if (finish) {
					break;
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Toast.makeText(SelectArbor.this, "중단되었습니다.",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}
	};
	private BroadcastReceiver foundreceive = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			devices.add(device);
			showDevices();
		}
	};

	private void showDevices() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		if (devices.size() > 0) {
			for (int i = 0, size = devices.size(); i < size; i++) {
				StringBuilder b = new StringBuilder();
				BluetoothDevice d = devices.get(i);
				b.append(d.getAddress());
				b.append("\n");
				b.append(d.getName());
				String s = b.toString();
				list.add(s);
			}
		} else {
			list.add("주변에 사용가능한 기기가 없습니다");
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		handle.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setListAdapter(adapter);
			}
		});

	}

	private BroadcastReceiver discoverreceive = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			unregisterReceiver(foundreceive);
			unregisterReceiver(this);
			finish = true;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.lable);
		// if (!blue.isEnabled()) {
		// finish();
		// }
		IntentFilter discoverfilt = new IntentFilter(
				BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(discoverreceive, discoverfilt);
		IntentFilter foundfilt = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(foundreceive, foundfilt);
		LoadingBluetooth.indeterminate(SelectArbor.this, handle, "Scanning",
				discover, new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						for (; blue.isDiscovering();) {
							blue.cancelDiscovery();
						}
						arg0.dismiss();
						finish = true;
					}
				}, true);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Intent result = new Intent();
		result.putExtra(BluetoothDevice.EXTRA_DEVICE, devices.get(position));
		setResult(RESULT_OK, result);
		finish();
	}

}
