package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CurrentApplication{

	@SerializedName("Current_Application_Details")
	private CurrentApplicationDetails currentApplicationDetails;

	public CurrentApplicationDetails getCurrentApplicationDetails(){
		return currentApplicationDetails;
	}
}