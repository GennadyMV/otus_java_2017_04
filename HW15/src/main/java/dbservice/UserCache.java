package dbservice;

import cache.CachInfo;
import cache.CacheElement;
import dbservice.model.UserDataSet;

/**
 * @author sergey
 *         created on 12.07.17.
 */
public interface UserCache {
    CachInfo getCachInfo();

    void put(long key, CacheElement<UserDataSet> casheElement);

    CacheElement<UserDataSet> get(long key);
}
