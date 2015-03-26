package com.mobirizer.smartinvitationpro;




import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.common.utils.StringUtils;
import com.aviary.android.feather.library.Constants;
import com.aviary.android.feather.library.providers.FeatherContentProvider;
import com.aviary.android.feather.library.utils.DecodeUtils;
import com.aviary.android.feather.library.utils.ImageSizes;

public class EditCardActivity extends Activity {
	
	  
	private static final int SELECT_PICTURE =  1; 
	private String selectedImagePath;
	Point p;
	
	private  Integer[] weddingF = {
			R.drawable.wedding_f_1,
			R.drawable.wedding_f_2,
			R.drawable.wedding_f_3,
			R.drawable.wedding_f_4,
			R.drawable.wedding_f_5,
			R.drawable.wedding_f_6,
			R.drawable.wedding_f_7,
			R.drawable.wedding_f_8,
			R.drawable.wedding_f_9,
			R.drawable.wedding_f_10,
			R.drawable.wedding_f_11,
			R.drawable.wedding_f_12,
			R.drawable.wedding_f_13,
			R.drawable.wedding_f_14,
			R.drawable.wedding_f_15
	};

	private  Integer[] birthdayF = {
			R.drawable.birthday_f_1,
			R.drawable.birthday_f_2,
			R.drawable.birthday_f_3,
			R.drawable.birthday_f_4,
			R.drawable.birthday_f_5,
			R.drawable.birthday_f_6,
			R.drawable.birthday_f_7,
			R.drawable.birthday_f_8,
			R.drawable.birthday_f_9,
			R.drawable.birthday_f_10,
			R.drawable.birthday_f_11,
			R.drawable.birthday_f_12,
			R.drawable.birthday_f_13,
			R.drawable.birthday_f_14,
			R.drawable.birthday_f_15
	};

	private  Integer[] anniversaryF = {
			R.drawable.anniversary_f_1,
			R.drawable.anniversary_f_2,
			R.drawable.anniversary_f_3,
			R.drawable.anniversary_f_4,
			R.drawable.anniversary_f_5,
			R.drawable.anniversary_f_6,
			R.drawable.anniversary_f_7,
			R.drawable.anniversary_f_8,
			R.drawable.anniversary_f_9,
			R.drawable.anniversary_f_10,
			R.drawable.anniversary_f_11,
			R.drawable.anniversary_f_12,
			R.drawable.anniversary_f_13,
			R.drawable.anniversary_f_14,
			R.drawable.anniversary_f_15
	};

	private  Integer[] engagaeF = {
			R.drawable.engagement_f_1,
			R.drawable.engagement_f_2,
			R.drawable.engagement_f_3,
			R.drawable.engagement_f_4,
			R.drawable.engagement_f_5,
			R.drawable.engagement_f_6,
			R.drawable.engagement_f_7,
			R.drawable.engagement_f_8,
			R.drawable.engagement_f_9,
			R.drawable.engagement_f_10,
			R.drawable.engagement_f_11,
			R.drawable.engagement_f_12,
			R.drawable.engagement_f_13,
			R.drawable.engagement_f_14,
			R.drawable.engagement_f_15
	};

	private  Integer[] festivalF = {
			R.drawable.festival_f_1,
			R.drawable.festival_f_2,
			R.drawable.festival_f_3,
			R.drawable.festival_f_4,
			R.drawable.festival_f_5,
			R.drawable.festival_f_6,
			R.drawable.festival_f_7,
			R.drawable.festival_f_8,
			R.drawable.festival_f_9,
			R.drawable.festival_f_10,
			R.drawable.festival_f_11,
			R.drawable.festival_f_12,
			R.drawable.festival_f_13,
			R.drawable.festival_f_14,
			R.drawable.festival_f_15
	};

	private  Integer[] partyF = {
			R.drawable.party_f_1,
			R.drawable.party_f_2,
			R.drawable.party_f_3,
			R.drawable.party_f_4,
			R.drawable.party_f_5,
			R.drawable.party_f_6,
			R.drawable.party_f_7,
			R.drawable.party_f_8,
			R.drawable.party_f_9,
			R.drawable.party_f_10,
			R.drawable.party_f_11,
			R.drawable.party_f_12,
			R.drawable.party_f_13,
			R.drawable.party_f_14,
			R.drawable.party_f_15

	};
	
