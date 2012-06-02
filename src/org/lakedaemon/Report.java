package org.lakedaemon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

public class Report extends Handler {
	public static final int ALERT = -1; // show error messages
	public static final int DONE = 0; // stop showing progress dialog
	public static final int PROGRESS = 1; // show indeterminate progress dialog
	public static final int STEP = 2; // show determinate progress dialog
	public static final int TOAST = 3; // toast
	public static final int DOWNLOAD = 4; // downloading
	public static final int BUILD = 5; // start the building process (including downloading things)

	

	public boolean polling = false;
	
	public Report(Activity activity) {
		this.activity = activity;
	}

	private Activity activity = null;	
	private ProgressDialog progressDialog = null;  //  for long operations

	
	private void alert(String string) {

		
		new AlertDialog.Builder(activity)  
		//	.setTitle(getString(R.string.filesNeeded))  
		.setMessage(string)  
		.setPositiveButton(R.string.buttonExit,  
				new AlertDialog.OnClickListener() {  
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();
			}  
		})  
		.setCancelable(false)  
		.create()  
		.show();
	}

	public boolean isWorking() {
		return false;
	}
	
	public void build(Message msg) {}

	public void poll() {}
	
	public String title() {
		return null;
	}
	
	public void progress(Message msg) {
		Boolean indeterminate = (msg.what == PROGRESS);
		if (progressDialog == null || indeterminate != progressDialog.isIndeterminate()) {
			if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
			progressDialog = new ProgressDialog(activity);
			progressDialog.setCancelable(false);
			progressDialog.setProgressStyle(indeterminate ? ProgressDialog.STYLE_SPINNER : ProgressDialog.STYLE_HORIZONTAL);
		}
		if (msg.obj != null) progressDialog.setMessage((String) msg.obj);
		if (title() != null)  progressDialog.setTitle(title());
		
		if (!indeterminate) progressDialog.setProgress(msg.arg1);                
		if (!progressDialog.isShowing()) progressDialog.show();
		return;
	}

	public void handleMessage(Message msg) {
		switch(msg.what) {
		case ALERT:
			alert((String) msg.obj);
			return;
		case DONE:
			if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
			progressDialog = null;
			return;
		case DOWNLOAD:
			poll();
			if (isWorking()) return;
		case PROGRESS:
		case STEP:
			progress(msg);
			return;
		case TOAST:
			Toast toast = Toast.makeText(activity, (String) msg.obj, 10);
			toast.setGravity(Gravity.BOTTOM, 0, 20);
			toast.show();
			return;
		case BUILD:
			build(msg);
			return;
		}	
	}

}
