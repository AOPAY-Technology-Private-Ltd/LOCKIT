package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CAISSummary{

	@SerializedName("Total_Outstanding_Balance")
	private TotalOutstandingBalance totalOutstandingBalance;

	@SerializedName("Credit_Account")
	private CreditAccount creditAccount;

	public TotalOutstandingBalance getTotalOutstandingBalance(){
		return totalOutstandingBalance;
	}

	public CreditAccount getCreditAccount(){
		return creditAccount;
	}
}