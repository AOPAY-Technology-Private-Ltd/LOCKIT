package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CAISHolderAddressDetailsItem{

	@SerializedName("State_non_normalized")
	private String stateNonNormalized;

	@SerializedName("Fifth_Line_Of_Address_non_normalized")
	private Object fifthLineOfAddressNonNormalized;

	@SerializedName("Residence_code_non_normalized")
	private Object residenceCodeNonNormalized;

	@SerializedName("First_Line_Of_Address_non_normalized")
	private String firstLineOfAddressNonNormalized;

	@SerializedName("City_non_normalized")
	private Object cityNonNormalized;

	@SerializedName("Second_Line_Of_Address_non_normalized")
	private Object secondLineOfAddressNonNormalized;

	@SerializedName("ZIP_Postal_Code_non_normalized")
	private String zIPPostalCodeNonNormalized;

	@SerializedName("Address_indicator_non_normalized")
	private String addressIndicatorNonNormalized;

	@SerializedName("Third_Line_Of_Address_non_normalized")
	private Object thirdLineOfAddressNonNormalized;

	@SerializedName("CountryCode_non_normalized")
	private String countryCodeNonNormalized;

	public String getStateNonNormalized(){
		return stateNonNormalized;
	}

	public Object getFifthLineOfAddressNonNormalized(){
		return fifthLineOfAddressNonNormalized;
	}

	public Object getResidenceCodeNonNormalized(){
		return residenceCodeNonNormalized;
	}

	public String getFirstLineOfAddressNonNormalized(){
		return firstLineOfAddressNonNormalized;
	}

	public Object getCityNonNormalized(){
		return cityNonNormalized;
	}

	public Object getSecondLineOfAddressNonNormalized(){
		return secondLineOfAddressNonNormalized;
	}

	public String getZIPPostalCodeNonNormalized(){
		return zIPPostalCodeNonNormalized;
	}

	public String getAddressIndicatorNonNormalized(){
		return addressIndicatorNonNormalized;
	}

	public Object getThirdLineOfAddressNonNormalized(){
		return thirdLineOfAddressNonNormalized;
	}

	public String getCountryCodeNonNormalized(){
		return countryCodeNonNormalized;
	}
}