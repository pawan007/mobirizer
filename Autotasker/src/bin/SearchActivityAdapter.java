package bin;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mobirizer.autotasker.LocationActivity;
import com.mobirizer.autotasker.R;


//this class using for set search list value..which comes from location search in location activity
public class SearchActivityAdapter extends BaseAdapter {
	private ArrayList<String> mSearchList;
	private ArrayList<String> mSearchListforshow;
	Activity activity;
	ViewHolder viewHolder;
	private static LayoutInflater inflater = null;
	public SearchActivityAdapter(Activity applicationContext,
			ArrayList<String> namelist,ArrayList<String> namelistforshow) {
		super();
		this.activity = applicationContext;
		this.mSearchList = namelist;
		this.mSearchListforshow=namelistforshow;
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
		if (convertView == null) {
			try {
				convertView = inflater.inflate(
						R.layout.search_element_activity, null);
				viewHolder = new ViewHolder();				
				viewHolder.textsearchlistforclick = (TextView) convertView
						.findViewById(R.id.txtName1);
				viewHolder.textsearchlistforclick.setTextSize(20);
				viewHolder.textsearchlist = (TextView) convertView
						.findViewById(R.id.txtName);
				viewHolder.textsearchlist.setTextSize(16);
				viewHolder.textsearchlist.setTextColor(Color.WHITE);
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
		viewHolder.textsearchlistforclick.setText(mSearchListforshow.get(position));
		viewHolder.textsearchlistforclick.setOnClickListener(new OnItemClickListener(position));
//		convertView.setOnClickListener(new OnItemClickListener(position));
		return convertView;

	}

	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			this.mPosition = position;
		}

		@Override
		public void onClick(View v) {
			LocationActivity sct = (LocationActivity) activity;
			sct.onItemClick(mPosition);

		}
	}

	public static class ViewHolder {
		public TextView textsearchlist,textsearchlistforclick;
	}

}
