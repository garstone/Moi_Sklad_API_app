import com.kamenev.service.SalesReportService;
import config.DBUnitConfig;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SalesReportServiceTest extends DBUnitConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServiceTest.class);
    private Connection connection;
    private SalesReportService salesReportService;
    String path = new File("src/test/java/data/").getAbsolutePath();

    public SalesReportServiceTest(String name) throws Exception {
        super(name);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        String filePath = new File(path + "/salesReportData.xml").getAbsolutePath();
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        beforeData = builder.build(new File(filePath));
        ReplacementDataSet rData = new ReplacementDataSet(beforeData);
        replaceDate(rData, "2019.11.11");
        replaceDate(rData, "2019.11.12");
        replaceDate(rData, "2019.11.13");
        replaceDate(rData, "2019.11.14");
        tester.setDataSet(rData);
        tester.onSetup();
        connection = tester.getConnection().getConnection();
        Statement st = connection.createStatement();
        st.execute("ALTER SEQUENCE products_id_seq RESTART WITH 1");
        st.execute("ALTER SEQUENCE salesreport_id_seq RESTART WITH 5");
        salesReportService = new SalesReportService(connection);
    }

    public void testAdd() throws Exception {
        salesReportService.add("samsung", 10000, "2019.11.15");
        IDataSet expectedData = new FlatXmlDataSetBuilder().build(new File(path + "/salesReportData-add.xml"));
        ReplacementDataSet rExpected = new ReplacementDataSet(expectedData);
        replaceDate(rExpected, "2019.11.11");
        replaceDate(rExpected, "2019.11.12");
        replaceDate(rExpected, "2019.11.13");
        replaceDate(rExpected, "2019.11.14");
        replaceDate(rExpected, "2019.11.15");
        ITable expectedTable = rExpected.getTable("salesreport");
        IDataSet actualData = tester.getConnection().createDataSet();
        ITable actualTable = actualData.getTable("salesreport");
        Assertion.assertEquals(expectedTable, actualTable);
    }

    public void testGetIncome() throws Exception {
        Assert.assertEquals(6000, salesReportService.getIncome("iphone", "2019.11.12"));
        Assert.assertEquals(0, salesReportService.getIncome("iphone", "2019.11.10"));
        Assert.assertEquals(8000, salesReportService.getIncome("iphone", "2019.11.15"));
        Assert.assertEquals(-1, salesReportService.getIncome("google", "2019.11.12"));
        Assert.assertEquals(3000, salesReportService.getIncome("samsung", "2019.11.14"));
    }

    private void replaceDate(ReplacementDataSet rData, String dateString) throws ParseException {
        rData.addReplacementObject(dateString, new Date(new SimpleDateFormat("yyyy.MM.dd")
                .parse(dateString).getTime()));
    }
}
