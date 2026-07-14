package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CurrentApplicantDetails{

	@SerializedName("IncomeTaxPan")
	private String incomeTaxPan;

	@SerializedName("PAN_Issue_Date")
	private Object pANIssueDate;

	@SerializedName("Passport_number")
	private Object passportNumber;

	@SerializedName("Voter_ID_Expiration_Date")
	private Object voterIDExpirationDate;

	@SerializedName("Telephone_Number_Applicant_1st")
	private Object telephoneNumberApplicant1st;

	@SerializedName("Voter_s_Identity_Card")
	private Object voterSIdentityCard;

	@SerializedName("Telephone_Type")
	private Object telephoneType;

	@SerializedName("Middle_Name2")
	private Object middleName2;

	@SerializedName("Middle_Name1")
	private Object middleName1;

	@SerializedName("MobilePhoneNumber")
	private String mobilePhoneNumber;

	@SerializedName("Middle_Name3")
	private Object middleName3;

	@SerializedName("First_Name")
	private String firstName;

	@SerializedName("Ration_Card_Issue_Date")
	private Object rationCardIssueDate;

	@SerializedName("Telephone_Extension")
	private Object telephoneExtension;

	@SerializedName("Driver_License_Number")
	private Object driverLicenseNumber;

	@SerializedName("Voter_ID_Issue_Date")
	private Object voterIDIssueDate;

	@SerializedName("Gender_Code")
	private Object genderCode;

	@SerializedName("PAN_Expiration_Date")
	private Object pANExpirationDate;

	@SerializedName("Universal_ID_Number")
	private Object universalIDNumber;

	@SerializedName("Universal_ID_Expiration_Date")
	private Object universalIDExpirationDate;

	@SerializedName("Ration_Card_Number")
	private Object rationCardNumber;

	@SerializedName("Driver_License_Expiration_Date")
	private Object driverLicenseExpirationDate;

	@SerializedName("EMailId")
	private Object eMailId;

	@SerializedName("Ration_Card_Expiration_Date")
	private Object rationCardExpirationDate;

	@SerializedName("Universal_ID_Issue_Date")
	private Object universalIDIssueDate;

	@SerializedName("Date_Of_Birth_Applicant")
	private Object dateOfBirthApplicant;

	@SerializedName("Last_Name")
	private Object lastName;

	@SerializedName("Driver_License_Issue_Date")
	private Object driverLicenseIssueDate;

	@SerializedName("Passport_Expiration_Date")
	private Object passportExpirationDate;

	@SerializedName("Passport_Issue_Date")
	private Object passportIssueDate;

	public String getIncomeTaxPan(){
		return incomeTaxPan;
	}

	public Object getPANIssueDate(){
		return pANIssueDate;
	}

	public Object getPassportNumber(){
		return passportNumber;
	}

	public Object getVoterIDExpirationDate(){
		return voterIDExpirationDate;
	}

	public Object getTelephoneNumberApplicant1st(){
		return telephoneNumberApplicant1st;
	}

	public Object getVoterSIdentityCard(){
		return voterSIdentityCard;
	}

	public Object getTelephoneType(){
		return telephoneType;
	}

	public Object getMiddleName2(){
		return middleName2;
	}

	public Object getMiddleName1(){
		return middleName1;
	}

	public String getMobilePhoneNumber(){
		return mobilePhoneNumber;
	}

	public Object getMiddleName3(){
		return middleName3;
	}

	public String getFirstName(){
		return firstName;
	}

	public Object getRationCardIssueDate(){
		return rationCardIssueDate;
	}

	public Object getTelephoneExtension(){
		return telephoneExtension;
	}

	public Object getDriverLicenseNumber(){
		return driverLicenseNumber;
	}

	public Object getVoterIDIssueDate(){
		return voterIDIssueDate;
	}

	public Object getGenderCode(){
		return genderCode;
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

	public Object getRationCardNumber(){
		return rationCardNumber;
	}

	public Object getDriverLicenseExpirationDate(){
		return driverLicenseExpirationDate;
	}

	public Object getEMailId(){
		return eMailId;
	}

	public Object getRationCardExpirationDate(){
		return rationCardExpirationDate;
	}

	public Object getUniversalIDIssueDate(){
		return universalIDIssueDate;
	}

	public Object getDateOfBirthApplicant(){
		return dateOfBirthApplicant;
	}

	public Object getLastName(){
		return lastName;
	}

	public Object getDriverLicenseIssueDate(){
		return driverLicenseIssueDate;
	}

	public Object getPassportExpirationDate(){
		return passportExpirationDate;
	}

	public Object getPassportIssueDate(){
		return passportIssueDate;
	}
}