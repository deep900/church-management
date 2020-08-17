/**
 * 
 */
package com.church.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.church.model.BlackListToken;

/**
 * @author pradheep
 *
 */
public interface BlacklistedTokenRepository extends MongoRepository<BlackListToken, String> {

}
