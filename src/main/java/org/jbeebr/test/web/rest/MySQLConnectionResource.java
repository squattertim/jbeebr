package org.jbeebr.test.web.rest;

import org.jbeebr.test.domain.MySQLConnection;
import org.jbeebr.test.repository.MySQLConnectionRepository;
import org.jbeebr.test.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.jbeebr.test.domain.MySQLConnection}.
 */
@RestController
@RequestMapping("/api")
public class MySQLConnectionResource {

    private final Logger log = LoggerFactory.getLogger(MySQLConnectionResource.class);

    private static final String ENTITY_NAME = "mySQLConnection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MySQLConnectionRepository mySQLConnectionRepository;

    public MySQLConnectionResource(MySQLConnectionRepository mySQLConnectionRepository) {
        this.mySQLConnectionRepository = mySQLConnectionRepository;
    }

    /**
     * {@code POST  /my-sql-connections} : Create a new mySQLConnection.
     *
     * @param mySQLConnection the mySQLConnection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mySQLConnection, or with status {@code 400 (Bad Request)} if the mySQLConnection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/my-sql-connections")
    public ResponseEntity<MySQLConnection> createMySQLConnection(@RequestBody MySQLConnection mySQLConnection) throws URISyntaxException {
        log.debug("REST request to save MySQLConnection : {}", mySQLConnection);
        if (mySQLConnection.getId() != null) {
            throw new BadRequestAlertException("A new mySQLConnection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MySQLConnection result = mySQLConnectionRepository.save(mySQLConnection);
        return ResponseEntity.created(new URI("/api/my-sql-connections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /my-sql-connections} : Updates an existing mySQLConnection.
     *
     * @param mySQLConnection the mySQLConnection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mySQLConnection,
     * or with status {@code 400 (Bad Request)} if the mySQLConnection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mySQLConnection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/my-sql-connections")
    public ResponseEntity<MySQLConnection> updateMySQLConnection(@RequestBody MySQLConnection mySQLConnection) throws URISyntaxException {
        log.debug("REST request to update MySQLConnection : {}", mySQLConnection);
        if (mySQLConnection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MySQLConnection result = mySQLConnectionRepository.save(mySQLConnection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mySQLConnection.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /my-sql-connections} : get all the mySQLConnections.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mySQLConnections in body.
     */
    @GetMapping("/my-sql-connections")
    public List<MySQLConnection> getAllMySQLConnections() {
        log.debug("REST request to get all MySQLConnections");
        return mySQLConnectionRepository.findAll();
    }

    /**
     * {@code GET  /my-sql-connections/:id} : get the "id" mySQLConnection.
     *
     * @param id the id of the mySQLConnection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mySQLConnection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/my-sql-connections/{id}")
    public ResponseEntity<MySQLConnection> getMySQLConnection(@PathVariable String id) {
        log.debug("REST request to get MySQLConnection : {}", id);
        Optional<MySQLConnection> mySQLConnection = mySQLConnectionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(mySQLConnection);
    }

    /**
     * {@code DELETE  /my-sql-connections/:id} : delete the "id" mySQLConnection.
     *
     * @param id the id of the mySQLConnection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/my-sql-connections/{id}")
    public ResponseEntity<Void> deleteMySQLConnection(@PathVariable String id) {
        log.debug("REST request to delete MySQLConnection : {}", id);
        mySQLConnectionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
