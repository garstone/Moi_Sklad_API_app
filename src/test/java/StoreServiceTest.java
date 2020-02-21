import com.kamenev.service.StoreService;
import config.DBUnitConfig;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StoreServiceTest extends DBUnitConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServiceTest.class);
    private Connection connection;
    private StoreService storeService;
    String path = new File("src/test/java/data/").getAbsolutePath();

    @Temporal(TemporalType.DATE)
    private Date date;

    public StoreServiceTest(String name) throws Exception {
        super(name);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        connection = tester.getConnection().getConnection();
        Statement st = connection.createStatement();
        st.execute("ALTER SEQUENCE products_id_seq RESTART WITH 1");
        st.execute("ALTER SEQUENCE store_id_seq RESTART WITH 4");
        String filePath = new File(path + "/storeData.xml").getAbsolutePath();
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        beforeData = builder.build(new File(filePath));
        ReplacementDataSet rData = new ReplacementDataSet(beforeData);
        replaceDate(rData, "2019.10.10");
        replaceDate(rData, "2019.10.11");
        replaceDate(rData, "2019.10.12");
        tester.setDataSet(rData);
        tester.onSetup();
        storeService = new StoreService(connection);
    }

    @Test
    public void testAdd() throws Exception {
        storeService.add("samsung", 10, 50, "2019.10.13", "2019.10.13");
        IDataSet expectedData = new FlatXmlDataSetBuilder().build(new File(path + "/storeData-save.xml"));
        ReplacementDataSet rExpected = new ReplacementDataSet(expectedData);
        replaceDate(rExpected, "2019.10.10");
        replaceDate(rExpected, "2019.10.11");
        replaceDate(rExpected, "2019.10.12");
        replaceDate(rExpected, "2019.10.13");
        ITable expectedTable = rExpected.getTable("store");
        IDataSet actualData = tester.getConnection().createDataSet();
        ITable actualTable = actualData.getTable("store");
        Assertion.assertEquals(expectedTable, actualTable);
        Assert.assertEquals(1, storeService.add("samsung", 10, 50, "2019.10.13", "2019.10.13"));
    }

    @Test
    public void testPop() throws Exception {
        assertEquals(500, storeService.pop("iphone", 5));
        assertEquals(4500, storeService.pop("iphone", 25));
        assertEquals(-1, storeService.pop("iphone", 100));
    }

    @Test
    public void testSale() throws Exception {
        assertEquals(500, storeService.sale("iphone", 5, 200, "2019.10.13"));
        assertEquals(1500, storeService.sale("iphone", 10, 300, "2019.10.14"));
        assertEquals(-1, storeService.sale("iphone", 1000, 200, "2019.10.13"));
        assertEquals(-1, storeService.sale("oneplus", 5, 10, "2019.10.13"));
        assertEquals(-1, storeService.sale("google", 5, 10, "2019.10.13"));
    }

    private void replaceDate(ReplacementDataSet rData, String dateString) throws ParseException {
        rData.addReplacementObject(dateString, new Date(new SimpleDateFormat("yyyy.MM.dd")
                .parse(dateString).getTime()));
    }
}
