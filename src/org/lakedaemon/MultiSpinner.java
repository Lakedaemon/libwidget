package org.lakedaemon;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.TextView;

public class MultiSpinner extends TextView implements
OnMultiChoiceClickListener, OnClickListener {

	private CharSequence[] items;
	private boolean[] values;
	private String emptyText;
	private MultiSelectionListener listener;
	private Dialog dialog;

	public MultiSpinner(Context context) {
		super(context);
		setOnClickListener(this);
	}

	public MultiSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	public MultiSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);
	}

	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		values[which] = isChecked;
	}

	private void refreshSummary() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		for (int i = 0; i < items.length; ++i) if (values[i]) {

			if (builder.length() > 0) builder.append(", ");
			builder.append(items[i]);
		}
		if (builder.length() >0) {
			builder.setSpan(new RelativeSizeSpan((float) 0.5), 0, builder.length(), 0);
			setText(builder);
		} else setText(emptyText);
	}

	public void onClick(View view) {
		dialog = new AlertDialog.Builder(getContext())
		.setMultiChoiceItems(items, values, this)
		.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				refreshSummary();
				listener.onItemsSelected(values);
				dialog.dismiss();
			}
		}).create();
		dialog.show();
	}

	public void setItems(CharSequence[] items, boolean[] values, String emptyText, MultiSelectionListener listener) {
		this.items = items;
		this.emptyText = emptyText;
		this.listener = listener;
		this.values = values;
		refreshSummary();
	}	


	public interface MultiSelectionListener {
		public void onItemsSelected(boolean[] selected);
	}

}
