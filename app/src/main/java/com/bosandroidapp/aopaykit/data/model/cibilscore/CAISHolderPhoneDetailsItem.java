package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CAISHolderPhoneDetailsItem{

	@SerializedName("Telephone_Extension")
	private Object telephoneExtension;

	@SerializedName("Telephone_Number")
	private Object telephoneNumber;

	@SerializedName("Mobile_Telephone_Number")
	private String mobileTelephoneNumber;

	@SerializedName("FaxNumber")
	private Object faxNumber;

	@SerializedName("Telephone_Type")
	private String telephoneType;

	@SerializedName("EMailId")
	private String eMailId;

	public Object getTelephoneExtension(){
		return telephoneExtension;
	}

	public Object getTelephoneNumber(){
		return telephoneNumber;
	}

	public String getMobileTelephoneNumber(){
		return mobileTelephoneNumber;
	}

	public Object getFaxNumber(){
		return faxNumber;
	}

	public String getTelephoneType(){
		return telephoneType;
	}

	public String getEMailId(){
		return eMailId;
	}
}