package com.bosandroidapp.aopaykit.data.model.cibilscore;

import com.google.gson.annotations.SerializedName;

public class CibilScroeResp{

	@SerializedName("result")
	private Result result;

	@SerializedName("Status")
	private String status;

	@SerializedName("Value")
	private String value;

	@SerializedName("result_code")
	private String resultCode;

	@SerializedName("client_ref_num")
	private String clientRefNum;

	@SerializedName("message")
	private String message;

	@SerializedName("request_id")
	private String requestId;

	@SerializedName("http_response_code")
	private String httpResponseCode;

	public Result getResult(){
		return result;
	}

	public String getStatus(){
		return status;
	}

	public String getValue(){
		return value;
	}

	public String getResultCode(){
		return resultCode;
	}

	public String getClientRefNum(){
		return clientRefNum;
	}

	public String getMessage(){
		return message;
	}

	public String getRequestId(){
		return requestId;
	}

	public String getHttpResponseCode(){
		return httpResponseCode;
	}
}