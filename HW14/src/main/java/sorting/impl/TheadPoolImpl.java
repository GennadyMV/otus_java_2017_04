package sorting.impl;

import sorting.ThreadPool;
import sorting.ThreadPoolTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sergey
 *         created on 15.07.17.
 */
public class TheadPoolImpl<R> implements ThreadPool<R> {
    private final int TASK_QUEUE_SIZE = 1000;
    private final int THREAD_POOL_SIZE;

    private final AtomicLong taskIdGenetator = new AtomicLong(0);
    private final BlockingQueue<ThreadPoolTask<R>> tasks = new ArrayBlockingQueue<>(TASK_QUEUE_SIZE);
    private final Map<Long, ThreadPoolTask<R>> tasksWithResults = new ConcurrentHashMap<>();

    private final List<Thread> threads = new ArrayList<>();

    public TheadPoolImpl(int poolSize) {
        THREAD_POOL_SIZE = poolSize;
        for (int idx = 0; idx < THREAD_POOL_SIZE; idx++) {
            threads.add(new Thread(() -> {
                while (true) {
                    try {
                    //    System.out.println("Thread:" + Thread.currentThread().getName() + " waiting for a task...");
                        ThreadPoolTask<R> task = tasks.take();
                 //       System.out.println("Thread:" + Thread.currentThread().getName() + " taken task, id:" + task.getTaskId());
                        task.execute();
                        synchronized (this) {
                            tasksWithResults.put(task.getTaskId(), task);
                            notifyAll();
                        }
                 //       System.out.println("Thread:" + Thread.currentThread().getName() + " DONE task, id:" + task.getTaskId());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
        threads.forEach(Thread::start);
    }

    @Override
    public void addTask(ThreadPoolTask<R> task) throws InterruptedException {
        final long taskId = taskIdGenetator.addAndGet(1);
        task.saveTaskId(taskId);
        tasks.put(task);
    }

    @Override
    public ThreadPoolTask<R> getTaskIfDone(long taskId) throws InterruptedException {
        ThreadPoolTask<R> result = null;
     //   System.out.println("Thread:" + Thread.currentThread().getName() + " waiting for result, TaskId:" + taskId);
        while(result == null) {
            if (tasksWithResults.containsKey(taskId)) {
                result = tasksWithResults.get(taskId);
                tasksWithResults.remove(taskId);
            } else {
                synchronized(this) {
                    wait();
                }
            }
        }
      //  System.out.println("Thread:" + Thread.currentThread().getName() + " result DONE, TaskId:" + taskId);
        return result;
    }

    public void printThreadState() {
        System.out.println("THREAD_POOL_SIZE:" + THREAD_POOL_SIZE);
        threads.forEach(th -> {
            System.out.println((th.getId() + " " + th.getName() + " " + th.getState()));
        });
    }
}
