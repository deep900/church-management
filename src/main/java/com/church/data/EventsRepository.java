/**
 * 
 */
package com.church.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.church.model.ChurchEvent;

/**
 * @author pradheep
 *
 */
@Repository
public interface EventsRepository extends MongoRepository<ChurchEvent, String> {

}
