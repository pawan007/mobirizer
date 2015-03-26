package com.mobirizer.autotasker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;

public class ForgetPasswordActivity extends Activity{

	private EditText editTextSecurityQuestion = null;
	private EditText editTextSecurityAnswer = null;
	private Button btnSecuritySave = null;
	private static final String SECURITY_QUES_KEY = "ques_key";
	private static final String SECURITY_ANS_KEY = "ans_key";
	String questionText = null;
	String answerText = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_forget_password);
		initView();
		initializeData();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			Intent intent = new Intent("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.HOME");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}// onKeyDown()

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode==1 && resultCode==-1)
		{
			finish();
		}
		else 
		{
			Intent intent = new Intent("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.HOME");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	private void initView()
	{
		editTextSecurityQuestion = (EditText)findViewById(R.id.editTextSecurityQuestion);
		editTextSecurityAnswer = (EditText)findViewById(R.id.editTextSecurityAnswer);
		btnSecuritySave = (Button)findViewById(R.id.btnSecuritySave);

		btnSecuritySave.setOnClickListener(new OnClickListener() 
		{

			public void onClick(View v) 
			{
				compareSqrtyAnswer();
			}
		});
	}

	private void initializeData()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		questionText = preferences.getString(SECURITY_QUES_KEY, "");
		answerText = preferences.getString(SECURITY_ANS_KEY, "");
		editTextSecurityQuestion.setText(questionText);
		editTextSecurityQuestion.setFocusable(false);
	}
	private void compareSqrtyAnswer()
	{
		String tempText = (editTextSecurityAnswer != null) ? editTextSecurityAnswer.getText().toString() : "";

		if(!validateText(answerText))
		{
			Toast.makeText(getApplicationContext(), "Please enter your security Answer!", Toast.LENGTH_SHORT).show();
		}
		else if(!tempText.equalsIgnoreCase(answerText))
		{
			Toast.makeText(getApplicationContext(), "Security Answer don't match. Please enter current Answer!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Answer match Successfully!", Toast.LENGTH_SHORT).show();
			changePattern();
			//			finish();
		}
	}


	private void changePattern()
	{
		Settings.Security.setAutoSavePattern(ForgetPasswordActivity.this, true);
		Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN,
				null, ForgetPasswordActivity.this, LockPatternActivity.class);
		startActivityForResult(intent, 1);
	}

	private boolean validateText(String text)
	{
		if(text != null && !text.equalsIgnoreCase(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
