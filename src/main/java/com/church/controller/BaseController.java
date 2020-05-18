/**
 * 
 */
package com.church.controller;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.church.model.ResponseModel;
import com.church.util.ApplicationConstants;

/**
 * @author pradheep
 *
 */
public class BaseController {
	
	public ResponseModel getDefaultResponseModel(){
		ResponseModel responseModel = new ResponseModel();
		responseModel.setResponseCode(HttpStatus.OK.value());
		responseModel.setResponseMessage(ApplicationConstants.SUCCESS_RESPONSE);
		return responseModel;
	}
}
