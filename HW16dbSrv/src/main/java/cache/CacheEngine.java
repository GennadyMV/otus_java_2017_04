package cache;

/**
 * @author sergey
 *         created on 04.07.17.
 */
public interface CacheEngine<K, V> {

    void put(K key, CacheElement<V> casheElement);

    CacheElement<V> get(K key);

    CachInfo getCachInfo();

    void dispose();
}