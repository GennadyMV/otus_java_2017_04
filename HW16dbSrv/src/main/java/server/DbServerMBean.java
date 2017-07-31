package server;

/**
 * @author sergey
 * created on 30.07.17.
 */
public interface DbServerMBean {
    boolean getRunning();

    void setRunning(boolean running);
}
