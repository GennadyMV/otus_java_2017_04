package dbservice;

import cache.CachInfo;
import cache.CacheElement;
import cache.CacheEngine;
import cache.CacheEngineImpl;
import dbservice.model.UserDataSet;
import messageSystem.Address;

import java.util.concurrent.TimeUnit;

/**
 * @author sergey
 *         created on 12.07.17.
 */

public class UserCacheImpl implements UserCacheMessageSupport {
    private static final String name = "CacheEngine";

    public static final int MAX_ELEMENTS = 300;
    public static final long LIFE_TIMES_M = 10;
    public static final long IDLE_TIME_M = 10;

    private CacheEngine<Long, UserDataSet> userCache;
    private Address address;

    public UserCacheImpl() {
        userCache = new CacheEngineImpl<>(MAX_ELEMENTS,
                TimeUnit.MINUTES.toMillis(LIFE_TIMES_M),
                TimeUnit.MINUTES.toMillis(IDLE_TIME_M),
                false);

        address = new Address(name);
    }

    @Override
    public CachInfo getCachInfo() {
        return userCache.getCachInfo();
    }

    @Override
    public void put(long key, CacheElement<UserDataSet> casheElement) {
        userCache.put(key, casheElement);
    }

    @Override
    public CacheElement<UserDataSet> get(long key) {
        return userCache.get(key);
    }

    @Override
    public Address getAddress() {
        return address;
    }

}
