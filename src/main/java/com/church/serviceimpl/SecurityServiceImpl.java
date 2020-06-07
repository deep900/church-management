/**
 * 
 */
package com.church.serviceimpl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.church.model.ApplicationUser;
import com.church.model.SessionData;
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
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(new Date());
		gregorianCalendar.add(GregorianCalendar.HOUR_OF_DAY, -5);
		Timestamp toCompare = new Timestamp(gregorianCalendar.getTimeInMillis());
		Query query = new Query();		
		query.addCriteria(Criteria.where("timeIssued").lt(toCompare));
		List<SessionData> sessionDataList = mongoOps.findAllAndRemove(query, SessionData.class);
		log.info("Removed session data unused: " + sessionDataList.size());
	}

	@Override
	public SessionData getSessionDataById(String id) {
		return mongoOps.findById(id, SessionData.class);
	}

	@Override
	public ApplicationUser getUserByEmail(String emailAddress) {
		Query query = new Query();
		query.addCriteria(Criteria.where("emailAddress").is(emailAddress));
		return mongoOps.findOne(query, ApplicationUser.class);
	}

	@Override
	public void updateUser(Update updateObj, String emailAddress) {
		Query query = new Query();
		query.addCriteria(Criteria.where("emailAddress").is(emailAddress));
		mongoOps.updateFirst(query, updateObj, ApplicationUser.class);
		log.info("User updated successfullt [Email]:" + emailAddress);
	}

	@Override
	public List<ApplicationUser> getAllUsers() {
		return mongoOps.findAll(ApplicationUser.class);
	}

	@Override
	public List<ApplicationUser> getUserByPrevilege(String previlege) {
		return null;
	}
}
