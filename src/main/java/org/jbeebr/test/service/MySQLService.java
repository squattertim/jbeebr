package org.jbeebr.test.service;

import org.jbeebr.test.domain.MySQLConnection;
import org.jbeebr.test.repository.MySQLConnectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MySQLService {

    private final Logger log = LoggerFactory.getLogger(MySQLService.class);

    private final MySQLConnectionRepository connectionRepository;

    public MySQLService(MySQLConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public String getConnectionDetails(MySQLConnection connection) {
        StringBuilder info = new StringBuilder();
        info.append("connected");
        return info.toString();
    }
}
