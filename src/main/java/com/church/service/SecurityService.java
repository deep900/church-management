/**
 * 
 */
package com.church.service;

import java.util.List;

import com.church.model.SessionData;
import com.church.model.Worker;

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
	
	//---------------- Worker details --------------- //
	public Worker getWorkerByEmail(String emailAddress);
	
	public void updateWorker(Worker workerObj);
	
	public List<Worker> getAllWorkers();
	
	public List<Worker> getWorkerByPrevilege(String previlege);
	
	//-------------------------------------------------//
}
