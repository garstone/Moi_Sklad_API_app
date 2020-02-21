import com.kamenev.service.ProductService;
import config.DBUnitConfig;
import org.dbunit.Assertion;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Set;

public class ProductServiceTest extends DBUnitConfig{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceTest.class);
    private Connection connection;
    private ProductService productService;
    String path = new File("src/test/java/data/").getAbsolutePath();

    public ProductServiceTest(String name) throws Exception {
        super(name);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        String filePath = new File(path + "/productData.xml").getAbsolutePath();
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        beforeData = builder.build(new File(filePath));
        tester.setDataSet(beforeData);
        tester.onSetup();
        connection = tester.getConnection().getConnection();
        Statement st = connection.createStatement();
        st.execute("ALTER SEQUENCE products_id_seq RESTART WITH 4");
        productService = new ProductService(connection);
    }


    @Test
    public void testInsert() throws Exception {
        Assert.assertEquals(1, productService.insert("nokia"));
        String filePath = new File(path + "/productData-insert.xml").getAbsolutePath();
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet expectedData = builder.build(new File(filePath));
        ITable expectedTable = expectedData.getTable("products");
        IDataSet actualData = tester.getConnection().createDataSet();
        ITable actualTable = actualData.getTable("products");
        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    public void testFindAll() throws Exception {
        Set<String> products = productService.findAll();
        ITable expectedTable = beforeData.getTable("products");
        IDataSet actualData = tester.getConnection().createDataSet();
        ITable actualTable = actualData.getTable("products");
        Assertion.assertEquals(expectedTable, actualTable);
        Assert.assertEquals(expectedTable.getRowCount(), products.size());
    }

    @Test
    public void testFind() throws Exception {
        boolean isIphoneInDB = productService.find("iphone");
        Assert.assertEquals(true, isIphoneInDB);
        boolean isXiaomiInDB = productService.find("xiaomi");
        Assert.assertEquals(false, isXiaomiInDB);
    }
}
