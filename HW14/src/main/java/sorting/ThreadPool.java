package sorting;

/**
 * @author sergey
 *         created on 15.07.17.
 */
public interface ThreadPool<R>{

    void addTask(ThreadPoolTask<R> task) throws InterruptedException;

    ThreadPoolTask<R> getTaskIfDone(long taskId) throws InterruptedException;
}
