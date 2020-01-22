package org.jbeebr.test.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.jbeebr.test.web.rest.TestUtil;

public class MySQLConnectionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MySQLConnection.class);
        MySQLConnection mySQLConnection1 = new MySQLConnection();
        mySQLConnection1.setId("id1");
        MySQLConnection mySQLConnection2 = new MySQLConnection();
        mySQLConnection2.setId(mySQLConnection1.getId());
        assertThat(mySQLConnection1).isEqualTo(mySQLConnection2);
        mySQLConnection2.setId("id2");
        assertThat(mySQLConnection1).isNotEqualTo(mySQLConnection2);
        mySQLConnection1.setId(null);
        assertThat(mySQLConnection1).isNotEqualTo(mySQLConnection2);
    }
}
