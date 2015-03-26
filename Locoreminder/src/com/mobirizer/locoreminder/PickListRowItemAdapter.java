package com.mobirizer.locoreminder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;



import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PickListRowItemAdapter extends ArrayAdapter<String> {

	public LayoutInflater inflater;
	public View rowView;
	public TextView ider;
	public TextView name;
	public TextView address;
	public ImageView checkImg;
	public final Context context;
	private final String[] Ids;
	public final int rowResourceId;
	public FirstActivity pickAddressInstance;
	public ListView pickAddressList; 
	private int checkedRow;

	public PickListRowItemAdapter(Context context, int textViewResourceId, String[] objects, int theCheckedRow) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.Ids = objects;
		this.rowResourceId = textViewResourceId;
		this.checkedRow = theCheckedRow;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		pickAddressInstance = new FirstActivity();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = inflater.inflate(rowResourceId, parent, false);
//		name = (TextView) rowView.findViewById(R.id.text1);
//		address = (TextView) rowView.findViewById(R.id.text2);
//		address.setTag(position);
//		checkImg = (ImageView) rowView.findViewById(R.id.checked);
		final int id = Integer.parseInt(Ids[position]);
		String check_img = PickListModel.GetById(id).checkImg;
		name.setText(PickListModel.GetById(id).name);
		address.setText(PickListModel.GetById(id).address);
		InputStream aInputStream = null;
		try {
			aInputStream = context.getAssets().open(check_img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(aInputStream, null);
		checkImg.setImageDrawable(d);
		checkImg.setVisibility(View.INVISIBLE);
		if(id==checkedRow+1){
			checkImg.setVisibility(View.VISIBLE);
		}
		return rowView;
	}

	public void ShowChecked(){
		checkImg.setVisibility(View.VISIBLE);
	} 
	public void HideChecked(){
		checkImg.setVisibility(View.INVISIBLE);
	}
}


