/**
 * 
 */
package com.church.service;

import java.util.List;

import org.springframework.data.mongodb.core.query.Update;

import com.church.model.SessionData;
import com.church.model.ApplicationUser;

/**
 * @author pradheep
 *
 */
public interface SecurityService {
	
	public static final String SESSION_ID = "sessionId";

	public void saveSession(SessionData sessionData);
	
	public List<SessionData> getAllActiveSession();
	
	public void updateSession(SessionData sessionData);
	
	/* Sessions older than 3 days will be cleared */
	public void clearUnusedSession();
	
	public SessionData getSessionDataById(String id);
	
	//---------------- User details --------------- //
	public ApplicationUser getUserByEmail(String emailAddress);
	
	public void updateUser(Update updateObj,String emailAddress);
	
	public List<ApplicationUser> getAllUsers();
	
	public List<ApplicationUser> getUserByPrevilege(String previlege);
	
	//-------------------------------------------------//
}
