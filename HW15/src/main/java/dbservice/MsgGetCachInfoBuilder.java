package dbservice;

import messageSystem.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sergey
 * created on 23.07.17.
 */

@Component
public class MsgGetCachInfoBuilder {

    @Autowired
    private UserCacheImpl cacheEngine;


    public MsgGetCachInfo make(Address from) {
        return new MsgGetCachInfo(cacheEngine, from);
    }
}
