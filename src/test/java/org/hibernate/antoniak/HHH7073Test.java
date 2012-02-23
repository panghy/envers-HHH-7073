package org.hibernate.antoniak;

import org.hibernate.cfg.Environment;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.EntityManagerFactoryImpl;
import org.hibernate.service.BootstrapServiceRegistryBuilder;
import org.junit.*;

import javax.persistence.EntityManager;
import java.util.Properties;

/**
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class HHH7073Test {
  static private Ejb3Configuration cfg = null;
  static private EntityManagerFactoryImpl emf = null;
  private EntityManager em = null;

  @BeforeClass
  public static void init() {
    cfg = new Ejb3Configuration();
    cfg.configure(getConnectionProviderProperties());
    configure(cfg);
    emf = (EntityManagerFactoryImpl) cfg.buildEntityManagerFactory(new BootstrapServiceRegistryBuilder());
  }

  @AfterClass
  public static void destroy() {
    emf.close();
  }

  @Before
  public void setUp() {
    em = emf.createEntityManager();
  }

  @After
  public void tearDown() {
    em.close();
  }

  @Test
  public void testAddressBook() {
    em.getTransaction().begin();
    // create two people
    Person me = new Person();
    Person myFriend = new Person();
    em.persist(me);
    em.persist(myFriend);
    // create my address.
    Address myaddress = new Address();
    me.addAddress(myaddress);
    em.persist(myaddress);
    // my friend adds my address to his address book.
    myFriend.addNewAddressBookEntry(myaddress);
    em.getTransaction().commit();
  }

  private static void configure(Ejb3Configuration cfg) {
    cfg.addAnnotatedClass(Address.class);
    cfg.addAnnotatedClass(Person.class);
  }

  public static final String DRIVER = "org.h2.Driver";
  public static final String URL = "jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;MVCC=TRUE";
  public static final String USER = "sa";
  public static final String PASS = "";

  public static Properties getConnectionProviderProperties(String dbName) {
    Properties props = new Properties(null);
    props.put(Environment.DRIVER, DRIVER);
    props.put(Environment.URL, String.format(URL, dbName));
    props.put(Environment.USER, USER);
    props.put(Environment.PASS, PASS);
    props.put(Environment.SHOW_SQL, "true");
    props.put(Environment.FORMAT_SQL, "true");
    props.put(Environment.HBM2DDL_AUTO, "create-drop");
    return props;
  }

  public static Properties getConnectionProviderProperties() {
    return getConnectionProviderProperties("db1");
  }
}