package msgservice;

import java.io.IOException;

/**
 * @author sergey
 * created on 30.07.17.
 */
public interface ProcessRunner {
    void start(String command, String args) throws IOException;
}
