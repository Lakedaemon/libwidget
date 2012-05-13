package org.lakedaemon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class GridSpinner extends TextView implements OnClickListener {
	private Dialog dialog = null;
	private AdapterView.OnItemClickListener gridListener = null;
	private SpinnerAdapter adapter = null;

	public GridSpinner(Context context) {
		super(context);
		setOnClickListener(this);
	}
	
	public GridSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}
	
	public GridSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnClickListener(this);
	}
	
	public void setAdapter(SpinnerAdapter adapter) {
		this.adapter = adapter;
	}

	public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
		gridListener = listener;
	}




	
	public void setSelectedId(int position) {
		if (adapter != null) setText((CharSequence) adapter.getItem(position));
	}



	public void onClick(View view) {
		GridView  gridView = (GridView) ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.grid_spinner_grid, null, false);

		gridView.setAdapter((ListAdapter) adapter); 
		gridView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
				setText("" + (position + 1));
				dialog.dismiss();
				if (gridListener != null) gridListener.onItemClick(parentView, view, position, id);
			}

		});
		dialog = new AlertDialog.Builder(getContext()).setView(gridView).create();
		dialog.show();
	}


}
