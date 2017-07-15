package sorting;

import org.junit.Test;
import sorting.impl.SortImplSingle;
import sorting.impl.SortImplThread;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * @author sergey
 *         created on 15.07.17.
 */
public class SortImplTest {

    @Test
    public void sortArrayInt2() throws Exception {
        final int[] initArray = {86, 45};
        final int[] okArray = Arrays.copyOf(initArray, initArray.length);
        Arrays.sort(okArray);

        final int[] sortedArray = new SortImplSingle().sortArrayInt(initArray);

        System.out.println("initArray:" + Arrays.toString(initArray));
        System.out.println("sortedArray:" + Arrays.toString(sortedArray));
        assertTrue("sorted array", Arrays.equals(sortedArray, okArray));
    }

    @Test
    public void sortArrayInt3() throws Exception {
        final int[] initArray = {86, 45, 4};
        final int[] okArray = Arrays.copyOf(initArray, initArray.length);
        Arrays.sort(okArray);

        final int[] sortedArray = new SortImplSingle().sortArrayInt(initArray);

        System.out.println("initArray:" + Arrays.toString(initArray));
        System.out.println("sortedArray:" + Arrays.toString(sortedArray));
        assertTrue("sorted array", Arrays.equals(sortedArray, okArray));
    }

    @Test
    public void sortArrayInt7() throws Exception {
        final int[] initArray = {86, 45, 4, 5, 77, 5, 7};
        final int[] okArray = Arrays.copyOf(initArray, initArray.length);
        Arrays.sort(okArray);

        final int[] sortedArray = new SortImplSingle().sortArrayInt(initArray);

        System.out.println("initArray:" + Arrays.toString(initArray));
        System.out.println("sortedArray:" + Arrays.toString(sortedArray));
        assertTrue("sorted array", Arrays.equals(sortedArray, okArray));
    }

    @Test
    public void sortArrayInt7Thread() throws Exception {
        final int[] initArray = {86, 45, 4, 5, 77, 5, 7};
        final int[] okArray = Arrays.copyOf(initArray, initArray.length);
        Arrays.sort(okArray);

        final int[] sortedArray = new SortImplThread(4).sortArrayInt(initArray);

        System.out.println("initArray:" + Arrays.toString(initArray));
        System.out.println("sortedArray:" + Arrays.toString(sortedArray));
        assertTrue("sorted array", Arrays.equals(sortedArray, okArray));
    }

    @Test
    public void longTest() throws Exception {
        final int ARRAY_SIZE = 101;
        final int[] initArray = new int[ARRAY_SIZE];

        Random random = new Random();
        for(int idx = 0; idx < ARRAY_SIZE; idx++) {
            initArray[idx++] = random.nextInt(100);
        }

        final int[] okArray = Arrays.copyOf(initArray, initArray.length);
        Arrays.sort(okArray);

        final int[] sortedArray = new SortImplSingle().sortArrayInt(initArray);

        System.out.println("initArray:" + Arrays.toString(initArray));
        System.out.println("sortedArray:" + Arrays.toString(sortedArray));
        assertTrue("sorted array", Arrays.equals(sortedArray, okArray));
    }

    //difference between 4 and 7
    @Test
    public void stressTest() throws Exception {
        final int ARRAY_SIZE = 1_000_000;
        final int[] initArray = new int[ARRAY_SIZE];

        Random random = new Random();
        for(int idx = 0; idx < ARRAY_SIZE; idx++) {
            initArray[idx++] = random.nextInt(100);
        }

        final int[] okArray = Arrays.copyOf(initArray, initArray.length);
        long beginArrays = System.currentTimeMillis();
        Arrays.sort(okArray);
        long endArrays = System.currentTimeMillis();
        System.out.println("Arrays:" + (endArrays - beginArrays)*1000);

        long beginMy = System.currentTimeMillis();
        final int[] sortedArray = new SortImplSingle().sortArrayInt(initArray);
        long endMy = System.currentTimeMillis();
        System.out.println("My:" + (endMy - beginMy)*1000);
        System.out.println("difference:" + (endMy - beginMy) / (endArrays - beginArrays));

        assertTrue("sorted array", Arrays.equals(sortedArray, okArray));
    }

    //difference between 3 and 5
    @Test
    public void stressTestThread() throws Exception {
        final int ARRAY_SIZE = 1_000_000;
        final int[] initArray = new int[ARRAY_SIZE];

        Random random = new Random();
        for(int idx = 0; idx < ARRAY_SIZE; idx++) {
            initArray[idx++] = random.nextInt(100);
        }

        final int[] okArray = Arrays.copyOf(initArray, initArray.length);
        long beginArrays = System.currentTimeMillis();
        Arrays.sort(okArray);
        long endArrays = System.currentTimeMillis();
        System.out.println("Arrays:" + (endArrays - beginArrays)*1000);

        final Sort sort = new SortImplThread(10);
        long beginMy = System.currentTimeMillis();
        final int[] sortedArray = sort.sortArrayInt(initArray);
        long endMy = System.currentTimeMillis();
        System.out.println("My:" + (endMy - beginMy)*1000);
        System.out.println("difference:" + (endMy - beginMy) / (endArrays - beginArrays));

        assertTrue("sorted array", Arrays.equals(sortedArray, okArray));
    }

}