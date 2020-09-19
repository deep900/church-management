/**
 * 
 */
package com.church.data.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.church.model.TaskForEngineer;

/**
 * @author pradheep
 *
 */
@Repository
public interface TaskAndUserRepository extends MongoRepository<TaskForEngineer, String> {

	@Query(value="{'userId':?0}")
	public List<TaskForEngineer> getTaskForEngineerByUserId(String userId);
}
