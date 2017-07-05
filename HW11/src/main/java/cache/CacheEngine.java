package cache;

/**
 * @author sergey
 *         created on 04.07.17.
 */
public interface CacheEngine<K, V> {

    void put(K key, CacheElement<V> cacheElement);

    CacheElement<V> get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();
}