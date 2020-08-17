/**
 * 
 */
package com.church.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.church.model.Event;

/**
 * @author pradheep
 *
 */
@Repository
public interface EventRepository extends MongoRepository<Event, String> {

}
