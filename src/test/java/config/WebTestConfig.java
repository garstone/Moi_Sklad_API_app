package config;


import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

public class WebTestConfig extends DBUnitConfig {


    public WebTestConfig(String name) throws Exception {
        super(name);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }
}
