package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CurrentApplicantAddressDetailsItem{

	@SerializedName("Country_Code")
	private String countryCode;

	@SerializedName("RoadNoNameAreaLocality")
	private Object roadNoNameAreaLocality;

	@SerializedName("State")
	private Object state;

	@SerializedName("Landmark")
	private Object landmark;

	@SerializedName("BldgNoSocietyName")
	private Object bldgNoSocietyName;

	@SerializedName("FlatNoPlotNoHouseNo")
	private Object flatNoPlotNoHouseNo;

	@SerializedName("City")
	private Object city;

	@SerializedName("PINCode")
	private Object pINCode;

	public String getCountryCode(){
		return countryCode;
	}

	public Object getRoadNoNameAreaLocality(){
		return roadNoNameAreaLocality;
	}

	public Object getState(){
		return state;
	}

	public Object getLandmark(){
		return landmark;
	}

	public Object getBldgNoSocietyName(){
		return bldgNoSocietyName;
	}

	public Object getFlatNoPlotNoHouseNo(){
		return flatNoPlotNoHouseNo;
	}

	public Object getCity(){
		return city;
	}

	public Object getPINCode(){
		return pINCode;
	}
}