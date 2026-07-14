package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class Header{

	@SerializedName("ReportTime")
	private String reportTime;

	@SerializedName("SystemCode")
	private String systemCode;

	@SerializedName("ReportDate")
	private String reportDate;

	@SerializedName("MessageText")
	private Object messageText;

	public String getReportTime(){
		return reportTime;
	}

	public String getSystemCode(){
		return systemCode;
	}

	public String getReportDate(){
		return reportDate;
	}

	public Object getMessageText(){
		return messageText;
	}
}