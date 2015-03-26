package com.mobirizer.hollywoodemoji;

import java.util.List;

import com.mobirizer.hollywoodemoji.db.HollywoodDb;
import com.mobirizer.hollywoodemoji.db.Question;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class Fragment1 extends Fragment {
	static ImageView img1, img2, img3, img4;
	TextView cAns;
	TextView quest1, questNo;
	public static Button change;
	static int i;
	Context c = QuestionActivity.c;
	HollywoodDb db;
	private FragmentTransaction ft;
	String no;
	static boolean nowWhat = false;

	public Fragment1(int questionid) {
		i = questionid;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment1, null);
	}

	private int getDrawable(String ansId) {
		int drawableId = getActivity()
				.getApplicationContext()
				.getResources()
				.getIdentifier(ansId, "drawable",
						getActivity().getApplicationContext().getPackageName());
		return drawableId;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initAnsView();
		createDataBase();
		checkIndexValue();
		no = questNo.getText().toString();
		questNo.setText(no + i);
		change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nextFragment();
			}
		});
	}

	private void checkIndexValue() {
		// TODO Auto-generated method stub
		if (i > 50) {
			showDialog();
			QuestionActivity.correctAnswers = 0;
			i = 1;

		}
		setQuestion(i);
		setAnswers(i);
	}

	private void nextFragment() { // TODO Auto-generated method stub
		ft = getFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.animator.slide1, R.animator.slide2);

		ft.replace(R.id.fragCont, new Fragment1(i + 1));
		// questNo.setText(no + i);
		ft.addToBackStack(null);

		ft.commit();
	}

	private String setQuestion(int i) {
		String question = db.getQuestion(i);
		if (question != "EOR") {
			quest1.setText(question);
			return question;
		} else {
			return "E";
		}

	}

	private boolean setAnswers(int i) {
		db = new HollywoodDb(c);
		List<Question> list = db.getAnswers(i);

		Question q1 = list.get(0);
		String ansIdA = q1.getOPTA();
		String ansIdB = q1.getOPTB();
		String ansIdC = q1.getOPTC();
		String ansIdD = q1.getOPTD();

		int drawableId = getDrawable(ansIdA);
		img1.setBackgroundResource(drawableId);

		drawableId = getDrawable(ansIdB);
		img2.setBackgroundResource(drawableId);

		drawableId = getDrawable(ansIdC);
		img3.setBackgroundResource(drawableId);

		drawableId = getDrawable(ansIdD);
		img4.setBackgroundResource(drawableId);
		// System.out.println("aman check a1 : " + ansIdA + " , : " +
		// drawableId);
		return false;
	}

	private void createDataBase() {
		// TODO Auto-generated method stub
		db = new HollywoodDb(c);
		db.addQuestions();
	}

	private void initAnsView() {
		// TODO Auto-generated method stub

		quest1 = (TextView) getActivity().findViewById(R.id.quest1);

		img1 = (ImageView) getActivity().findViewById(R.id.f1ansView1);

		img2 = (ImageView) getActivity().findViewById(R.id.f1ansView2);

		img3 = (ImageView) getActivity().findViewById(R.id.f1ansView3);

		img4 = (ImageView) getActivity().findViewById(R.id.f1ansView4);

		questNo = (TextView) getActivity().findViewById(R.id.questNo);

		change = (Button) getActivity().findViewById(R.id.changer);
		change.setEnabled(false);
		img1.setEnabled(true);
		img2.setEnabled(true);
		img3.setEnabled(true);
		img4.setEnabled(true);
		cAns = (TextView) getActivity().findViewById(R.id.isCurrect);
		cAns.setText("Score: " + QuestionActivity.correctAnswers + "/50");

	}

	private void showDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

		// set title
		alertDialogBuilder.setTitle("Congratulations! Your Final Score is "
				+ QuestionActivity.correctAnswers + " Out Of 50.");

		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Start Over",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialog.cancel();
							}
						})
				.setNegativeButton("Quit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialog.cancel();
								getActivity().finish();
								Intent mainInt = new Intent(getActivity(),
										MainActivity.class);
								startActivity(mainInt);
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
		// return nowWhat;

	}

}
