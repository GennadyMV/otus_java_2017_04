package dbservice.impl;

import cache.CacheElement;
import cache.CacheEngine;
import cache.impl.CacheEngineImpl;
import dbservice.DBService;
import hibernate.UserDataSetDAO;
import model.AddressDataSet;
import model.PhoneDataSet;
import model.UserDataSet;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author sergey
 *         created on 10.06.17.
 */
public class DBServiceHibernate  implements DBService {

    private SessionFactory sessionFactory;
    private final CacheEngine<Long, UserDataSet> userCache;

    public static final int MAX_ELEMENTS = 300;
    public static final long LIFE_TIMES_M = 10;
    public static final long IDLE_TIME_M = 10;

    public DBServiceHibernate() {
        userCache = new CacheEngineImpl<>(MAX_ELEMENTS, TimeUnit.MINUTES.toMillis(LIFE_TIMES_M),
                TimeUnit.MINUTES.toMillis(IDLE_TIME_M),
                false);
    }

    @Override
    public void startup() throws SQLException {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://127.0.0.1:5432/testdb");
        configuration.setProperty("hibernate.connection.username", "userdb");
        configuration.setProperty("hibernate.connection.password", "userdb");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "validate");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        sessionFactory = createSessionFactory(configuration);
    }

    @Override
    public String getLocalStatus() {
        return runInSession(session -> session.getTransaction().getStatus().name());
    }

    @Override
    public void save(UserDataSet dataSet) {
        try (Session session = sessionFactory.openSession()) {
            new UserDataSetDAO(session).save(dataSet);
        }
    }

    @Override
    public UserDataSet read(long id) {
        CacheElement<UserDataSet> element = userCache.get(id);
        UserDataSet user = null;
        if (element != null) {
            user = element.getValue();
        }
        if (user == null) {
            user = readFromDb(id);
            userCache.put(id, new CacheElement<>(user));
        }
        return user;
    }

    public UserDataSet readFromDb(long id) {
        System.out.println("get from db, id:" + id);
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.read(id);
        });

    }

    public UserDataSet readByName(String name) {
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.readByName(name);
        });
    }

    public List<UserDataSet> readAll() {
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.readAll();
        });
    }


    @Override
    public void shutdown() {
        sessionFactory.close();
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public CacheEngine<Long, UserDataSet> getUserCache() {
        return userCache;
    }
}
