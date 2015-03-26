package com.mobirizer.autotasker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;

public class SecurityQuestionAnswerActivity extends Activity{

	private EditText editTextSecurityQuestion = null;
	private EditText editTextSecurityAnswer = null;
	private Button btnSecuritySave = null;
	private SavePreferences preferences = null;
	private static final String SECURITY_QUES_KEY = "ques_key";
	private static final String SECURITY_ANS_KEY = "ans_key";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sqty_ques);
		initView();
		
		char[] pattern = Settings.Security.getPattern(getApplicationContext());
		if (pattern == null || pattern.length<=0) 
		{
			preferences.SavePreferences(getApplicationContext(),"first_launch",1);
			System.out.println("calling showcreatepatter");
			showCreatePattern();
		}
		else if(isDataExist())
		{
			Intent intent = new Intent(SecurityQuestionAnswerActivity.this,FirstActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void showCreatePattern()
	{
		Settings.Security.setAutoSavePattern(SecurityQuestionAnswerActivity.this, true);
		Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN,
				null, SecurityQuestionAnswerActivity.this, LockPatternActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		if(requestCode==1 && resultCode==0)
		{
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);

	}
	
	private void initView()
	{
		editTextSecurityQuestion = (EditText)findViewById(R.id.editTextSecurityQuestion);
		editTextSecurityAnswer = (EditText)findViewById(R.id.editTextSecurityAnswer);
		btnSecuritySave = (Button)findViewById(R.id.btnSecuritySave);
		preferences = new SavePreferences();

		btnSecuritySave.setOnClickListener(new OnClickListener() 
		{

			public void onClick(View v) 
			{
				saveData();
			}
		});
	}

	private void saveData()
	{
		String questionText = (editTextSecurityQuestion != null) ? editTextSecurityQuestion.getText().toString() : "";
		String answerText = (editTextSecurityAnswer != null) ? editTextSecurityAnswer.getText().toString() : "";
		
		if(!validateText(questionText))
		{
			Toast.makeText(getApplicationContext(), "Please enter your security Question!", Toast.LENGTH_SHORT).show();
		}
		else if(!validateText(answerText))
		{
			Toast.makeText(getApplicationContext(), "Please enter your security Answer!", Toast.LENGTH_SHORT).show();
		}
		else
		{
			preferences.SavePreferences(getApplicationContext(), SECURITY_QUES_KEY, questionText);
			preferences.SavePreferences(getApplicationContext(), SECURITY_ANS_KEY, answerText);
			Toast.makeText(getApplicationContext(), "Save data Successfully!", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(SecurityQuestionAnswerActivity.this,FirstActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	private boolean isDataExist()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String questionText = preferences.getString(SECURITY_QUES_KEY, "");
		String answerText = preferences.getString(SECURITY_ANS_KEY, "");
		if(!questionText.equalsIgnoreCase("") && !answerText.equalsIgnoreCase(""))
		{
			return true;
		}
		else
		{
			return false;
		}
		
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
