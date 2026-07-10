package com.bosandroidapp.aopaykit.data.model.cibilscore;


import com.google.gson.annotations.SerializedName;

public class INProfileResponse{

	@SerializedName("SCORE")
	private SCORE sCORE;

	@SerializedName("Header")
	private Header header;

	@SerializedName("TotalCAPS_Summary")
	private TotalCAPSSummary totalCAPSSummary;

	@SerializedName("CreditProfileHeader")
	private CreditProfileHeader creditProfileHeader;

	@SerializedName("NonCreditCAPS")
	private NonCreditCAPS nonCreditCAPS;

	@SerializedName("UserMessage")
	private UserMessage userMessage;

	@SerializedName("CAIS_Account")
	private CAISAccount cAISAccount;

	@SerializedName("Match_result")
	private MatchResult matchResult;

	@SerializedName("Current_Application")
	private CurrentApplication currentApplication;

	@SerializedName("CAPS")
	private CAPS cAPS;

	public SCORE getSCORE(){
		return sCORE;
	}

	public Header getHeader(){
		return header;
	}

	public TotalCAPSSummary getTotalCAPSSummary(){
		return totalCAPSSummary;
	}

	public CreditProfileHeader getCreditProfileHeader(){
		return creditProfileHeader;
	}

	public NonCreditCAPS getNonCreditCAPS(){
		return nonCreditCAPS;
	}

	public UserMessage getUserMessage(){
		return userMessage;
	}

	public CAISAccount getCAISAccount(){
		return cAISAccount;
	}

	public MatchResult getMatchResult(){
		return matchResult;
	}

	public CurrentApplication getCurrentApplication(){
		return currentApplication;
	}

	public CAPS getCAPS(){
		return cAPS;
	}
}