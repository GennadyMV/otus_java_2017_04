package cache.impl;

import cache.CacheEngine;
import cache.CacheElement;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

/**
 * @author sergey
 *         created on 04.07.17.
 */
public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {
    private static final int TIME_THRESHOLD_MS = 5;

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, SoftReference<CacheElement<V>>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    public CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    @Override
    public void put(K key, CacheElement<V> cacheElement) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
            System.out.println("remove element, key:" + firstKey);
        }

        elements.put(key, new SoftReference<>(cacheElement));

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeCacheElement -> lifeCacheElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleCacheElement -> idleCacheElement.getCreationTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs);
            }
        }
    }

    private CacheElement<V> getElementFromReference(K key) {
        SoftReference<CacheElement<V>> reference = elements.get(key);
        CacheElement<V> cacheElement = null;
        if (reference != null) {
            cacheElement = reference.get();
        }
        return cacheElement;
    }

    @Override
    public CacheElement<V> get(K key) {
        CacheElement<V> cacheElement = getElementFromReference(key);
        if (cacheElement != null) {
            hit++;
            cacheElement.setAccessed();
        } else {
            miss++;
        }
        return cacheElement;
    }

    public int getHitCount() {
        return hit;
    }

    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private TimerTask getTimerTask(final K key, Function<CacheElement<V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                CacheElement<V> checkedCacheElement = getElementFromReference(key);
                if (checkedCacheElement == null ||
                        isT1BeforeT2(timeFunction.apply(checkedCacheElement), System.currentTimeMillis())) {
                    elements.remove(key);
                    System.out.println("timer remove, key:" + key);
                }
            }
        };
    }


    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
