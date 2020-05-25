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
		responseModel.setResponseCode(ApplicationConstants.SUCCESS_CODE);
		responseModel.setResponseMessage(ApplicationConstants.SUCCESS_RESPONSE);
		return responseModel;
	}
	
	public ResponseModel getFailureResponseModel(){
		ResponseModel responseModel = new ResponseModel();
		responseModel.setResponseCode(ApplicationConstants.FAILURE_CODE);
		responseModel.setResponseMessage(ApplicationConstants.FAILURE_RESPONSE);
		return responseModel;
	}
}
