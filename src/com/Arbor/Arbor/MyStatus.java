package com.Arbor.Arbor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

import com.Arbor.Arbor.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MyStatus extends Activity implements OnClickListener {
	protected static final int REQ_CODE_PICK_IMAGE = 0;
	private ImageButton selectPicture;
	private String path, savePath = "savePath", title = "bringdate", str,
			str2 = "";
	private final Calendar c = Calendar.getInstance();
	BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
	private static final int REQUEST_DISCOVERY = 0x1;
	private BluetoothSocket socket = null;
	// private String title = "bringdate";
	private InputStream is;
	private TextView tv[];
	private StringBuffer sbuffer;
	private StringBuilder build;
	// private String[] buffer;
	int countloop = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mystatus);
		Intent init = new Intent(this, SelectArbor.class);
		startActivityForResult(init, REQUEST_DISCOVERY);
		selectPicture = (ImageButton) findViewById(R.id.selectpicture);
		selectPicture.setOnClickListener(this);
		tv = new TextView[3];
		tv[0] = (TextView) findViewById(R.id.temperature);
		tv[1] = (TextView) findViewById(R.id.moisture);
		tv[2] = (TextView) findViewById(R.id.acid);
		Button back = (Button) findViewById(R.id.backkey);
		back.setOnClickListener(this);
		Button complete = (Button) findViewById(R.id.mystatussaver);
		complete.setOnClickListener(this);
		try {
			FileInputStream fis = openFileInput(savePath);
			byte[] buffer = new byte[1024];
			StringBuilder sb = new StringBuilder();
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				sb.append(new String(buffer, 0, len));
			}
			Uri uri = Uri.parse(sb.toString());
			selectPicture.setImageURI(uri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// LoadingStatus();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.selectpicture: {
			Intent init = new Intent(Intent.ACTION_PICK);
			init.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
			init.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(init, REQ_CODE_PICK_IMAGE);
			break;
		}
		case R.id.backkey: {
			finish();
			break;
		}
		case R.id.mystatussaver: {
			saveStatus();
			savePicture();
			finish();
			break;
		}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_CODE_PICK_IMAGE
				&& resultCode == Activity.RESULT_OK) {
			selectPicture.setImageURI(data.getData());
			path = data.getData().toString();
		}
		if (requestCode == REQUEST_DISCOVERY && data!=null) {
			//if (data != null) {
				final BluetoothDevice device = data
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				connect(device);
		//	}
		}
	}

	private void connect(BluetoothDevice device) {
		// TODO Auto-generated method stub
		try {
			socket = device.createInsecureRfcommSocketToServiceRecord(UUID
					.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			socket.connect();
			is = socket.getInputStream();
			int read = -1;
			final byte[] bytes = new byte[1024];
			while ((read = is.read(bytes)) > -1) {
				// final int count = read;
				build = new StringBuilder();
				for (int i = 0; i < 10; i++) {
					String s = Integer.toString(bytes[i]);
					build.append(s);
					build.append(",");
				}
				String s = build.toString();
				String[] chars = s.split(",");
				sbuffer = new StringBuffer();
				for (int i = 0; i < chars.length; i++) {
					if ((char) Integer.parseInt(chars[i]) == '\n') {
						countloop += 1;
						break;
					} else {
						sbuffer.append((char) Integer.parseInt(chars[i]));
					}
				}
				if (str != null) {
					str += sbuffer.toString();
				} else {
					str = sbuffer.toString();
				}
				if (countloop == 1) {
					for (int i = 0; i < chars.length; i++) {
						str2 += String.valueOf((char) Integer
								.parseInt(chars[i]));

					}
				} else if (countloop == 2) {
					String em = str2.substring(1, str2.length() / 2);
					String tempvalue = str2.substring(1);
					String em2 = tempvalue.substring((tempvalue.length() / 2),
							tempvalue.length());
					// Toast.makeText(MyStatus.this,tempvalue,
					// Toast.LENGTH_SHORT)
					// .show();
					tv[0].setText(em);
					tv[1].setText(em2);
					socket.close();
					// tv[2].setText(tempvalue);
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyStatus.this, "통신이 원활하지 않습니다.\n다시시도해주세요",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	public void saveStatus() {
		int Year = c.get(Calendar.YEAR);
		int Month = c.get(Calendar.MONTH);
		int Day = c.get(Calendar.DAY_OF_MONTH);
		String saveDay = String.format("%d-%d-%d", Year, Month + 1, Day);
		if (path != null) {
			try {
				FileOutputStream fos = openFileOutput(title, MODE_PRIVATE);
				fos.write(saveDay.getBytes());
				fos.close();
				Toast toast = Toast.makeText(this, "date save success",
						Toast.LENGTH_SHORT);
				toast.show();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * public void LoadingStatus() { String temp = null; try { temp = new
	 * TcpClient().execute().get(); buffer = temp.split(" "); for (int i = 0; i
	 * < buffer.length; i++) { tv[i].setText(buffer[i]); } } catch (Exception e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); } }
	 */

	public void savePicture() {
		if (path != null) {
			try {
				FileOutputStream fos = openFileOutput(savePath, MODE_PRIVATE);
				fos.write(path.getBytes());
				fos.close();
				// Toast toast = Toast.makeText(this, path,
				// Toast.LENGTH_SHORT);
				// toast.show();
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
