package server;

import dbservice.DbServiceMessageSupport;
import messageSystem.MessageResponse;

/**
 * @author sergey
 * created on 30.07.17.
 */
public interface MsgChannel {
    void replay(MessageResponse response);

    DbServiceMessageSupport pool();

    void close();

    boolean isClosed();
}
