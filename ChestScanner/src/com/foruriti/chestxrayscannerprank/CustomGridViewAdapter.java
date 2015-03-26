package com.foruriti.chestxrayscannerprank;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
public class CustomGridViewAdapter extends BaseAdapter{
	   private Context ctx;
	   private Integer a[] = new Integer[8];

	   // Constructor
	   public CustomGridViewAdapter(Context ctx, Integer[] a) {
	     this.ctx = ctx;
	      this.a= a;
	   }

	   public int getCount() {
	      return a.length;
	   }

	   public Object getItem(int position) {
	      return null;
	   }

	   public long getItemId(int position) {
	      return 0;
	   }

	   // create a new ImageView for each item referenced by the Adapter
	   public View getView(int position, View convertView, ViewGroup parent) {
		   ImageView imageView;
		      if (convertView == null) {
		      imageView = new ImageView(ctx);
		      imageView.setLayoutParams(new GridView.LayoutParams(250,250));
		      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		      imageView.setPadding(4, 4, 4, 4);
		      } else {
		      imageView = (ImageView) convertView;
		      }

		      imageView.setImageResource(a[position]);
		      return imageView;

		}

	}





