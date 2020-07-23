/**
 * 
 */
package com.church.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.church.model.TaskForEngineer;

/**
 * @author pradheep
 *
 */
@Repository
public interface TaskAndUserRepository extends MongoRepository<TaskForEngineer, String> {

}