	private ImageView imageview,add_text,share,iv_greet;
	private TextView tv_title;
	private String fontPath = "fonts/SCRIPTBL.TTF";
	private Typeface tf ;
	private String mApiKey;
	Uri mImageUri;
	int imageWidth, imageHeight;
	private File mGalleryFolder;
	String mOutputFilePath;
	
	/** session id for the hi-res post processing */
	private String mSessionId;
	
	private static final int ACTION_REQUEST_GALLERY = 99;
	private static final int ACTION_REQUEST_FEATHER = 1;
	private static final int EXTERNAL_STORAGE_UNAVAILABLE = 1;
	// your aviary secret key
		private static final String API_SECRET = "cc1416449664c565";	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.edit_card);
			
		Intent i = getIntent();
		
		imageview = (ImageView)findViewById(R.id.bg_image);
		//camera 	= (ImageView)findViewById(R.id.camera);
		//sticker = (ImageView)findViewById(R.id.sticker);
		add_text=(ImageView)findViewById(R.id.add_text);
		share = (ImageView)findViewById(R.id.share);
		iv_greet = (ImageView)findViewById(R.id.iv_greet);
		tv_title = (TextView)findViewById(R.id.tv_title);
		tf = Typeface.createFromAsset(getAssets(), fontPath);
	
		int position = i.getExtras().getInt("id",0);
		int cat_id = Integer.parseInt(getIntent().getExtras().getString("cat_id"));

		switch(cat_id) {
		case 1:
			tv_title.setText("Wedding");
			tv_title.setTypeface(tf);
			iv_greet.setImageResource(R.drawable.wed1);
			System.out.print("Position>>>>>>Position  "+position);
			imageview.setImageResource(weddingF[position]);
			break;

		case 2:
			tv_title.setText("BirthDay");
			tv_title.setTypeface(tf);
			iv_greet.setImageResource(R.drawable.bday);
			System.out.print("Position>>>>>>Position  "+position);
			imageview.setImageResource(birthdayF[position]);
			break;


		case 3:
			tv_title.setText("Anniversary");
			tv_title.setTypeface(tf);
			iv_greet.setImageResource(R.drawable.ann);
			System.out.print("Position>>>>>>Position  "+position);
			imageview.setImageResource(anniversaryF[position]);
			break;


		case 4:
			tv_title.setText("Engagement");
			tv_title.setTypeface(tf);
			iv_greet.setImageResource(R.drawable.engm);
			System.out.print("Position>>>>>>Position  "+position);
			imageview.setImageResource(engagaeF[position]);
			break;


		case 5:
			tv_title.setText("Festival");
			tv_title.setTypeface(tf);
			iv_greet.setImageResource(R.drawable.festi);
			System.out.print("Position>>>>>>Position  "+position);
			imageview.setImageResource(festivalF[position]);
			break;


		case 6:
			tv_title.setText("Party");
			tv_title.setTypeface(tf);
			iv_greet.setImageResource(R.drawable.part);
			System.out.print("Position>>>>>>Position  "+position);
			imageview.setImageResource(partyF[position]);
			break;
		default:
			break;

		}
		
		add_text.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(EditCardActivity.this,AddTextActivity.class);
