package com.bosandroidapp.aopaykit.data.model.cibilscore;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CAISAccount{

	@SerializedName("CAIS_Summary")
	private CAISSummary cAISSummary;

	@SerializedName("CAIS_Account_DETAILS")
	private List<CAISAccountDETAILSItem> cAISAccountDETAILS;

	public CAISSummary getCAISSummary(){
		return cAISSummary;
	}

	public List<CAISAccountDETAILSItem> getCAISAccountDETAILS(){
		return cAISAccountDETAILS;
	}
}