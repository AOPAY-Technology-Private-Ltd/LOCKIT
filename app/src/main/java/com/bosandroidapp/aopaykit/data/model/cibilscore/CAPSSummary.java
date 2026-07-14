package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CAPSSummary{

	@SerializedName("CAPSLast30Days")
	private String cAPSLast30Days;

	@SerializedName("CAPSLast7Days")
	private String cAPSLast7Days;

	@SerializedName("CAPSLast180Days")
	private String cAPSLast180Days;

	@SerializedName("CAPSLast90Days")
	private String cAPSLast90Days;

	public String getCAPSLast30Days(){
		return cAPSLast30Days;
	}

	public String getCAPSLast7Days(){
		return cAPSLast7Days;
	}

	public String getCAPSLast180Days(){
		return cAPSLast180Days;
	}

	public String getCAPSLast90Days(){
		return cAPSLast90Days;
	}
}