//				intent.putExtra("Image",(CharSequence) imageview.getBackground());
//				startActivity(intent);
				BitmapDrawable bm = (BitmapDrawable) imageview.getDrawable();
				Bitmap mysharebmp = bm.getBitmap();
				String path = Images.Media.insertImage(getContentResolver(),
						mysharebmp, "MyImage", null);
				Uri uri = Uri.parse(path);
				startFeather( uri );
			}
		});
		
		share.setOnClickListener(new View.OnClickListener() {

			//final int resource = getArguments().getInt("image");
			@Override
			public void onClick(View v) {
				BitmapDrawable bm = (BitmapDrawable) imageview.getDrawable();
				Bitmap mysharebmp = bm.getBitmap();
				String path = Images.Media.insertImage(getContentResolver(),
						mysharebmp, "MyImage", null);
				Uri uri = Uri.parse(path);
				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("image/png");
				sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
				startActivity(Intent.createChooser(sharingIntent,
						"Share image using"));
			}
		}) ;
	}
	
	private void pickFromGallery() {
		Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
		intent.setType( "image/*" );

		Intent chooser = Intent.createChooser( intent, "Choose a Picture" );
		startActivityForResult( chooser, ACTION_REQUEST_GALLERY );
	}
	public void onActivityResult( int requestCode, int resultCode, Intent data ) {
		if ( resultCode == RESULT_OK ) {
			switch ( requestCode ) {

				case ACTION_REQUEST_FEATHER:
					
					boolean changed = true;
					
					if( null != data ) {
						Bundle extra = data.getExtras();
						if( null != extra ) {
							// image was changed by the user?
							changed = extra.getBoolean( Constants.EXTRA_OUT_BITMAP_CHANGED );
						}

					}
					
					
					
					// send a notification to the media scanner
				//	updateMedia( mOutputFilePath );
					
					// update the preview with the result
					loadAsync( data.getData() );
				    Uri imageUri = data.getData();
					imageview.setImageURI(imageUri);
					//onSaveCompleted( mOutputFilePath );
				//	mOutputFilePath = null;
					break;
			}
		} else if ( resultCode == RESULT_CANCELED ) {
			switch ( requestCode ) {
				case ACTION_REQUEST_FEATHER:

					// feather was cancelled without saving.
					// we need to delete the entire session
					if ( null != mSessionId ) deleteSession( mSessionId );

					// delete the result file, if exists
					if ( mOutputFilePath != null ) {
						deleteFileNoThrow( mOutputFilePath );
						mOutputFilePath = null;
					}
					break;
			}
		}
	}

	private void loadAsync( final Uri uri ) {
		//Log.i( LOG_TAG, "loadAsync: " + uri );

		Drawable toRecycle = imageview.getDrawable();
		if ( toRecycle != null && toRecycle instanceof BitmapDrawable ) {
			if ( ( (BitmapDrawable) imageview.getDrawable() ).getBitmap() != null )
				( (BitmapDrawable) imageview.getDrawable() ).getBitmap().recycle();
		}
		imageview.setImageDrawable( null );
		mImageUri = null;

		DownloadAsync task = new DownloadAsync();
		task.execute( uri );
	}
	
	

	private void deleteSession( final String session_id ) {
		Uri uri = FeatherContentProvider.SessionsDbColumns.getContentUri( this, session_id );
		getContentResolver().delete( uri, null, null );
	}
	
	private boolean deleteFileNoThrow( String path ) {
		File file;
		try {
			file = new File( path );
		} catch ( NullPointerException e ) {
			return false;
		}

		if ( file.exists() ) {
			return file.delete();
		}
		return false;
	}

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    public void onWindowFocusChanged(boolean hasFocus) {
		 
		   int[] location = new int[2];
		  // ImageView button = (ImageView) findViewById(R.id.camera);
		 
		   // Get the x, y location and store it in the location[] array
		   // location[0] = x, location[1] = y.
	//	   button.getLocationOnScreen(location);
		 
		   //Initialize the Point with x, and y positions
		   p = new Point();
		   p.x = location[0];
		   p.y = location[1];
		}
    
	/**
	 * Check the external storage status
	 * 
	 * @return
	 */
	private boolean isExternalStorageAvilable() {
		String state = Environment.getExternalStorageState();
		if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
			return true;
		}
		return false;
	}
	
	private File getNextFileName() {
		if ( mGalleryFolder != null ) {
			if ( mGalleryFolder.exists() ) {
				File file = new File( mGalleryFolder, "aviary_" + System.currentTimeMillis() + ".jpg" );
				return file;
			}
		}
		return null;
	}
    /**
	 * Once you've chosen an image you can start the feather activity
	 * 
	 * @param uri
	 */
	@SuppressWarnings("deprecation")
	private void startFeather( Uri uri ) {

	//	Log.d( LOG_TAG, "uri: " + uri );

		// first check the external storage availability
		if ( !isExternalStorageAvilable() ) {
			showDialog( EXTERNAL_STORAGE_UNAVAILABLE );
			return;
		}
		
		// Create the intent needed to start feather
		Intent newIntent = new Intent( this, FeatherActivity.class );

		// === INPUT IMAGE URI (MANDATORY) ===
		// Set the source image uri
		newIntent.setData( uri );
		
		// === API KEY SECRET (MANDATORY) ====
		// You must pass your Aviary key secret
		newIntent.putExtra( Constants.EXTRA_IN_API_KEY_SECRET, API_SECRET );

		// Format of the destination image
		newIntent.putExtra( Constants.EXTRA_OUTPUT_FORMAT, Bitmap.CompressFormat.JPEG.name() );

		// === OUTPUT QUALITY (OPTIONAL) ===
		// Output format quality (jpeg only)
		newIntent.putExtra( Constants.EXTRA_OUTPUT_QUALITY, 90 );
	
		newIntent.putExtra( Constants.EXTRA_WHITELABEL, true );
		
	
		final DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics( metrics );
		int max_size = Math.max( metrics.widthPixels, metrics.heightPixels );
		max_size = (int) ( (float) max_size / 1.2f );
		newIntent.putExtra( Constants.EXTRA_MAX_IMAGE_SIZE, max_size );

		mSessionId = StringUtils.getSha256( System.currentTimeMillis() + mApiKey );
		//Log.d( LOG_TAG, "session: " + mSessionId + ", size: " + mSessionId.length() );
		newIntent.putExtra( Constants.EXTRA_OUTPUT_HIRES_SESSION_ID, mSessionId );
		
		newIntent.putExtra( Constants.EXTRA_IN_SAVE_ON_NO_CHANGES, true );
		
		// ..and start feather
		startActivityForResult( newIntent, ACTION_REQUEST_FEATHER );
	}
	  @Override
	  public void onResume() {
	    super.onResume();
	  }

	  @Override
	  public void onPause() {
	    super.onPause();
	  }
	  
	  /** Called before the activity is destroyed. */
	  @Override
	  public void onDestroy() {
	    super.onDestroy();
	  }
	  

	  @SuppressWarnings("deprecation")
	     private boolean setImageURI( final Uri uri, final Bitmap bitmap ) {
		  	imageview.setImageBitmap( bitmap );
		  	imageview.setBackgroundDrawable( null );
			mImageUri = uri;
			return true;
		}

