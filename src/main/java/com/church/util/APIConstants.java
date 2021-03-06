/**
 * 
 */
package com.church.util;

/**
 * @author pradheep
 *
 */
public interface APIConstants {
	
	public static final String[] skipAuthenticationAPIArray = new String[] { APIConstants.AUTHENTICATE ,APIConstants.PREPARE_LOGIN};
	
	public static final String PREPARE_LOGIN = "/api/prepareForLogin";
	
	public static final String AUTHENTICATE = "/api/authenticateReq";
	
	public static final String GET_ALL_EVENTS = "/api/admin/getAllEvents";
	
	public static final String ADD_EVENT = "/api/admin/addEvent";
	
	public static final String DELETE_EVENT = "/api/admin/deleteEvent";	
	
	public static final String GET_ALL_USERS = "/api/admin/getAllUsers";
	
	public static final String ADD_USER = "/api/admin/addUser";
	
	public static final String UPDATE_TASK = "/api/updateTask";
	
	public static final String LOGOUT = "/api/logout";

}
