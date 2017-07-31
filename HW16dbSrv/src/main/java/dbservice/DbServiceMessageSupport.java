package dbservice;

import messageSystem.Message;

/**
 * @author sergey
 * created on 30.07.17.
 */
public abstract class DbServiceMessageSupport extends Message {

    private UserCacheMessageSupport cacheEngine;

    protected DbServiceMessageSupport(Class<?> klass) {
        super(klass);
    }

    public void setCacheEngine(UserCacheMessageSupport cacheEngine) {
        this.cacheEngine = cacheEngine;
    }

    protected UserCacheMessageSupport getCacheEngine() {
        return cacheEngine;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
