package org.jbeebr.test.web.rest;

import org.jbeebr.test.domain.*;
import org.jbeebr.test.repository.MySQLConnectionRepository;
import org.jbeebr.test.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.sql.*;
import java.util.ArrayList;
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

    /**
     * {@code GET  /my-sql-connections/:id/schemas} : get all the mySQLConnection schemas.
     *
     * @param id
     * @return list od DB schemas
     */
    @GetMapping("/my-sql-connections/{id}/schemas")
    public List<DatabaseSchema> getMySQLConnectionSchemas(@PathVariable String id) {
        log.info("REST request to get schemas of MySQLConnection : {}", id);
        Optional<MySQLConnection> mySQLConnection = mySQLConnectionRepository.findById(id);

        List<DatabaseSchema> schemas = new ArrayList<DatabaseSchema>();

        if(mySQLConnection.isPresent()) {
            try {
                Connection conn = getConnection(mySQLConnection);
                // --- LISTING DATABASE SCHEMA NAMES ---
                ResultSet resultSet = conn.getMetaData().getCatalogs();
                while (resultSet.next()) {
                    log.info("Schema Name = " + resultSet.getString("TABLE_CAT"));
                    schemas.add(new DatabaseSchema(resultSet.getString("TABLE_CAT")));
                }
                resultSet.close();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        return schemas;
    }

    /**
     * {@code GET  /my-sql-connections/:id/tables} : get all the mySQLConnection tables.
     *
     * @param id
     * @return list od DB tables
     */
    @GetMapping("/my-sql-connections/{id}/tables")
    public List<DatabaseTable> getMySQLConnectionTables(@PathVariable String id) {
        log.info("REST request to get tables of MySQLConnection : {}", id);
        Optional<MySQLConnection> mySQLConnection = mySQLConnectionRepository.findById(id);

        List<DatabaseTable> tables = new ArrayList<DatabaseTable>();

        if(mySQLConnection.isPresent()) {
            try {
                String databaseName = mySQLConnection.get().getDbName();
                Connection conn = getConnection(mySQLConnection);
                // --- LISTING DATABASE TABLE NAMES ---
                String[] types = {"TABLE"};
                ResultSet resultSet = conn.getMetaData()
                    .getTables(databaseName, null, "%", types);
                String tableName = "";
                while (resultSet.next()) {
                    tableName = resultSet.getString(3);
                    log.info("Table Name = " + tableName);
                    tables.add(new DatabaseTable(tableName));
                }
                resultSet.close();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        return tables;
    }

    /**
     * {@code GET  /my-sql-connections/:id/columns/:tableName} : get all the mySQLConnection columns of the table.
     *
     * @param id
     * @param tableName
     * @return all columns of given table
     */
    @GetMapping("/my-sql-connections/{id}/columns/{tableName}")
    public List<DatabaseColumn> getMySQLConnectionColumns(@PathVariable String id, @PathVariable String tableName) {
        log.info("REST request to get columns of MySQLConnection : {}", id);
        Optional<MySQLConnection> mySQLConnection = mySQLConnectionRepository.findById(id);

        List<DatabaseColumn> columns = new ArrayList<DatabaseColumn>();

        if(mySQLConnection.isPresent()) {
            try {
                String databaseName = mySQLConnection.get().getDbName();
                Connection conn = getConnection(mySQLConnection);
                // --- LISTING DATABASE COLUMN NAMES ---
                DatabaseMetaData meta = conn.getMetaData();
                ResultSet resultSet = meta.getColumns(databaseName, null, tableName, "%");
                while (resultSet.next()) {
                    String columnName = resultSet.getString(4);
                    Integer dataType = resultSet.getInt(5);
                    log.info("Column Name of table " + tableName + " = "
                        + columnName);
                    columns.add(new DatabaseColumn(columnName, dataType));
                }
                resultSet.close();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        return columns;
    }

    /**
     * {@code GET  /my-sql-connections/:id/data/:tableName} : get all the data from DB table.
     *
     * @param id
     * @param tableName
     * @return list of all the data of given table
     */
    @GetMapping("/my-sql-connections/{id}/data/{tableName}")
    public List<DatabaseData> getMySQLConnectionTableData(@PathVariable String id, @PathVariable String tableName) {
        log.info("REST request to get columns of MySQLConnection : {}", id);
        Optional<MySQLConnection> mySQLConnection = mySQLConnectionRepository.findById(id);

        List<DatabaseColumn> columns = new ArrayList<DatabaseColumn>();
        List<DatabaseData> databaseData = new ArrayList<DatabaseData>();

        if(mySQLConnection.isPresent()) {
            try {
                String databaseName = mySQLConnection.get().getDbName();
                Connection conn = getConnection(mySQLConnection);
                // --- LISTING DATABASE COLUMN NAMES ---
                DatabaseMetaData meta = conn.getMetaData();
                Statement stmt = conn.createStatement();
                ResultSet resultSet = meta.getColumns(databaseName, null, tableName, "%");
                while (resultSet.next()) {
                    String columnName = resultSet.getString(4);
                    Integer dataType = resultSet.getInt(5);
                    log.info("Column Name of table " + tableName + " = "
                        + columnName);
                    columns.add(new DatabaseColumn(columnName, dataType));
                }

                resultSet = stmt.executeQuery("SELECT * FROM " + databaseName + "." + tableName);
                while (resultSet.next()) {
                    DatabaseData data = new DatabaseData();
                    for (DatabaseColumn col : columns) {
                        String columnName = col.getName();
                        int dataType = col.getDataType();
                        switch (dataType) {
                            case Types.CHAR:
                                String strValue = resultSet.getString(columnName);
                                log.info("Column Value of table " + tableName + " = "
                                    + columnName + ": " + strValue);
                                data.addValue(col.getName(), strValue);
                                break;
                            case Types.INTEGER:
                                Integer intValue = resultSet.getInt(columnName);
                                log.info("Column Value of table " + tableName + " = "
                                    + columnName + ": " + intValue);
                                data.addValue(col.getName(), intValue);
                                break;
                            default:
                                log.error("Unsupported column type " + tableName + " = "
                                    + columnName + ": " + dataType);
                        }
                    }
                    databaseData.add(data);
                }
                resultSet.close();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        return databaseData;
    }

    private Connection getConnection(Optional<MySQLConnection> mySQLConnection) throws ClassNotFoundException, SQLException {
        String userName = mySQLConnection.get().getUsername();
        String password = mySQLConnection.get().getPassword();
        String mySQLPort = mySQLConnection.get().getPort() != null ? mySQLConnection.get().getPort().toString() : "";
        String hostUrl = mySQLConnection.get().getHostname();
        // Setup the connection with the DB

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection("jdbc:mysql://" + hostUrl
            + ":" + mySQLPort, userName, password);
    }

}
