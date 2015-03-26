package com.mobirizer.smartinvitationpro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardGridActivity extends Activity{
	private GridView gridview;
	private ImageView iv_greet,opt_btn;
	private TextView tv_title;
	private String fontPath = "fonts/SCRIPTBL.TTF";
	private Typeface tf ;
	private CustomGridViewAdapter cardAdapter;
	 private static final String AD_UNIT_ID = "ca-app-pub-1765898360821109/7125949074";
	private  Integer[] wedThumb = {
			R.drawable.wedding_t_1,
			R.drawable.wedding_t_2,
			R.drawable.wedding_t_3,
			R.drawable.wedding_t_4,
			R.drawable.wedding_t_5,
			R.drawable.wedding_t_6,
			R.drawable.wedding_t_7,
			R.drawable.wedding_t_8,
			R.drawable.wedding_t_9,
			R.drawable.wedding_t_10,
			R.drawable.wedding_t_11,
			R.drawable.wedding_t_12,
			R.drawable.wedding_t_13,
			R.drawable.wedding_t_14,
			R.drawable.wedding_t_15
	};

	private Integer[] birthThumb ={
			R.drawable.birthday_t_1,
			R.drawable.birthday_t_2,
			R.drawable.birthday_t_3,
			R.drawable.birthday_t_4,
			R.drawable.birthday_t_5,
			R.drawable.birthday_t_6,
			R.drawable.birthday_t_7,
			R.drawable.birthday_t_8,
			R.drawable.birthday_t_9,
			R.drawable.birthday_t_10,
			R.drawable.birthday_t_11,
			R.drawable.birthday_t_12,
			R.drawable.birthday_t_13,
			R.drawable.birthday_t_14,
			R.drawable.birthday_t_15
	};

	private Integer[] anniversaryThumb ={
			R.drawable.anniversary_t_1,
			R.drawable.anniversary_t_2,
			R.drawable.anniversary_t_3,
			R.drawable.anniversary_t_4,
			R.drawable.anniversary_t_5,
			R.drawable.anniversary_t_6,
			R.drawable.anniversary_t_7,
			R.drawable.anniversary_t_8,
			R.drawable.anniversary_t_9,
			R.drawable.anniversary_t_10,
			R.drawable.anniversary_t_11,
			R.drawable.anniversary_t_12,
			R.drawable.anniversary_t_13,
			R.drawable.anniversary_t_14,
			R.drawable.anniversary_t_15
	};

	private Integer[] engageThumb ={
			R.drawable.engagement_t_1,
			R.drawable.engagement_t_2,
			R.drawable.engagement_t_3,
			R.drawable.engagement_t_4,
			R.drawable.engagement_t_5,
			R.drawable.engagement_t_6,
			R.drawable.engagement_t_7,
			R.drawable.engagement_t_8,
			R.drawable.engagement_t_9,
			R.drawable.engagement_t_10,
			R.drawable.engagement_t_11,
			R.drawable.engagement_t_12,
			R.drawable.engagement_t_13,
			R.drawable.engagement_t_14,
			R.drawable.engagement_t_15
	};

	private Integer[] festThumb ={
			R.drawable.festival_t_1,
			R.drawable.festival_t_2,
			R.drawable.festival_t_3,
			R.drawable.festival_t_4,
			R.drawable.festival_t_5,
			R.drawable.festival_t_6,
			R.drawable.festival_t_7,
			R.drawable.festival_t_8,
			R.drawable.festival_t_9,
			R.drawable.festival_t_10,
			R.drawable.festival_t_11,
			R.drawable.festival_t_12,
			R.drawable.festival_t_13,
			R.drawable.festival_t_14,
			R.drawable.festival_t_15
	};

	private Integer[] partyThumb ={
			R.drawable.party_t_1,
			R.drawable.party_t_2,
			R.drawable.party_t_3,
			R.drawable.party_t_4,
			R.drawable.party_t_5,
			R.drawable.party_t_6,
			R.drawable.party_t_7,
			R.drawable.party_t_8,
			R.drawable.party_t_9,
			R.drawable.party_t_10,
			R.drawable.party_t_11,
			R.drawable.party_t_12,
			R.drawable.party_t_13,
			R.drawable.party_t_14,
			R.drawable.party_t_15
			
	};

		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.gridview);

		System.out.println("values for cat_id>>>>>>>>>>>>>"+getIntent().getExtras().getString("cat_id"));
		int cat_id = Integer.parseInt(getIntent().getExtras().getString("cat_id"));

		gridview = (GridView) findViewById(R.id.gridview);
		tv_title =(TextView)findViewById(R.id.tv_title);
		iv_greet =(ImageView)findViewById(R.id.iv_greet);
		opt_btn = (ImageView) findViewById(R.id.option_btn); 
		tf = Typeface.createFromAsset(getAssets(), fontPath);


		switch(cat_id) {
		case 1:
			tv_title.setText("Wedding");
			tv_title.setTypeface(tf);
			iv_greet.setBackgroundResource(R.drawable.wed1);
			cardAdapter =new CustomGridViewAdapter(this,wedThumb);
			gridview.setAdapter(cardAdapter);
			gridview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

					// Send intent to SingleViewActivity
					Intent i = new Intent(getApplicationContext(),EditCardActivity.class);
					//String cat_id = getIntent().getExtras().getString("cat_id");
					System.out.println("*****Category id*********"+getIntent().getExtras().getString("cat_id"));
					i.putExtra("cat_id", getIntent().getExtras().getString("cat_id"));
					i.putExtra("id", position);
					System.out.print("POSITION :"+position);
					startActivity(i);
				}
			});
			break;

		case 2:
			tv_title.setText("BirthDay");
			tv_title.setTypeface(tf);
			iv_greet.setBackgroundResource(R.drawable.bday);
			cardAdapter =new CustomGridViewAdapter(this,birthThumb);
			gridview.setAdapter(cardAdapter);
			gridview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

					// Send intent to SingleViewActivity
					Intent i = new Intent(getApplicationContext(),EditCardActivity.class);
					//String cat_id=getIntent().getExtras().getString("cat_id");
					System.out.println("*****Category id*********"+getIntent().getExtras().getString("cat_id"));
					i.putExtra("cat_id", getIntent().getExtras().getString("cat_id"));
					i.putExtra("id", position);
					System.out.print("POSITION :"+position);
					startActivity(i);
				}
			});
			break;

		case 3:
			tv_title.setText("Anniversary");
			tv_title.setTypeface(tf);
			iv_greet.setBackgroundResource(R.drawable.ann);
			cardAdapter =new CustomGridViewAdapter(this,anniversaryThumb);
			gridview.setAdapter(cardAdapter);
			gridview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

					// Send intent to SingleViewActivity
					Intent i = new Intent(getApplicationContext(),EditCardActivity.class);
					System.out.println("*****Category id*********"+getIntent().getExtras().getString("cat_id"));
					i.putExtra("cat_id", getIntent().getExtras().getString("cat_id"));
					i.putExtra("id", position);
					System.out.print("POSITION :"+position);
					startActivity(i);
				}
			});
			break;

		case 4:
			tv_title.setText("Engagement");
			tv_title.setTypeface(tf);
			iv_greet.setBackgroundResource(R.drawable.engm);
			cardAdapter =new CustomGridViewAdapter(this,engageThumb);
			gridview.setAdapter(cardAdapter);
			gridview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

					// Send intent to SingleViewActivity
					Intent i = new Intent(getApplicationContext(),EditCardActivity.class);
					System.out.println("*****Category id*********"+getIntent().getExtras().getString("cat_id"));
					i.putExtra("cat_id", getIntent().getExtras().getString("cat_id"));
					i.putExtra("id", position);
					System.out.print("POSITION :"+position);
					startActivity(i);
				}
			});
			break;

		case 5:
			tv_title.setText("Festival");
			tv_title.setTypeface(tf);
			iv_greet.setBackgroundResource(R.drawable.festi);
			cardAdapter =new CustomGridViewAdapter(this,festThumb);
			gridview.setAdapter(cardAdapter);
			gridview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

					// Send intent to SingleViewActivity
					Intent i = new Intent(getApplicationContext(),EditCardActivity.class);
					System.out.println("*****Category id*********"+getIntent().getExtras().getString("cat_id"));
					i.putExtra("cat_id", getIntent().getExtras().getString("cat_id"));
					i.putExtra("id", position);
					System.out.print("POSITION :"+position);
					startActivity(i);
				}
			});
			break;

		case 6:
			tv_title.setText("Party");
			tv_title.setTypeface(tf);
			iv_greet.setBackgroundResource(R.drawable.part);
			cardAdapter =new CustomGridViewAdapter(this,partyThumb);
			gridview.setAdapter(cardAdapter);
			gridview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View v,int position, long id) {

					// Send intent to SingleViewActivity
					Intent i = new Intent(getApplicationContext(),EditCardActivity.class);
					System.out.println("*****Category id*********"+getIntent().getExtras().getString("cat_id"));
					i.putExtra("cat_id", getIntent().getExtras().getString("cat_id"));
					i.putExtra("id", position);
					System.out.print("POSITION :"+position);
					startActivity(i);
				}
			});
			break;
		default:
			break;
		}
	}  
	
	/**
	 * Part of the activity's life cycle, StartAppAd should be integrated here. 
	 */
	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * Part of the activity's life cycle, StartAppAd should be integrated here
	 * for the home button exit ad integration.
	 */
	@Override
	public void onPause() {
		super.onPause();
	}
	
@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
}
	/**
	 * Part of the activity's life cycle, StartAppAd should be integrated here
	 * for the back button exit ad integration.
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(CardGridActivity.this, MainActivity.class);
		startActivity(i);
		CardGridActivity.this.finish();
	}
	
}
