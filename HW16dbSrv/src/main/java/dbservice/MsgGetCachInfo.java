package dbservice;

import messageSystem.MessageResponse;

/**
 * @author sergey
 * created on 23.07.17.
 */
public class MsgGetCachInfo extends DbServiceMessageSupport {

    public MsgGetCachInfo() {
        super(MsgGetCachInfo.class);
    }

    @Override
    public MessageResponse exec() {
        return new MessageResponse<>(getCacheEngine().getCachInfo());
    }
}
