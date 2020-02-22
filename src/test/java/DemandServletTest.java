import com.kamenev.controller.DemandServlet;
import com.kamenev.controller.PurchaseServlet;
import config.DBUnitConfig;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
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
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.mockito.Mockito.when;

public class DemandServletTest extends DBUnitConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseServletTest.class);
    private static final String MOCK_URL = "/demand";
    String path = new File("src/test/java/data/").getAbsolutePath();

    private Connection connection;
    protected IDatabaseTester tester;
    DemandServlet servlet;
    private StringWriter responseWriter;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private ServletContext context;

    public DemandServletTest(String name) throws Exception {
        super(name);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setUp();
        servlet = new DemandServlet();
        responseWriter = new StringWriter();
        tester = super.tester;
        connection = tester.getConnection().getConnection();
        Statement st = connection.createStatement();
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        beforeData = builder.build(new File(path + "/storeData.xml"));
        ReplacementDataSet rData = new ReplacementDataSet(beforeData);
        replaceDate(rData, "2019.10.10");
        replaceDate(rData, "2019.10.11");
        replaceDate(rData, "2019.10.12");
        tester.setDataSet(rData);
        tester.onSetup();
        context.setAttribute("DBConnection", connection);
        when(mockRequest.getRequestURI()).thenReturn(MOCK_URL);
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
        when(mockRequest.getReader())
                .thenReturn(new BufferedReader(new FileReader(new File(path + "/demandServletData.json"))));
        when(mockRequest.getServletContext()).thenReturn(context);
        when(mockRequest.getServletContext().getAttribute("DBConnection")).thenReturn(connection);
    }

    @Test
    public void testDoPost() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet expectedData = builder.build(new File(path + "/storeData-sale.xml"));
        ReplacementDataSet rExpected = new ReplacementDataSet(expectedData);
        replaceDate(rExpected, "2019.10.10");
        replaceDate(rExpected, "2019.10.11");
        replaceDate(rExpected, "2019.10.12");
        replaceDate(rExpected, "2019.10.13");
        ITable expectedTable = rExpected.getTable("store");
        servlet.doPost(mockRequest, mockResponse);
        IDataSet actualData = tester.getConnection().createDataSet();
        ITable actualTable = actualData.getTable("store");
        Assert.assertEquals("{\"income\": " + 3000 + "}" + System.getProperty("line.separator"),
                responseWriter.toString());
        Assertion.assertEquals(expectedTable, actualTable);
        salesReportTables(expectedTable, actualTable);
        Assertion.assertEquals(expectedTable, actualTable);
    }

    private void replaceDate(ReplacementDataSet rData, String dateString) throws ParseException {
        rData.addReplacementObject(dateString, new Date(new SimpleDateFormat("yyyy.MM.dd")
                .parse(dateString).getTime()));
    }

    private void salesReportTables(ITable expectedTable, ITable actualTable) throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet expectedData = builder.build(new File(path + "/salesReportData-sale.xml"));
        ReplacementDataSet rExpected = new ReplacementDataSet(expectedData);
        replaceDate(rExpected, "2019.10.15");
        expectedTable = rExpected.getTable("salesreport");
        IDataSet actualData = tester.getConnection().createDataSet();
        actualTable = actualData.getTable("salesreport");
    }

    @After
    public void tearDown() throws Exception {
        PreparedStatement pst = connection.prepareStatement("DELETE FROM salesreport");
        pst.executeUpdate();
        Statement st = connection.createStatement();
        st.execute("ALTER SEQUENCE salesreport_id_seq RESTART WITH 1");
    }
}
