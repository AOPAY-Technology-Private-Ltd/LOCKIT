package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class Result{

	@SerializedName("result_json")
	private ResultJson resultJson;

	public ResultJson getResultJson(){
		return resultJson;
	}
}