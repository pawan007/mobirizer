package com.foruriti.chestxrayscannerprank;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomBaseAdapter extends BaseAdapter {
  Context context;
  List<RowItemModel> rowItems;
 
  public CustomBaseAdapter(Context context, List<RowItemModel> items) {
		this.context = context;
		this.rowItems = items;
	}
	
	/*private view holder class*/
	private class ViewHolder {
		ImageView imageView;
		TextView txtTitle;
	}
@Override
public int getCount() {
	// TODO Auto-generated method stub
	return rowItems.size();
}
@Override
public Object getItem(int position) {
	// TODO Auto-generated method stub
	return rowItems.get(position);
}
@Override
public long getItemId(int position) {
	// TODO Auto-generated method stub
	return rowItems.indexOf(getItem(position));
}
@Override
public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	ViewHolder holder=null;
	LayoutInflater mInflater =(LayoutInflater)
			context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	
	if(convertView==null) {
		convertView=mInflater.inflate(R.layout.listitem_rows,null);
		holder = new ViewHolder();
		holder.imageView=(ImageView) convertView.findViewById(R.id.scanthumb);
		holder.txtTitle=(TextView) convertView.findViewById(R.id.title);
		convertView.setTag(holder);
	}
	else {
		holder=(ViewHolder) convertView.getTag();
	}
	RowItemModel rowItem=(RowItemModel) getItem(position);
	holder.imageView.setImageResource(rowItem.getImageId());
	holder.txtTitle.setText(rowItem.getTitle());
	//tf = Typeface.createFromAsset(context.getAssets(), fontPath);
	holder.txtTitle.setTypeface(Typeface.createFromAsset(parent.getContext().getAssets(),"font/Cheap-Motel.ttf"));
	//text.setTypeface(Typeface.createFromAsset(getAssets(), "/*path to your font file */"));
	return convertView;
 }
}
