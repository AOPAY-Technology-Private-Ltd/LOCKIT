package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class NonCreditCAPSSummary{

	@SerializedName("NonCreditCAPSLast30Days")
	private String nonCreditCAPSLast30Days;

	@SerializedName("NonCreditCAPSLast180Days")
	private String nonCreditCAPSLast180Days;

	@SerializedName("NonCreditCAPSLast90Days")
	private String nonCreditCAPSLast90Days;

	@SerializedName("NonCreditCAPSLast7Days")
	private String nonCreditCAPSLast7Days;

	public String getNonCreditCAPSLast30Days(){
		return nonCreditCAPSLast30Days;
	}

	public String getNonCreditCAPSLast180Days(){
		return nonCreditCAPSLast180Days;
	}

	public String getNonCreditCAPSLast90Days(){
		return nonCreditCAPSLast90Days;
	}

	public String getNonCreditCAPSLast7Days(){
		return nonCreditCAPSLast7Days;
	}
}