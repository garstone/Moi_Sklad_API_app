import com.kamenev.controller.PurchaseServlet;
import config.DBUnitConfig;
import config.WebTestConfig;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Properties;

public class PurchaseServletTest extends DBUnitConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebTestConfig.class);
    private Properties prop;
    private Connection connection;
    protected IDatabaseTester tester;
    protected IDataSet beforeData;
    private PurchaseServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public PurchaseServletTest(String name) throws Exception {
        super(name);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        servlet = new PurchaseServlet();
        request = Mockito.mock(request.getClass());
        response = Mockito.mock(response.getClass());
    }

    @Test
    public void testPurchase() throws Exception {

    }
}
