package dbservice;

import cache.CacheElement;
import model.UserDataSet;

/**
 * @author sergey
 *         created on 12.07.17.
 */
public interface UserCache {
    int getHitCount();
    int getMissCount();

    void put(long key, CacheElement<UserDataSet> cacheElement);

    CacheElement<UserDataSet> get(long key);
}
