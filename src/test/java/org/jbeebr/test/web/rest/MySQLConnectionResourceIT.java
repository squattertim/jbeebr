package org.jbeebr.test.web.rest;

import org.jbeebr.test.JbeebrApp;
import org.jbeebr.test.domain.MySQLConnection;
import org.jbeebr.test.repository.MySQLConnectionRepository;
import org.jbeebr.test.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.List;

import static org.jbeebr.test.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MySQLConnectionResource} REST controller.
 */
@SpringBootTest(classes = JbeebrApp.class)
public class MySQLConnectionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HOSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_HOSTNAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PORT = 1;
    private static final Integer UPDATED_PORT = 2;

    private static final String DEFAULT_DB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private MySQLConnectionRepository mySQLConnectionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restMySQLConnectionMockMvc;

    private MySQLConnection mySQLConnection;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MySQLConnectionResource mySQLConnectionResource = new MySQLConnectionResource(mySQLConnectionRepository);
        this.restMySQLConnectionMockMvc = MockMvcBuilders.standaloneSetup(mySQLConnectionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MySQLConnection createEntity() {
        MySQLConnection mySQLConnection = new MySQLConnection()
            .name(DEFAULT_NAME)
            .hostname(DEFAULT_HOSTNAME)
            .port(DEFAULT_PORT)
            .dbName(DEFAULT_DB_NAME)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD);
        return mySQLConnection;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MySQLConnection createUpdatedEntity() {
        MySQLConnection mySQLConnection = new MySQLConnection()
            .name(UPDATED_NAME)
            .hostname(UPDATED_HOSTNAME)
            .port(UPDATED_PORT)
            .dbName(UPDATED_DB_NAME)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD);
        return mySQLConnection;
    }

    @BeforeEach
    public void initTest() {
        mySQLConnectionRepository.deleteAll();
        mySQLConnection = createEntity();
    }

    @Test
    public void createMySQLConnection() throws Exception {
        int databaseSizeBeforeCreate = mySQLConnectionRepository.findAll().size();

        // Create the MySQLConnection
        restMySQLConnectionMockMvc.perform(post("/api/my-sql-connections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mySQLConnection)))
            .andExpect(status().isCreated());

        // Validate the MySQLConnection in the database
        List<MySQLConnection> mySQLConnectionList = mySQLConnectionRepository.findAll();
        assertThat(mySQLConnectionList).hasSize(databaseSizeBeforeCreate + 1);
        MySQLConnection testMySQLConnection = mySQLConnectionList.get(mySQLConnectionList.size() - 1);
        assertThat(testMySQLConnection.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMySQLConnection.getHostname()).isEqualTo(DEFAULT_HOSTNAME);
        assertThat(testMySQLConnection.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testMySQLConnection.getDbName()).isEqualTo(DEFAULT_DB_NAME);
        assertThat(testMySQLConnection.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testMySQLConnection.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    public void createMySQLConnectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = mySQLConnectionRepository.findAll().size();

        // Create the MySQLConnection with an existing ID
        mySQLConnection.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restMySQLConnectionMockMvc.perform(post("/api/my-sql-connections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mySQLConnection)))
            .andExpect(status().isBadRequest());

        // Validate the MySQLConnection in the database
        List<MySQLConnection> mySQLConnectionList = mySQLConnectionRepository.findAll();
        assertThat(mySQLConnectionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllMySQLConnections() throws Exception {
        // Initialize the database
        mySQLConnectionRepository.save(mySQLConnection);

        // Get all the mySQLConnectionList
        restMySQLConnectionMockMvc.perform(get("/api/my-sql-connections?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mySQLConnection.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].hostname").value(hasItem(DEFAULT_HOSTNAME)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].dbName").value(hasItem(DEFAULT_DB_NAME)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }
    
    @Test
    public void getMySQLConnection() throws Exception {
        // Initialize the database
        mySQLConnectionRepository.save(mySQLConnection);

        // Get the mySQLConnection
        restMySQLConnectionMockMvc.perform(get("/api/my-sql-connections/{id}", mySQLConnection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mySQLConnection.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.hostname").value(DEFAULT_HOSTNAME))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT))
            .andExpect(jsonPath("$.dbName").value(DEFAULT_DB_NAME))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    public void getNonExistingMySQLConnection() throws Exception {
        // Get the mySQLConnection
        restMySQLConnectionMockMvc.perform(get("/api/my-sql-connections/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateMySQLConnection() throws Exception {
        // Initialize the database
        mySQLConnectionRepository.save(mySQLConnection);

        int databaseSizeBeforeUpdate = mySQLConnectionRepository.findAll().size();

        // Update the mySQLConnection
        MySQLConnection updatedMySQLConnection = mySQLConnectionRepository.findById(mySQLConnection.getId()).get();
        updatedMySQLConnection
            .name(UPDATED_NAME)
            .hostname(UPDATED_HOSTNAME)
            .port(UPDATED_PORT)
            .dbName(UPDATED_DB_NAME)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD);

        restMySQLConnectionMockMvc.perform(put("/api/my-sql-connections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMySQLConnection)))
            .andExpect(status().isOk());

        // Validate the MySQLConnection in the database
        List<MySQLConnection> mySQLConnectionList = mySQLConnectionRepository.findAll();
        assertThat(mySQLConnectionList).hasSize(databaseSizeBeforeUpdate);
        MySQLConnection testMySQLConnection = mySQLConnectionList.get(mySQLConnectionList.size() - 1);
        assertThat(testMySQLConnection.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMySQLConnection.getHostname()).isEqualTo(UPDATED_HOSTNAME);
        assertThat(testMySQLConnection.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testMySQLConnection.getDbName()).isEqualTo(UPDATED_DB_NAME);
        assertThat(testMySQLConnection.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testMySQLConnection.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    public void updateNonExistingMySQLConnection() throws Exception {
        int databaseSizeBeforeUpdate = mySQLConnectionRepository.findAll().size();

        // Create the MySQLConnection

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMySQLConnectionMockMvc.perform(put("/api/my-sql-connections")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mySQLConnection)))
            .andExpect(status().isBadRequest());

        // Validate the MySQLConnection in the database
        List<MySQLConnection> mySQLConnectionList = mySQLConnectionRepository.findAll();
        assertThat(mySQLConnectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteMySQLConnection() throws Exception {
        // Initialize the database
        mySQLConnectionRepository.save(mySQLConnection);

        int databaseSizeBeforeDelete = mySQLConnectionRepository.findAll().size();

        // Delete the mySQLConnection
        restMySQLConnectionMockMvc.perform(delete("/api/my-sql-connections/{id}", mySQLConnection.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MySQLConnection> mySQLConnectionList = mySQLConnectionRepository.findAll();
        assertThat(mySQLConnectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
