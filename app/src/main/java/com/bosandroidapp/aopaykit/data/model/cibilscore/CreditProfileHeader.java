package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CreditProfileHeader{

	@SerializedName("ReportTime")
	private String reportTime;

	@SerializedName("Version")
	private String version;

	@SerializedName("Subscriber")
	private Object subscriber;

	@SerializedName("ReportNumber")
	private String reportNumber;

	@SerializedName("ReportDate")
	private String reportDate;

	@SerializedName("Subscriber_Name")
	private String subscriberName;

	public String getReportTime(){
		return reportTime;
	}

	public String getVersion(){
		return version;
	}

	public Object getSubscriber(){
		return subscriber;
	}

	public String getReportNumber(){
		return reportNumber;
	}

	public String getReportDate(){
		return reportDate;
	}

	public String getSubscriberName(){
		return subscriberName;
	}
}