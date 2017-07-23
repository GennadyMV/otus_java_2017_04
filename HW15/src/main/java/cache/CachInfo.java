package cache;

/**
 * @author sergey
 * created on 23.07.17.
 */
public class CachInfo {
    private int hitCount;
    private int missCount;

    public CachInfo() {
    }

    public CachInfo(int hitCount, int missCount) {
        this.hitCount = hitCount;
        this.missCount = missCount;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public int getMissCount() {
        return missCount;
    }

    public void setMissCount(int missCount) {
        this.missCount = missCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CachInfo cachInfo = (CachInfo) o;

        if (hitCount != cachInfo.hitCount) return false;
        return missCount == cachInfo.missCount;
    }

    @Override
    public int hashCode() {
        int result = hitCount;
        result = 31 * result + missCount;
        return result;
    }

    @Override
    public String toString() {
        return "CachInfo{" +
                "hitCount=" + hitCount +
                ", missCount=" + missCount +
                '}';
    }
}
