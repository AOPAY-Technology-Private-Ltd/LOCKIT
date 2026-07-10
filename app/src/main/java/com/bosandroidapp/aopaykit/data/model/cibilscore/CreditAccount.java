package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CreditAccount{

	@SerializedName("CreditAccountActive")
	private String creditAccountActive;

	@SerializedName("CreditAccountClosed")
	private String creditAccountClosed;

	@SerializedName("CreditAccountDefault")
	private String creditAccountDefault;

	@SerializedName("CreditAccountTotal")
	private String creditAccountTotal;

	@SerializedName("CADSuitFiledCurrentBalance")
	private String cADSuitFiledCurrentBalance;

	public String getCreditAccountActive(){
		return creditAccountActive;
	}

	public String getCreditAccountClosed(){
		return creditAccountClosed;
	}

	public String getCreditAccountDefault(){
		return creditAccountDefault;
	}

	public String getCreditAccountTotal(){
		return creditAccountTotal;
	}

	public String getCADSuitFiledCurrentBalance(){
		return cADSuitFiledCurrentBalance;
	}
}