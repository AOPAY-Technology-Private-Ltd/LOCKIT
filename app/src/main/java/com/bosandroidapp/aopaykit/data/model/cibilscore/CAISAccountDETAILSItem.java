package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CAISAccountDETAILSItem{

	@SerializedName("AccountHoldertypeCode")
	private String accountHoldertypeCode;

	@SerializedName("LitigationStatusDate")
	private Object litigationStatusDate;

	@SerializedName("Open_Date")
	private String openDate;

	@SerializedName("Account_Type")
	private String accountType;

	@SerializedName("Income")
	private Object income;

	@SerializedName("Subscriber_comments")
	private Object subscriberComments;

	@SerializedName("CurrencyCode")
	private String currencyCode;

	@SerializedName("CAIS_Holder_Details")
	private List<CAISHolderDetailsItem> cAISHolderDetails;

	@SerializedName("Payment_History_Profile")
	private String paymentHistoryProfile;

	@SerializedName("Portfolio_Type")
	private String portfolioType;

	@SerializedName("DateOfAddition")
	private String dateOfAddition;

	@SerializedName("Payment_Rating")
	private String paymentRating;

	@SerializedName("Value_of_Collateral")
	private String valueOfCollateral;

	@SerializedName("Occupation_Code")
	private Object occupationCode;

	@SerializedName("Rate_of_stringerest")
	private Object rateOfStringerest;

	@SerializedName("Subscriber_Name")
	private String subscriberName;

	@SerializedName("Credit_Limit_Amount")
	private Object creditLimitAmount;

	@SerializedName("SuitFiled_WilfulDefault")
	private Object suitFiledWilfulDefault;

	@SerializedName("Written_off_Settled_Status")
	private Object writtenOffSettledStatus;

	@SerializedName("Written_Off_Amt_Total")
	private Object writtenOffAmtTotal;

	@SerializedName("Promotional_Rate_Flag")
	private Object promotionalRateFlag;

	@SerializedName("CAIS_Account_History")
	private List<CAISAccountHistoryItem> cAISAccountHistory;

	@SerializedName("CAIS_Holder_Address_Details")
	private List<CAISHolderAddressDetailsItem> cAISHolderAddressDetails;

	@SerializedName("Date_Reported")
	private String dateReported;

	@SerializedName("SuitFiledWillfulDefaultWrittenOffStatus")
	private Object suitFiledWillfulDefaultWrittenOffStatus;

	@SerializedName("CAIS_Holder_Phone_Details")
	private List<CAISHolderPhoneDetailsItem> cAISHolderPhoneDetails;

	@SerializedName("Value_of_Credits_Last_Month")
	private Object valueOfCreditsLastMonth;

	@SerializedName("Date_Closed")
	private String dateClosed;

	@SerializedName("Current_Balance")
	private String currentBalance;

	@SerializedName("Scheduled_Monthly_Payment_Amount")
	private Object scheduledMonthlyPaymentAmount;

	@SerializedName("Amount_Past_Due")
	private String amountPastDue;

	@SerializedName("Date_of_First_Delinquency")
	private Object dateOfFirstDelinquency;

	@SerializedName("Terms_Frequency")
	private Object termsFrequency;

	@SerializedName("CAIS_Holder_ID_Details")
	private List<CAISHolderIDDetailsItem> cAISHolderIDDetails;

	@SerializedName("Account_Number")
	private String accountNumber;

	@SerializedName("Date_of_Last_Payment")
	private String dateOfLastPayment;

	@SerializedName("Special_Comment")
	private Object specialComment;

	@SerializedName("DefaultStatusDate")
	private Object defaultStatusDate;

	@SerializedName("Income_Indicator")
	private Object incomeIndicator;

	@SerializedName("Highest_Credit_or_Original_Loan_Amount")
	private String highestCreditOrOriginalLoanAmount;

	@SerializedName("Settlement_Amount")
	private Object settlementAmount;

	@SerializedName("Original_Charge_Off_Amount")
	private Object originalChargeOffAmount;

	@SerializedName("Type_of_Collateral")
	private Object typeOfCollateral;

	@SerializedName("WriteOffStatusDate")
	private Object writeOffStatusDate;

	@SerializedName("Written_Off_Amt_Principal")
	private Object writtenOffAmtPrincipal;

	@SerializedName("Income_Frequency_Indicator")
	private Object incomeFrequencyIndicator;

	@SerializedName("Consumer_comments")
	private Object consumerComments;

	@SerializedName("Repayment_Tenure")
	private String repaymentTenure;

	@SerializedName("Terms_Duration")
	private Object termsDuration;

	@SerializedName("Identification_Number")
	private String identificationNumber;

	@SerializedName("Account_Status")
	private String accountStatus;

	public String getAccountHoldertypeCode(){
		return accountHoldertypeCode;
	}

	public Object getLitigationStatusDate(){
		return litigationStatusDate;
	}

	public String getOpenDate(){
		return openDate;
	}

	public String getAccountType(){
		return accountType;
	}

	public Object getIncome(){
		return income;
	}

	public Object getSubscriberComments(){
		return subscriberComments;
	}

	public String getCurrencyCode(){
		return currencyCode;
	}

	public List<CAISHolderDetailsItem> getCAISHolderDetails(){
		return cAISHolderDetails;
	}

	public String getPaymentHistoryProfile(){
		return paymentHistoryProfile;
	}

	public String getPortfolioType(){
		return portfolioType;
	}

	public String getDateOfAddition(){
		return dateOfAddition;
	}

	public String getPaymentRating(){
		return paymentRating;
	}

	public String getValueOfCollateral(){
		return valueOfCollateral;
	}

	public Object getOccupationCode(){
		return occupationCode;
	}

	public Object getRateOfStringerest(){
		return rateOfStringerest;
	}

	public String getSubscriberName(){
		return subscriberName;
	}

	public Object getCreditLimitAmount(){
		return creditLimitAmount;
	}

	public Object getSuitFiledWilfulDefault(){
		return suitFiledWilfulDefault;
	}

	public Object getWrittenOffSettledStatus(){
		return writtenOffSettledStatus;
	}

	public Object getWrittenOffAmtTotal(){
		return writtenOffAmtTotal;
	}

	public Object getPromotionalRateFlag(){
		return promotionalRateFlag;
	}

	public List<CAISAccountHistoryItem> getCAISAccountHistory(){
		return cAISAccountHistory;
	}

	public List<CAISHolderAddressDetailsItem> getCAISHolderAddressDetails(){
		return cAISHolderAddressDetails;
	}

	public String getDateReported(){
		return dateReported;
	}

	public Object getSuitFiledWillfulDefaultWrittenOffStatus(){
		return suitFiledWillfulDefaultWrittenOffStatus;
	}

	public List<CAISHolderPhoneDetailsItem> getCAISHolderPhoneDetails(){
		return cAISHolderPhoneDetails;
	}

	public Object getValueOfCreditsLastMonth(){
		return valueOfCreditsLastMonth;
	}

	public String getDateClosed(){
		return dateClosed;
	}

	public String getCurrentBalance(){
		return currentBalance;
	}

	public Object getScheduledMonthlyPaymentAmount(){
		return scheduledMonthlyPaymentAmount;
	}

	public String getAmountPastDue(){
		return amountPastDue;
	}

	public Object getDateOfFirstDelinquency(){
		return dateOfFirstDelinquency;
	}

	public Object getTermsFrequency(){
		return termsFrequency;
	}

	public List<CAISHolderIDDetailsItem> getCAISHolderIDDetails(){
		return cAISHolderIDDetails;
	}

	public String getAccountNumber(){
		return accountNumber;
	}

	public String getDateOfLastPayment(){
		return dateOfLastPayment;
	}

	public Object getSpecialComment(){
		return specialComment;
	}

	public Object getDefaultStatusDate(){
		return defaultStatusDate;
	}

	public Object getIncomeIndicator(){
		return incomeIndicator;
	}

	public String getHighestCreditOrOriginalLoanAmount(){
		return highestCreditOrOriginalLoanAmount;
	}

	public Object getSettlementAmount(){
		return settlementAmount;
	}

	public Object getOriginalChargeOffAmount(){
		return originalChargeOffAmount;
	}

	public Object getTypeOfCollateral(){
		return typeOfCollateral;
	}

	public Object getWriteOffStatusDate(){
		return writeOffStatusDate;
	}

	public Object getWrittenOffAmtPrincipal(){
		return writtenOffAmtPrincipal;
	}

	public Object getIncomeFrequencyIndicator(){
		return incomeFrequencyIndicator;
	}

	public Object getConsumerComments(){
		return consumerComments;
	}

	public String getRepaymentTenure(){
		return repaymentTenure;
	}

	public Object getTermsDuration(){
		return termsDuration;
	}

	public String getIdentificationNumber(){
		return identificationNumber;
	}

	public String getAccountStatus(){
		return accountStatus;
	}
}