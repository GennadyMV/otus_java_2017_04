package sorting;

/**
 * @author sergey
 *         created on 15.07.17.
 */
public interface ThreadPoolTask<R> {

    void saveTaskId(long taskId);
    long getTaskId();

    void execute();
    R getResult();
}
