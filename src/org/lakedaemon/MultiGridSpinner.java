package org.lakedaemon;


import org.lakedaemon.MultiSpinner.MultiSelectionListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class MultiGridSpinner extends TextView implements OnClickListener {	
	private boolean[] oldValues = null;	
	public boolean[] values = null;
	private CharSequence emptyText = null;
	private MultiSelectionListener listener = null;
	private int dialogLayoutId;


	private Dialog dialog = null;
	private AdapterView.OnItemClickListener gridListener = null;
	private SpinnerAdapter adapter = null;

	public MultiGridSpinner(Context context) {
		super(context);
		setOnClickListener(this);
	}

	public MultiGridSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	public MultiGridSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);
	}


	public void setAdapter(SpinnerAdapter adapter) {
		this.adapter = adapter;
	}

	public void setItems(int dialogLayoutId, SpinnerAdapter adapter, boolean[] values, CharSequence emptyText, MultiSelectionListener listener) {
		this.dialogLayoutId = dialogLayoutId;
		this.adapter = adapter;
		this.emptyText = emptyText;
		this.listener = listener;
		this.values = values;
		refreshSummary();
	}	

	public void refreshSummary() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		for (int i = 0; i < values.length; ++i) if (values[i]) {
			if (builder.length() > 0) builder.append(", ");
			builder.append((CharSequence) adapter.getItem(i));
		}
		setText(builder.length() > 0 ? builder : emptyText);
	}



	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		gridListener = listener;
	}

	public void onClick(View view) {
		oldValues = values.clone();
		final View  mainView = (View) ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(dialogLayoutId, null, false);

		Button button = (Button) mainView.findViewById(R.id.ok_button);
		button.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				listener.onItemsSelected(values);				
				if (dialog != null) dialog.dismiss();
				dialog = null;
			}
		});



		button = (Button) mainView.findViewById(R.id.cancel_button);
		button.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				values = oldValues;
				refreshSummary();
				// TODO, I'll haveto fix that
				if (dialog != null) dialog.dismiss();
				dialog = null;
			}
		});


		final GridView gridView = (GridView) mainView.findViewById(R.id.multi_spinner_grid);
		gridView.setAdapter((ListAdapter) adapter); 
		gridView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
				values[position] = !values[position];

				SpannableString string = new SpannableString(((TextView) view).getText().toString());
				if (!values[position]) string.setSpan(new ForegroundColorSpan(0x80808080), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


				((TextView) view).setText(string);
				refreshSummary();
				if (gridListener != null) gridListener.onItemClick(parentView, view, position, id);
			}

		});

		button = (Button) mainView.findViewById(R.id.clear_button);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				for (int a = 0; a < values.length; ++a) if (values[a]) {
					values[a] = false;
					if (a >= gridView.getFirstVisiblePosition() && a <= gridView.getLastVisiblePosition()) {
						TextView textView = (TextView) gridView.getChildAt(a - gridView.getFirstVisiblePosition());
						SpannableString string = new SpannableString(textView.getText().toString());
						string.setSpan(new ForegroundColorSpan(0x80808080), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						textView.setText(string);

					}
				}
				refreshSummary();
				listener.onItemsSelected(values);
			}
		});


		dialog = new AlertDialog.Builder(getContext()).setView(mainView).create();
		dialog.show();
	}
}
