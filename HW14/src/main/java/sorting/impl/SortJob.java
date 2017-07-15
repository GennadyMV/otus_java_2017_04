package sorting.impl;

/**
 * @author sergey
 *         created on 15.07.17.
 */
class SortJob {
    private int[] leftArr;
    private int[] rightArr;
    private int[] result;

    SortJob(int[] leftArr, int[] rightArr) {
        this.leftArr = leftArr;
        this.rightArr = rightArr;
    }

    void setResult(int[] result) {
        this.result = result;
    }

    int[] getResult() {
        return result;
    }

    int[] getLeftArr() {
        return leftArr;
    }

    int[] getRightArr() {
        return rightArr;
    }

    void setLeftArr(int[] leftArr) {
        this.leftArr = leftArr;
    }

    void setRightArr(int[] rightArr) {
        this.rightArr = rightArr;
    }
}
