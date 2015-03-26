package com.mobirizer.autotasker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;

public class LockActivity extends Activity{
	private static final int REQ_CREATE_PATTERN = 1;
	private static final int REQ_ENTER_PATTERN = 2;
	private static final int REQ_FORGOT_PATTERN = 4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lockbg);

		// show enter or create UI
		char[] pattern = Settings.Security.getPattern(getApplicationContext());
		if (pattern != null && pattern.length <= 0)
		{
			showCreatePattern();
		} 
		else
		{
			enterForgotSavedPattern();
		}
	}

	private void showCreatePattern()
	{
		Settings.Security.setAutoSavePattern(LockActivity.this, true);
		Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN,
				null, LockActivity.this, LockPatternActivity.class);
		startActivityForResult(intent, REQ_CREATE_PATTERN);
		
	}
	
	private void enterForgotSavedPattern() 
	{
		Intent intent = new Intent(LockPatternActivity.ACTION_COMPARE_PATTERN,
				null, LockActivity.this, LockPatternActivity.class);
		intent.putExtra(
				LockPatternActivity.EXTRA_PENDING_INTENT_FORGOT_PATTERN,
				new Intent(LockActivity.this, LockActivity.class));
		startActivityForResult(intent, REQ_FORGOT_PATTERN);
		overridePendingTransition(R.anim.left_anim, R.anim.right_anim);
	}

	@SuppressWarnings("unused")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Intent intent = null;
		switch (requestCode) {
		case REQ_CREATE_PATTERN:
		{
			if (resultCode == RESULT_OK)
			{
				char[] pattern = data
						.getCharArrayExtra(LockPatternActivity.EXTRA_PATTERN);
				// success in create pattern
			}
			break;
		}
		case REQ_FORGOT_PATTERN:
		{
			switch (resultCode)
			{
			case RESULT_OK:
				finish();
				break;
			case RESULT_CANCELED:
				intent = new Intent("android.intent.action.MAIN");
	    		intent.addCategory("android.intent.category.HOME");
	    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    		startActivity(intent);
	    		finish();
				break;
			case LockPatternActivity.RESULT_FAILED:
				intent = new Intent(getApplicationContext(),ForgetPasswordActivity.class);
				startActivity(intent);
				finish();
				break;
			case LockPatternActivity.RESULT_FORGOT_PATTERN:
				// The user forgot the pattern and invoked your recovery
				// Activity.
				break;
			}

			/*
			 * In any case, there's always a key EXTRA_RETRY_COUNT, which holds
			 * the number of tries that the user did.
			 */
			int retryCount = data.getIntExtra(
					LockPatternActivity.EXTRA_RETRY_COUNT, 0);

			break;
		}// REQ_ENTER_PATTERN
		}
	}

}
