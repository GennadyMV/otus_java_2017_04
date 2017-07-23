package dbservice;

import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageResponse;

/**
 * @author sergey
 * created on 23.07.17.
 */


public class MsgGetCachInfo extends Message {

    private UserCacheImpl cacheEngine;

    public MsgGetCachInfo(UserCacheImpl cacheEngine, Address from) {
        this.cacheEngine = cacheEngine;
        super.setTo(cacheEngine.getAddress());
        super.setFrom(from);
    }

    @Override
    public MessageResponse exec() {
        return new MessageResponse<>(cacheEngine.getCachInfo());
    }
}
