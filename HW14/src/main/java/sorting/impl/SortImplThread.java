package sorting.impl;

import sorting.Sort;
import sorting.ThreadPool;
import sorting.ThreadPoolTask;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author sergey
 *         created on 15.07.17.
 */
public class SortImplThread extends SortImplSingle implements Sort {
    private final ThreadPool<SortJob> threadPool;
    private final int LAST_TASK_ID = 9;
    private final AtomicBoolean createTaskFlag = new AtomicBoolean(true);

    public SortImplThread(int threadNumber) {
        threadPool = new TheadPoolImpl<>(threadNumber);
    }


    private ThreadPoolTask<SortJob> makeTaskForPool(SortJob sortJob) {
        return new ThreadPoolTask<SortJob>() {
            private long taskId;
            private SortJob result;

            @Override
            public void saveTaskId(long taskId) {
                this.taskId = taskId;
            }

            @Override
            public long getTaskId() {
                return taskId;
            }

            @Override
            public void execute() {
                if (sortJob.getLeftArr().length > 1) {
                    SortJob sortJobL = divide(sortJob.getLeftArr());
                    sort(sortJobL);
                    sortJob.setLeftArr(sortJobL.getResult());

                }
                if (sortJob.getRightArr().length > 1) {
                    SortJob sortJobR = divide(sortJob.getRightArr());
                    sort(sortJobR);
                    sortJob.setRightArr(sortJobR.getResult());
                }
                result = sortJob;
            }

            @Override
            public SortJob getResult() {
                return result;
            }
        };
    }

    @Override
    protected void sort(SortJob sortJob) {
        try {
            ThreadPoolTask<SortJob> task = makeTaskForPool(sortJob);

            if (createTaskFlag.get()) {
                threadPool.addTask(task);
                if (LAST_TASK_ID >= task.getTaskId()) {
                    createTaskFlag.set(false);
                    System.out.println("Do not create new task any more");
                }
                sortJob = threadPool.getTaskIfDone(task.getTaskId()).getResult();
            } else {
                task.execute();
                sortJob = task.getResult();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        merge(sortJob);
    }
}
