import com.kamenev.controller.DemandServlet;
import com.kamenev.controller.SalesReportServlet;
import config.DBUnitConfig;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
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
import java.sql.Date;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.mockito.Mockito.when;

public class SalesReportServletTest extends DBUnitConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PurchaseServletTest.class);
    private static final String MOCK_URL = "/salesreport";
    String path = new File("src/test/java/data/").getAbsolutePath();

    private Connection connection;
    protected IDatabaseTester tester;
    SalesReportServlet servlet;
    private StringWriter responseWriter;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private ServletContext context;

    public SalesReportServletTest(String name) throws Exception {
        super(name);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setUp();
        servlet = new SalesReportServlet();
        responseWriter = new StringWriter();
        tester = super.tester;
        connection = tester.getConnection().getConnection();
        Statement st = connection.createStatement();
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        beforeData = builder.build(new File(path + "/salesReportData.xml"));
        ReplacementDataSet rData = new ReplacementDataSet(beforeData);
        replaceDate(rData, "2019.11.11");
        replaceDate(rData, "2019.11.12");
        replaceDate(rData, "2019.11.13");
        replaceDate(rData, "2019.11.14");
        tester.setDataSet(rData);
        tester.onSetup();
        context.setAttribute("DBConnection", connection);
        when(mockRequest.getRequestURI()).thenReturn(MOCK_URL);
        when(mockResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
        when(mockRequest.getReader())
                .thenReturn(new BufferedReader(new FileReader(new File(path + "/salesReportData.json"))));
        when(mockRequest.getServletContext()).thenReturn(context);
        when(mockRequest.getServletContext().getAttribute("DBConnection")).thenReturn(connection);
    }

    @Test
    public void testDoPost() throws Exception {
        servlet.doPost(mockRequest, mockResponse);
        Assert.assertEquals("Замечательно, наша прибыль = 6000!!!" + System.getProperty("line.separator"),
                responseWriter.toString());
    }

    private void replaceDate(ReplacementDataSet rData, String dateString) throws ParseException {
        rData.addReplacementObject(dateString, new Date(new SimpleDateFormat("yyyy.MM.dd")
                .parse(dateString).getTime()));
    }
}
