package com.mobirizer.autotasker;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RecentlySearchItemAdapter extends BaseAdapter   implements OnClickListener {
    
    private Activity activity;
    private ArrayList<String> data;
    private static LayoutInflater inflater=null;
    public Resources res;
//    ViewHolder holder;
//    ListModel tempValues=null;
    int i=0;
    
    public RecentlySearchItemAdapter(Activity act, ArrayList<String> data) {
    	
        activity = act;
        this.data=data;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
    }

    public int getCount() {
    	
    	if(data.size()<=0)
    		return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
//    public static class ViewHolder{
//        public TextView text;
////        public ImageView image;
//    }

    public View getView(int position, View convertView, ViewGroup parent) {
        	
				try {
					convertView=inflater.inflate(R.layout.recentlyvieweditem, null);
					TextView recentplace=(TextView) convertView.findViewById(R.id.recentlysearch_text);
     	recentplace.setText(data.get(position).toString().substring(0, data.get(position).toString().indexOf("*")));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//	         convertView.setOnClickListener(new OnItemClickListener(position));
      
        return convertView;
    }
    
    @Override
    public void onClick(View v) {
            Log.v("CustomAdapter", "=====Row button clicked");
    }
    
    private class OnItemClickListener  implements OnClickListener{           
        private int mPosition;
        
        OnItemClickListener(int position){
        	 mPosition = position;
        }
        
        @Override
        public void onClick(View arg0) {
            LocationActivity sct = (LocationActivity)activity;
        	sct.onItemClick(mPosition);
        }               
    }   
}