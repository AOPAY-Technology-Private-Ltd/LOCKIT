package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CAISHolderIDDetailsItem{

	@SerializedName("PAN_Issue_Date")
	private Object pANIssueDate;

	@SerializedName("Ration_Card_Number")
	private Object rationCardNumber;

	@SerializedName("Income_TAX_PAN")
	private String incomeTAXPAN;

	@SerializedName("Voter_ID_Expiration_Date")
	private Object voterIDExpirationDate;

	@SerializedName("Driver_License_Expiration_Date")
	private Object driverLicenseExpirationDate;

	@SerializedName("EMailId")
	private Object eMailId;

	@SerializedName("Passport_Number")
	private Object passportNumber;

	@SerializedName("Ration_Card_Expiration_Date")
	private Object rationCardExpirationDate;

	@SerializedName("Ration_Card_Issue_Date")
	private Object rationCardIssueDate;

	@SerializedName("Universal_ID_Issue_Date")
	private Object universalIDIssueDate;

	@SerializedName("Driver_License_Number")
	private Object driverLicenseNumber;

	@SerializedName("Voter_ID_Issue_Date")
	private Object voterIDIssueDate;

	@SerializedName("Driver_License_Issue_Date")
	private Object driverLicenseIssueDate;

	@SerializedName("Passport_Expiration_Date")
	private Object passportExpirationDate;

	@SerializedName("Voter_ID_Number")
	private String voterIDNumber;

	@SerializedName("Passport_Issue_Date")
	private Object passportIssueDate;

	@SerializedName("PAN_Expiration_Date")
	private Object pANExpirationDate;

	@SerializedName("Universal_ID_Number")
	private Object universalIDNumber;

	@SerializedName("Universal_ID_Expiration_Date")
	private Object universalIDExpirationDate;

	public Object getPANIssueDate(){
		return pANIssueDate;
	}

	public Object getRationCardNumber(){
		return rationCardNumber;
	}

	public String getIncomeTAXPAN(){
		return incomeTAXPAN;
	}

	public Object getVoterIDExpirationDate(){
		return voterIDExpirationDate;
	}

	public Object getDriverLicenseExpirationDate(){
		return driverLicenseExpirationDate;
	}

	public Object getEMailId(){
		return eMailId;
	}

	public Object getPassportNumber(){
		return passportNumber;
	}

	public Object getRationCardExpirationDate(){
		return rationCardExpirationDate;
	}

	public Object getRationCardIssueDate(){
		return rationCardIssueDate;
	}

	public Object getUniversalIDIssueDate(){
		return universalIDIssueDate;
	}

	public Object getDriverLicenseNumber(){
		return driverLicenseNumber;
	}

	public Object getVoterIDIssueDate(){
		return voterIDIssueDate;
	}

	public Object getDriverLicenseIssueDate(){
		return driverLicenseIssueDate;
	}

	public Object getPassportExpirationDate(){
		return passportExpirationDate;
	}

	public String getVoterIDNumber(){
		return voterIDNumber;
	}

	public Object getPassportIssueDate(){
		return passportIssueDate;
	}

	public Object getPANExpirationDate(){
		return pANExpirationDate;
	}

	public Object getUniversalIDNumber(){
		return universalIDNumber;
	}

	public Object getUniversalIDExpirationDate(){
		return universalIDExpirationDate;
	}
}