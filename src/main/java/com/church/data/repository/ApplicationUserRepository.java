/**
 * 
 */
package com.church.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.church.model.ApplicationUser;

/**
 * @author pradheep
 *
 */
@Repository
public interface ApplicationUserRepository extends MongoRepository<ApplicationUser, String> {

	@Query(value = "{'emailAddress':?0}")
	public ApplicationUser findByEmail(String email);
}
