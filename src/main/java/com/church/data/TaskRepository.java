/**
 * 
 */
package com.church.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.church.model.Task;

/**
 * @author pradheep
 *
 */
@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

}
