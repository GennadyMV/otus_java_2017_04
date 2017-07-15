package sorting.impl;

import sorting.Sort;

import java.util.Arrays;

/**
 * @author sergey
 *         created on 15.07.17.
 */
public class SortImplSingle implements Sort {

    public SortImplSingle() {
    }

    @Override
    public int[] sortArrayInt(int[] array) {
        if (array.length < 2) {
            return Arrays.copyOf(array, array.length);
        }
        final SortJob sortJob = divide(array);
        sort(sortJob);
        return sortJob.getResult();
    }

    protected void sort(SortJob sortJob) {
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
        merge(sortJob);
    }


    protected void merge(SortJob sortJob) {
        final int[] result = new int[sortJob.getLeftArr().length + sortJob.getRightArr().length];
        int point1 = 0;
        int point2 = 0;
        int pointMerged = 0;
        for(int idx = 0; idx < result.length &&
                point1 < sortJob.getLeftArr().length &&
                point2 < sortJob.getRightArr().length; idx++) {
            if (sortJob.getLeftArr()[point1] < sortJob.getRightArr()[point2]) {
                result[pointMerged++] = sortJob.getLeftArr()[point1++];
            } else {
                result[pointMerged++] = sortJob.getRightArr()[point2++];
            }
        }
        for(int idx = point1; idx < sortJob.getLeftArr().length; idx++) {
            result[pointMerged++] = sortJob.getLeftArr()[idx];
        }
        for(int idx = point2; idx < sortJob.getRightArr().length; idx++) {
            result[pointMerged++] = sortJob.getRightArr()[idx];
        }

        sortJob.setResult(result);
    }

    protected SortJob divide(int[] array) {
        final int middle = array.length / 2;
        return new SortJob(Arrays.copyOfRange(array, 0, middle), Arrays.copyOfRange(array, middle, array.length));
    }
}
