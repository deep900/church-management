/**
 * 
 */
package com.church.data.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.church.model.Task;

/**
 * @author pradheep
 *
 */
@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

	@Query("{ 'presentState' : ?0 }")
	public List<Task> findTaskByState(String taskState);

	@Query("SELECT t FROM Task t WHERE t.presentState not like ?0")
	public List<Task> findTaskNotInState(String taskState);

	@Query("{ 'eventId' : ?0 }")
	public List<Task> findTaskByEventId(String eventId);
	
}
