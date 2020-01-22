package org.jbeebr.test.repository;

import org.jbeebr.test.domain.MySQLConnection;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the MySQLConnection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MySQLConnectionRepository extends MongoRepository<MySQLConnection, String> {

}
