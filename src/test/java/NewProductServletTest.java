import com.kamenev.controller.NewProductServlet;
import com.kamenev.controller.PurchaseServlet;
import config.DBUnitConfig;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

import static org.mockito.Mockito.when;

public class NewProductServletTest extends DBUnitConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewProductServletTest.class);
    private static final String MOCK_URL = "/newproduct";
    private String json = "{\"model\": \"iphone\"}";
    String path = new File("src/test/java/data/").getAbsolutePath();

    private Properties prop;
    private Connection connection;
    protected IDatabaseTester tester;
    protected IDataSet beforeData;
    NewProductServlet servlet;
    private StringWriter responseWriter;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private ServletContext context;

    public NewProductServletTest(String name) throws Exception {
        super(name);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setUp();
        tester = super.tester;
        when(mockRequest.getRequestURI()).thenReturn(MOCK_URL);
        servlet = new NewProductServlet();
        responseWriter = new StringWriter();
        connection = tester.getConnection().getConnection();
        PreparedStatement pst = connection.prepareStatement("DELETE FROM products");
        pst.executeUpdate();
        Statement st = connection.createStatement();
        st.execute("ALTER SEQUENCE products_id_seq RESTART WITH 1");
        context.setAttribute("DBConnection", connection);
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
        when(mockRequest.getReader()).thenReturn(new BufferedReader(new StringReader(json)));
        when(mockRequest.getServletContext()).thenReturn(context);
        when(mockRequest.getServletContext().getAttribute("DBConnection")).thenReturn(connection);
    }

    @Test
    public void testDoPost() throws Exception {
        servlet.doPost(mockRequest, mockResponse);
        String filePath = new File(path + "/newProductServletData.xml").getAbsolutePath();
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet expectedData = builder.build(new File(filePath));
        ITable expectedTable = expectedData.getTable("products");
        IDataSet actualData = tester.getConnection().createDataSet();
        ITable actualTable = actualData.getTable("products");
        Assert.assertEquals("Данная модель успешно добавлена в базу данных." + System.getProperty("line.separator"),
                responseWriter.toString());
        Assertion.assertEquals(expectedTable, actualTable);
    }

    @After
    public void tearDown() throws Exception {
        PreparedStatement pst = connection.prepareStatement("DELETE FROM products");
        pst.executeUpdate();
        Statement st = connection.createStatement();
        st.execute("ALTER SEQUENCE products_id_seq RESTART WITH 1");
    }

}
