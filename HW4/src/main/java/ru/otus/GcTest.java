package ru.otus;


import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
http://www.fasterj.com/articles/oraclecollectors1.shtml
			                               Minor                   Major
			                            Count| Duration       	Count|Duration
2 Copy (UseSerialGC)                       5 | 269                 - | -
4 PS Scavenge (UseParallelGC)              4 | 177                 - | -
1 ParNew (UseParNewGC)                    40 | 319                 - | -
3 G1 Young Generation (UseG1GC)           16 | 400                 - | -

2 MarkSweepCompact (UseSerialGC)           - | -                   1 | 114
4 PS MarkSweep (UseParallelOldGC)          - | -                   6 | 632
1 ConcurrentMarkSweep (UseConcMarkSweepGC) - | -                  36 | 21175
3 G1 Mixed Generation (UseG1GC)            - | -                   1 | 271 (*)

  (*) Was executed just after 5min, before OutOfMemory.
*/

public class GcTest {
    private static final int size = 500 * 1024;
    private static final AtomicInteger minCounter = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println(new Date() + " Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        final MonitoringGc monitoringGc = new MonitoringGc();;
        startTimerTask(monitoringGc);
        memoryConsume();
        System.out.println("end");
    }

    private static void startTimerTask(final MonitoringGc monitoringGc) {
        new Thread(() -> {
            while (true) {
                sleep(TimeUnit.MINUTES.toMillis(1));
                minCounter.addAndGet(1);
                Statistics statistics = monitoringGc.getStatistics();
                System.out.println("minCounter:" + minCounter.get() + " " + new Date());
                statistics.getCountMinor().forEach((key, val) -> {
                    System.out.println(key + " Minor count:" + val
                            + " Duration (ms)" + statistics.getDurationMinor().get(key) );
                });
                statistics.getCountMajor().forEach((key, val) -> {
                    System.out.println(key + " Major count:" + val
                            + " Duration (ms)" + statistics.getDurationMajor().get(key) );
                });
            }
        }).start();
    }

    private static void memoryConsume() {
        List<byte[]> links = new ArrayList<>();
        int idx = 0;
        while (true) {
            links.add(new byte[size + idx * 2]);
            if (idx % (2 + minCounter.get()) == 0) {
                for (int remIdx = links.size() - 1; remIdx > 0 && idx - remIdx < 500; remIdx--) {
                    links.remove(remIdx);
                }
            }
            sleep(200);
            idx++;
        }
    }

    private static void sleep(long msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
