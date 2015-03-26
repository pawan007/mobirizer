package adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mobirizer.locoreminder.R;
import com.mobirizer.locoreminder.SearchLocationActivity;

public class SearchActivityAdapter extends BaseAdapter {
	private ArrayList<String> mSearchList;
	// private ArrayList<String> fullname;
	// private ArrayList<String> geopoint;
	Activity activity;
	String fontPath;
	Typeface tf;
	ViewHolder viewHolder;
	private static LayoutInflater inflater = null;

	public SearchActivityAdapter(Activity applicationContext,
			ArrayList<String> namelist) {
		super();
		this.activity = applicationContext;
		this.mSearchList = namelist;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// return 0;
		return mSearchList.size();
	}

	@Override
	public Object getItem(int position) {

		// return null;
		return mSearchList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		fontPath = "fonts/Gabriola_0.ttf";
		tf = Typeface.createFromAsset(this.activity.getAssets(), fontPath);
		if (convertView == null) {
			try {
				convertView = inflater.inflate(
						R.layout.search_element_activity, null);
				viewHolder = new ViewHolder();
				viewHolder.textsearchlist = (TextView) convertView
						.findViewById(R.id.txtName);
				viewHolder.textsearchlist.setTextSize(25);
				viewHolder.textsearchlist.setTypeface(tf);

				convertView.setTag(viewHolder);
			} catch (InflateException e) {
				e.printStackTrace();
				Toast.makeText(activity, "Error Show", Toast.LENGTH_LONG)
						.show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(activity, "Error Show", Toast.LENGTH_LONG)
						.show();
			}

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textsearchlist.setText(mSearchList.get(position));
		viewHolder.textsearchlist.setOnClickListener(new OnItemClickListener(
				position));
		return convertView;

	}

	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			this.mPosition = position;
		}

		@Override
		public void onClick(View v) {

			SearchLocationActivity sct = (SearchLocationActivity) activity;
			sct.onItemClick(mPosition);

		}
	}

	public static class ViewHolder {
		public TextView textsearchlist;
	}

}
