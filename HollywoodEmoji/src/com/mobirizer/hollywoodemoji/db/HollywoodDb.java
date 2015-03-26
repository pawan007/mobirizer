package com.mobirizer.hollywoodemoji.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HollywoodDb extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Hollywood"; //"HollywoodDB";
	private SQLiteDatabase dbase;

	// tasks Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_QUES = "question";
	private static final String KEY_ANSWER = "answer"; // correct option
	private static final String TB_QUESTION = "question_table";
	private static final String KEY_OPTA = "opta";
	private static final String KEY_OPTB = "optb";
	private static final String KEY_OPTC = "optc";
	private static final String KEY_OPTD = "optd";
	Context c;

	public HollywoodDb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		this.c = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		dbase = db;

		String sql = "CREATE TABLE IF NOT EXISTS " + TB_QUESTION + "( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUES
				+ " TEXT, " + KEY_ANSWER + " TEXT, " + KEY_OPTA + " TEXT, "
				+ KEY_OPTB + " TEXT, " + KEY_OPTC + " TEXT," + KEY_OPTD
				+ " TEXT)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//del(c);
		onCreate(db);
	}

	// this method will delete the whole database...
	public void del(Context context) {
		// dbase.execSQL("DROP TABLE IF EXISTS " + TB_QUESTION);
		context.deleteDatabase(DATABASE_NAME);
	}

	public boolean addQuestions() {
		if (rowcount() > 0) {
			return false;
		}
		String cat = "q";

		Question q1 = new Question("Guess Henry Fonda's Movie Name?", cat
				+ "1a1", cat + "1a1", cat + "1a2", cat + "1a3", cat + "1a4");
		this.addQuestion(q1);

		Question q2 = new Question(
				"Guess Isabelle Huppert's Movie Name?",
				cat + "2a3", cat + "2a1", cat + "2a2", cat + "2a3", cat + "2a4");
		this.addQuestion(q2);

		Question q3 = new Question("Guess Tom Hanks Movie's Name, Which Was Directed By Ron Howard?",
				cat + "3a2", cat + "3a1", cat + "3a2", cat + "3a3", cat + "3a4");
		this.addQuestion(q3);

		Question q4 = new Question(
				"Guess Taylor Kitsch Movie Name?",
				cat + "4a4", cat + "4a1", cat + "4a2", cat + "4a3", cat + "4a4");
		this.addQuestion(q4);

		Question q5 = new Question("Which Cartoon Movie Directed By Krik Wise & Gary Trousdale?", cat
				+ "5a2", cat + "5a1", cat + "5a2", cat + "5a3", cat + "5a4");
		this.addQuestion(q5);
		
		Question q6 = new Question("Guess Adam Sandler's Movie Name?", cat
				+ "6a3", cat + "6a1", cat + "6a2", cat + "6a3", cat + "6a4");
		this.addQuestion(q6);

		Question q7 = new Question("Guess Jet Li's Movie Name?", cat
				+ "7a4", cat + "7a1", cat + "7a2", cat + "7a3", cat + "7a4");
		this.addQuestion(q7);

		Question q8 = new Question("Guess Chris Evans's Movie Name?", cat
				+ "8a1", cat + "8a1", cat + "8a2", cat + "8a3", cat + "8a4");
		this.addQuestion(q8);

		Question q9 = new Question("Guess Morgan Freeman's Movie Name?", cat
				+ "9a3", cat + "9a1", cat + "9a2", cat + "9a3", cat + "9a4");
		this.addQuestion(q9);

		Question q10 = new Question("Guess Vin Diesel's Movie Name?", cat
				+ "10a2", cat + "10a1", cat + "10a2", cat + "10a3", cat + "10a4");
		this.addQuestion(q10);

		Question q11 = new Question("Guess Paul Walker's Movie Name?", cat
				+ "11a4", cat + "11a1", cat + "11a2", cat + "11a3", cat + "11a4");
		this.addQuestion(q11);

		Question q12 = new Question("Which Black & White Movie Directed By J.searle Dawley?", cat
				+ "12a1", cat + "12a1", cat + "12a2", cat + "12a3", cat + "12a4");
		this.addQuestion(q12);

		Question q13 = new Question("Which Movie was Written, acted & Directed By H.B. Halicki?", cat
				+ "13a2", cat + "13a1", cat + "13a2", cat + "13a3", cat + "13a4");
		this.addQuestion(q13);

		Question q14 = new Question("In Which Movie Clark Gable & vivien leigh worked together?", cat
				+ "14a2", cat + "14a1", cat + "14a2", cat + "14a3", cat + "14a4");
		this.addQuestion(q14);

		Question q15 = new Question("In Which movie Patrick Stewart & Hugh jackman worked together?", cat
				+ "15a1", cat + "15a1", cat + "15a2", cat + "15a3", cat + "15a4");
		this.addQuestion(q15);

		Question q16 = new Question("Guess Sandra Bullock's Movie Name?", cat
				+ "16a1", cat + "16a1", cat + "16a2", cat + "16a3", cat + "16a4");
		this.addQuestion(q16);
		
		Question q17 = new Question("Guess Seth Rogen Movie Name?", cat
				+ "17a4", cat + "17a1", cat + "17a2", cat + "17a3", cat + "17a4");
		this.addQuestion(q17);
		
		Question q18 = new Question("Guess Zach Galifianakis's Movie Name?", cat
				+ "18a1", cat + "18a1", cat + "18a2", cat + "18a3", cat + "18a4");
		this.addQuestion(q18);
		
		Question q19 = new Question("Guess Macaulay Culkin's Movie Name?", cat
				+ "19a3", cat + "19a1", cat + "19a2", cat + "19a3", cat + "19a4");
		this.addQuestion(q19);
		
		Question q20 = new Question("Guess Nicolas Cage's Movie Name?", cat
				+ "20a4", cat + "20a1", cat + "20a2", cat + "20a3", cat + "20a4");
		this.addQuestion(q20);
		
		Question q21 = new Question("Guess Robert Downey's Movie Name?", cat
				+ "21a2", cat + "21a1", cat + "21a2", cat + "21a3", cat + "21a4");
		this.addQuestion(q21);
		
		Question q22 = new Question("Guess Uma Karuna Thurman's Movie Name?", cat
				+ "22a1", cat + "22a1", cat + "22a2", cat + "22a3", cat + "22a4");
		this.addQuestion(q22);
		
		Question q23 = new Question("Guess Colin Andrew Firth's Movie Name?", cat
				+ "23a3", cat + "23a1", cat + "23a2", cat + "23a3", cat + "23a4");
		this.addQuestion(q23);
		
		Question q24 = new Question("Which Movie was Directed By Mark Osborne & John stevenson?", cat
				+ "24a2", cat + "24a1", cat + "24a2", cat + "24a3", cat + "24a4");
		this.addQuestion(q24);
		
		Question q25 = new Question("Guess Timothy Dalton's Movie Name?", cat
				+ "25a4", cat + "25a1", cat + "25a2", cat + "25a3", cat + "25a4");
		this.addQuestion(q25);
		
		Question q26 = new Question("Guess Irfaan Khan's Movie Name", cat
				+ "26a2", cat + "26a1", cat + "26a2", cat + "26a3", cat + "26a4");
		this.addQuestion(q26);
		
		Question q27 = new Question("Guess Elijah Wood's Movie Name?", cat
				+ "27a1", cat + "27a1", cat + "27a2", cat + "27a3", cat + "27a4");
		this.addQuestion(q27);
		
		Question q28 = new Question("In Which Movie Everett Scott & Kate Capshaw worked together?", cat
				+ "28a4", cat + "28a1", cat + "28a2", cat + "28a3", cat + "28a4");
		this.addQuestion(q28);
		
		Question q29 = new Question("In Which Movie tommy Lee jones & Will Smith worked together?", cat
				+ "29a2", cat + "29a1", cat + "29a2", cat + "29a3", cat + "29a4");
		this.addQuestion(q29);
		
		Question q30 = new Question("Guess Owen Wilson's Movie Name?", cat
				+ "30a1", cat + "30a1", cat + "30a2", cat + "30a3", cat + "30a4");
		this.addQuestion(q30);
		
		Question q31 = new Question("Guess Tom Cruise's Movie Name?", cat
				+ "31a3", cat + "31a1", cat + "31a2", cat + "31a3", cat + "31a4");
		this.addQuestion(q31);
		
		Question q32 = new Question("Guess Robin Mclaurin Williams's Movie Name?", cat
				+ "32a2", cat + "32a1", cat + "32a2", cat + "32a3", cat + "32a4");
		this.addQuestion(q32);
		
		Question q33 = new Question("Guess Johnny depp's Movie Name?", cat
				+ "33a1", cat + "33a1", cat + "33a2", cat + "33a3", cat + "33a4");
		this.addQuestion(q33);
		
		Question q34 = new Question("Guess Anna Faris's Movie Name?", cat
				+ "34a4", cat + "34a1", cat + "34a2", cat + "34a3", cat + "34a4");
		this.addQuestion(q34);
		
		Question q35 = new Question("In Which Movie Bradley cooper & Jennifer Lawrence  worked together?", cat
				+ "35a4", cat + "35a1", cat + "35a2", cat + "35a3", cat + "35a4");
		this.addQuestion(q35);
		
		Question q36 = new Question("Guess Bruce Willis's Movie Name?", cat
				+ "36a1", cat + "36a1", cat + "36a2", cat + "36a3", cat + "36a4");
		this.addQuestion(q36);
		
		Question q37 = new Question("Guess Daniel Craig's Movie Name?", cat
				+ "37a3", cat + "37a1", cat + "37a2", cat + "37a3", cat + "37a4");
		this.addQuestion(q37);
		
		Question q38 = new Question("Guess Tobey Maguire's Movie Name?", cat
				+ "38a4", cat + "38a1", cat + "38a2", cat + "38a3", cat + "38a4");
		this.addQuestion(q38);
		
		Question q39 = new Question("Guess Mark Huberman's Movie Name?", cat
				+ "39a2", cat + "39a1", cat + "39a2", cat + "39a3", cat + "39a4");
		this.addQuestion(q39);
		
		Question q40 = new Question("Guess Chris Pine's Movie Name?", cat
				+ "40a1", cat + "40a1", cat + "40a2", cat + "40a3", cat + "40a4");
		this.addQuestion(q40);
		
		Question q41 = new Question("Guess Rob Minkoff Movie Name?", cat
				+ "41a1", cat + "41a1", cat + "41a2", cat + "41a3", cat + "41a4");
		this.addQuestion(q41);
		
		Question q42 = new Question("Guess Mark Wahlberg Movie Name?", cat
				+ "42a3", cat + "42a1", cat + "42a2", cat + "42a3", cat + "42a4");
		this.addQuestion(q42);
		
		Question q43 = new Question("In which movie Robert Downey & Mark Ruffalo  worked together?", cat
				+ "43a2", cat + "43a1", cat + "43a2", cat + "43a3", cat + "43a4");
		this.addQuestion(q43);
		
		Question q44 = new Question("Guess Ian Murray Mckellen's Movie Name?", cat
				+ "44a4", cat + "44a1", cat + "44a2", cat + "44a3", cat + "44a4");
		this.addQuestion(q44);
		
		Question q45 = new Question("Which Movie was directed By Roger Allers & Rob Minkoff?", cat
				+ "45a3", cat + "45a1", cat + "45a2", cat + "45a3", cat + "45a4");
		this.addQuestion(q45);
		
		Question q46 = new Question("Guess John Christopher's Movie Name?", cat
				+ "46a1", cat + "46a1", cat + "46a2", cat + "46a3", cat + "46a4");
		this.addQuestion(q46);
		
		Question q47 = new Question("Guess Chris Hemsworth's Movie Name?", cat
				+ "47a4", cat + "47a1", cat + "47a2", cat + "47a3", cat + "47a4");
		this.addQuestion(q47);
		
		Question q48 = new Question("In which movie Paul walker & Vin Diesel  worked together?", cat
				+ "48a2", cat + "48a1", cat + "48a2", cat + "48a3", cat + "48a4");
		this.addQuestion(q48);
		
		Question q49 = new Question("In which Movie Eliza Dushku & Jeremy sisto  worked together?", cat
				+ "49a3", cat + "49a1", cat + "49a2", cat + "49a3", cat + "49a4");
		this.addQuestion(q49);
		
		Question q50 = new Question("Guess Hugh Jackman's Movie Name?", cat
				+ "50a1", cat + "50a1", cat + "50a2", cat + "50a3", cat + "50a4");
		this.addQuestion(q50);

		return true;

	}

	// adding questions to database table TB_QUESTION
	public void addQuestion(Question quest) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_QUES, quest.getQUESTION());
		values.put(KEY_ANSWER, quest.getANSWER());
		values.put(KEY_OPTA, quest.getOPTA());
		values.put(KEY_OPTB, quest.getOPTB());
		values.put(KEY_OPTC, quest.getOPTC());
		values.put(KEY_OPTD, quest.getOPTD());
		// Inserting Row
		db.insert(TB_QUESTION, null, values);
		db.close();
	}

	public int rowcount() {
		int row = 0;
		String selectQuery = "SELECT  * FROM " + TB_QUESTION;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		row = cursor.getCount();
		cursor.close();
		return row;
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
	}

	public String getQuestion(int i) {
		Cursor cursor = null;
		dbase = this.getReadableDatabase();
		if (checkLastRecord()) {

			String selectQuery = " SELECT " + KEY_QUES + " FROM " + TB_QUESTION
					+ " WHERE " + KEY_ID + " = " + i;

			cursor = dbase.rawQuery(selectQuery, null);

			/*
			 * cursor = dbase.rawQuery("Select question From " + TB_QUESTION +
			 * " Order By RANDOM() LIMIT ?", new String[] { "6" });
			 */

			if (cursor.moveToFirst()) {

				Log.d(cursor.getString(0), "value of cursor where id= " + i);
				return "" + cursor.getString(0);
			}
			cursor.close();
			dbase.close();
		}
		return "EOR";
	}

	public List<Question> getAnswers(int index) {
		List<Question> quesList = new ArrayList<Question>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TB_QUESTION + " WHERE "
				+ KEY_ID + " = " + index;
		dbase = this.getReadableDatabase();
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Question quest = new Question();
				quest.setID(cursor.getInt(0));
				quest.setQUESTION(cursor.getString(1));
				quest.setANSWER(cursor.getString(2));
				quest.setOPTA(cursor.getString(3));
				quest.setOPTB(cursor.getString(4));
				quest.setOPTC(cursor.getString(5));
				quest.setOPTD(cursor.getString(6));
				quesList.add(quest);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return quesList;
	}

	private boolean checkLastRecord() {
		String selectQuery = " SELECT " + "*" + " FROM " + TB_QUESTION
				+ " WHERE " + KEY_ID + " = " + "(SELECT MAX(ID) FROM "
				+ TB_QUESTION + ")";
		Cursor cursor = dbase.rawQuery(selectQuery, null);
		if (cursor == null) {

			return false;
		}
		cursor.close();
		return true;

	}

}
