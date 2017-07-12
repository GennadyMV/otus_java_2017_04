package dbservice.impl;

import cache.CacheElement;
import cache.CacheEngine;
import cache.impl.CacheEngineImpl;
import dbservice.UserCache;
import model.UserDataSet;

import java.util.concurrent.TimeUnit;

/**
 * @author sergey
 *         created on 12.07.17.
 */
public class UserCacheImpl implements UserCache {

    public static final int MAX_ELEMENTS = 300;
    public static final long LIFE_TIMES_M = 10;
    public static final long IDLE_TIME_M = 10;

    private CacheEngine<Long, UserDataSet> userCache;

    public UserCacheImpl() {
        userCache = new CacheEngineImpl<>(MAX_ELEMENTS,
                TimeUnit.MINUTES.toMillis(LIFE_TIMES_M),
                TimeUnit.MINUTES.toMillis(IDLE_TIME_M),
                false);
    }

    @Override
    public int getHitCount() {
        return userCache.getHitCount();
    }

    @Override
    public int getMissCount() {
        return userCache.getMissCount();
    }

    @Override
    public void put(long key, CacheElement<UserDataSet> cacheElement) {
        userCache.put(key, cacheElement);
    }

    @Override
    public CacheElement<UserDataSet> get(long key) {
        return userCache.get(key);
    }


}
