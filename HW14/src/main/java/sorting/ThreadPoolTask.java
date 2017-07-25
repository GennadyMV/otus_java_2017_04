package sorting;

/**
 * @author sergey
 *         created on 15.07.17.
 */
public interface ThreadPoolTask<R> {
    long getTaskId();

    void execute();

    R getResult();
}
