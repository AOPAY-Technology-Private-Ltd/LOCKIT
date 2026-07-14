package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class TotalOutstandingBalance{

	@SerializedName("Outstanding_Balance_Secured")
	private String outstandingBalanceSecured;

	@SerializedName("Outstanding_Balance_UnSecured_Percentage")
	private String outstandingBalanceUnSecuredPercentage;

	@SerializedName("Outstanding_Balance_All")
	private String outstandingBalanceAll;

	@SerializedName("Outstanding_Balance_Secured_Percentage")
	private String outstandingBalanceSecuredPercentage;

	@SerializedName("Outstanding_Balance_UnSecured")
	private String outstandingBalanceUnSecured;

	public String getOutstandingBalanceSecured(){
		return outstandingBalanceSecured;
	}

	public String getOutstandingBalanceUnSecuredPercentage(){
		return outstandingBalanceUnSecuredPercentage;
	}

	public String getOutstandingBalanceAll(){
		return outstandingBalanceAll;
	}

	public String getOutstandingBalanceSecuredPercentage(){
		return outstandingBalanceSecuredPercentage;
	}

	public String getOutstandingBalanceUnSecured(){
		return outstandingBalanceUnSecured;
	}
}