package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class SCORE{

	@SerializedName("BureauScore")
	private String bureauScore;

	@SerializedName("BureauScoreConfidLevel")
	private Object bureauScoreConfidLevel;

	public String getBureauScore(){
		return bureauScore;
	}

	public Object getBureauScoreConfidLevel(){
		return bureauScoreConfidLevel;
	}
}