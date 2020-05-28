/**
 * 
 */
package com.church.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.church.model.SessionData;
import com.church.model.Worker;
import com.church.service.SecurityService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pradheep
 *
 */
@Service
@Slf4j
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private MongoOperations mongoOps;

	@Override
	public void saveSession(SessionData sessionData) {
		try {
			mongoOps.save(sessionData);
			log.info("Successfully saved the session details");
		} catch (Exception err) {
			log.error("Error while saving session details", err);
		}
	}

	/**
	 * Shall be used to update the details like captcha for a session.
	 */
	@Override
	public void updateSession(SessionData sessionData) {
		
	}

	@Override
	public List<SessionData> getAllActiveSession() {
		return null;
	}	

	@Override
	public void clearUnusedSession() {
		log.info("Clearing the unused session data");
		// TODO
	}
	
	@Override
	public SessionData getSessionDataById(String id) {
		return mongoOps.findById(id, SessionData.class);		
	}
	
	@Override
	public Worker getWorkerByEmail(String emailAddress) {

		return null;
	}

	@Override
	public void updateWorker(Worker workerObj) {

	}

	@Override
	public List<Worker> getAllWorkers() {

		return null;
	}

	@Override
	public List<Worker> getWorkerByPrevilege(String previlege) {

		return null;
	}
}
