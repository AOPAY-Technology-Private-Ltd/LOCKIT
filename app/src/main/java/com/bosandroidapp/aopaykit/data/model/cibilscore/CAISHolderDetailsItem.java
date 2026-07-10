package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CAISHolderDetailsItem{

	@SerializedName("Income_TAX_PAN")
	private Object incomeTAXPAN;

	@SerializedName("Surname_Non_Normalized")
	private String surnameNonNormalized;

	@SerializedName("Alias")
	private Object alias;

	@SerializedName("Gender_Code")
	private String genderCode;

	@SerializedName("Date_of_birth")
	private String dateOfBirth;

	@SerializedName("First_Name_Non_Normalized")
	private Object firstNameNonNormalized;

	@SerializedName("Voter_ID_Number")
	private String voterIDNumber;

	@SerializedName("Middle_Name_1_Non_Normalized")
	private Object middleName1NonNormalized;

	@SerializedName("Middle_Name_3_Non_Normalized")
	private Object middleName3NonNormalized;

	@SerializedName("Middle_Name_2_Non_Normalized")
	private Object middleName2NonNormalized;

	@SerializedName("Passport_Number")
	private Object passportNumber;

	public Object getIncomeTAXPAN(){
		return incomeTAXPAN;
	}

	public String getSurnameNonNormalized(){
		return surnameNonNormalized;
	}

	public Object getAlias(){
		return alias;
	}

	public String getGenderCode(){
		return genderCode;
	}

	public String getDateOfBirth(){
		return dateOfBirth;
	}

	public Object getFirstNameNonNormalized(){
		return firstNameNonNormalized;
	}

	public String getVoterIDNumber(){
		return voterIDNumber;
	}

	public Object getMiddleName1NonNormalized(){
		return middleName1NonNormalized;
	}

	public Object getMiddleName3NonNormalized(){
		return middleName3NonNormalized;
	}

	public Object getMiddleName2NonNormalized(){
		return middleName2NonNormalized;
	}

	public Object getPassportNumber(){
		return passportNumber;
	}
}