package com.mobirizer.hollywoodemoji.db;

public class Question {
	private int ID;
	private String QUESTION;
	private String ANSWER;
	private String OPTA, OPTB, OPTC, OPTD;

	public Question() {
		ID = 0;
		QUESTION = "";
		ANSWER = "";
		OPTA = "";
		OPTB = "";
		OPTC = "";
		OPTD="";
	}

	public Question(String qUESTION, String aNSWER, String oPTA,
			String oPTB, String oPTC, String oPTD) {
		super();
		QUESTION = qUESTION;
		ANSWER = aNSWER;
		OPTA = oPTA;
		OPTB = oPTB;
		OPTC = oPTC;
		OPTD = oPTD;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getQUESTION() {
		return QUESTION;
	}

	public void setQUESTION(String qUESTION) {
		QUESTION = qUESTION;
	}

	public String getANSWER() {
		return ANSWER;
	}

	public void setANSWER(String aNSWER) {
		ANSWER = aNSWER;
	}

	public String getOPTA() {
		return OPTA;
	}

	public void setOPTA(String oPTA) {
		OPTA = oPTA;
	}

	public String getOPTB() {
		return OPTB;
	}

	public void setOPTB(String oPTB) {
		OPTB = oPTB;
	}

	public String getOPTC() {
		return OPTC;
	}

	public void setOPTC(String oPTC) {
		OPTC = oPTC;
	}

	public String getOPTD() {
		return OPTD;
	}

	public void setOPTD(String oPTD) {
		OPTD = oPTD;
	}

}