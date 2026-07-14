package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class TotalCAPSSummary{

	@SerializedName("TotalCAPSLast90Days")
	private String totalCAPSLast90Days;

	@SerializedName("TotalCAPSLast7Days")
	private String totalCAPSLast7Days;

	@SerializedName("TotalCAPSLast30Days")
	private String totalCAPSLast30Days;

	@SerializedName("TotalCAPSLast180Days")
	private String totalCAPSLast180Days;

	public String getTotalCAPSLast90Days(){
		return totalCAPSLast90Days;
	}

	public String getTotalCAPSLast7Days(){
		return totalCAPSLast7Days;
	}

	public String getTotalCAPSLast30Days(){
		return totalCAPSLast30Days;
	}

	public String getTotalCAPSLast180Days(){
		return totalCAPSLast180Days;
	}
}