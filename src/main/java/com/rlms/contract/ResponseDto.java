package com.rlms.contract;

import java.util.List;

import org.json.JSONArray;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


public class ResponseDto {

	private boolean status;
	private Object response;
	

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
	
}