class DownloadAsync extends AsyncTask<Uri, Void, Bitmap> implements OnCancelListener {

	ProgressDialog mProgress;
	private Uri mUri;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		mProgress = new ProgressDialog(EditCardActivity.this);
		mProgress.setIndeterminate( true );
		mProgress.setCancelable( true );
		mProgress.setMessage( "Loading image..." );
		mProgress.setOnCancelListener( this );
		mProgress.show();
	}

	@Override
	protected Bitmap doInBackground( Uri... params ) {
		mUri = params[0];
		Bitmap bitmap = null;

		while ( imageview.getWidth() < 1 ) {
			try {
				Thread.sleep( 1 );
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}

		final int w = imageview.getWidth();
		final int h = imageview.getHeight();

		//Log.d( LOG_TAG, "width: " + w );
		ImageSizes sizes = new ImageSizes();
		bitmap = DecodeUtils.decode( EditCardActivity.this, mUri, w, h, sizes );
		return bitmap;
	}

	@Override
	protected void onPostExecute( Bitmap result ) {
		super.onPostExecute( result );

		if ( mProgress.getWindow() != null ) {
			mProgress.dismiss();
		}

		if ( result != null ) {
			setImageURI( mUri, result );
			
		} else {
			//Toast.makeText( EditCardActivity.this, "Failed to load image " + mUri, Toast.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onCancel( DialogInterface dialog ) {
		this.cancel( true );
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

  }
}

