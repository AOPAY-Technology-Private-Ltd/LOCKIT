package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CAISAccountHistoryItem{

	@SerializedName("Days_Past_Due")
	private Object daysPastDue;

	@SerializedName("Month")
	private String month;

	@SerializedName("Asset_Classification")
	private String assetClassification;

	@SerializedName("Year")
	private String year;

	public Object getDaysPastDue(){
		return daysPastDue;
	}

	public String getMonth(){
		return month;
	}

	public String getAssetClassification(){
		return assetClassification;
	}

	public String getYear(){
		return year;
	}
}