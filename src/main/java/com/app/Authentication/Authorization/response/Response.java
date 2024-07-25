package com.app.Authentication.Authorization.response;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.Getter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

	private Object data;
	private HttpStatus status;
	private int StatusCode;
	private Error error;
	private String timeStamp;
	private String message;
	private List<String> errorMessages;

	@Override
	public String toString() {
		return "Response [response=" + data + ", error=" + error + ", timeStamp=" + timeStamp + "]";
	}
	
	public void setError(Error error) {
		this.error = error;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

}
