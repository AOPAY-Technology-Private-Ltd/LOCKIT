package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentApplicationDetails{

	@SerializedName("Current_Other_Details")
	private CurrentOtherDetails currentOtherDetails;

	@SerializedName("Current_Applicant_Additional_AddressDetails")
	private Object currentApplicantAdditionalAddressDetails;

	@SerializedName("Enquiry_Reason")
	private String enquiryReason;

	@SerializedName("Amount_Financed")
	private String amountFinanced;

	@SerializedName("Current_Applicant_Address_Details")
	private List<CurrentApplicantAddressDetailsItem> currentApplicantAddressDetails;

	@SerializedName("Duration_Of_Agreement")
	private String durationOfAgreement;

	@SerializedName("Finance_Purpose")
	private Object financePurpose;

	@SerializedName("Current_Applicant_Details")
	private CurrentApplicantDetails currentApplicantDetails;

	public CurrentOtherDetails getCurrentOtherDetails(){
		return currentOtherDetails;
	}

	public Object getCurrentApplicantAdditionalAddressDetails(){
		return currentApplicantAdditionalAddressDetails;
	}

	public String getEnquiryReason(){
		return enquiryReason;
	}

	public String getAmountFinanced(){
		return amountFinanced;
	}

	public List<CurrentApplicantAddressDetailsItem> getCurrentApplicantAddressDetails(){
		return currentApplicantAddressDetails;
	}

	public String getDurationOfAgreement(){
		return durationOfAgreement;
	}

	public Object getFinancePurpose(){
		return financePurpose;
	}

	public CurrentApplicantDetails getCurrentApplicantDetails(){
		return currentApplicantDetails;
	}
}