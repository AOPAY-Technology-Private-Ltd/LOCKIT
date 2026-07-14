package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CurrentOtherDetails{

	@SerializedName("Time_with_Employer")
	private Object timeWithEmployer;

	@SerializedName("Employment_Status")
	private Object employmentStatus;

	@SerializedName("Number_of_Major_Credit_Card_Held")
	private Object numberOfMajorCreditCardHeld;

	@SerializedName("Marital_Status")
	private Object maritalStatus;

	@SerializedName("Income")
	private String income;

	public Object getTimeWithEmployer(){
		return timeWithEmployer;
	}

	public Object getEmploymentStatus(){
		return employmentStatus;
	}

	public Object getNumberOfMajorCreditCardHeld(){
		return numberOfMajorCreditCardHeld;
	}

	public Object getMaritalStatus(){
		return maritalStatus;
	}

	public String getIncome(){
		return income;
	}
}