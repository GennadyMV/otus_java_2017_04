package ru.otus;

import ru.otus.mytypes.MyArrayList;

import java.util.Collections;
import java.util.List;

/**
 * Created by sergey on 15.04.17.
 */
public class myTypesUsage {

    public static void main(String[] args) {
        final int arrSize = 10_000;

        List<String> myArrayList = new MyArrayList<>();
        myArrayList.add("val1");
        myArrayList.add("val2");
        myArrayList.add("val3");
        System.out.println(myArrayList.toString());
//addAll
        Collections.addAll(myArrayList,"add1", "add2", "add3");
        System.out.println(myArrayList.toString());

        List<String> myArrayListSrc = new MyArrayList<>();
        for(int i = 0; i < arrSize; i++) {
            myArrayListSrc.add("src" + i);
        }

        List<String> myArrayListDest = new MyArrayList<>(myArrayListSrc.size() + 1);
        for(int i = 0; i < arrSize; i++) {
            myArrayListDest.add("dest" + i);
        }
        myArrayListDest.add("dest_last");
//copy
        Collections.copy(myArrayListDest, myArrayListSrc);
        System.out.println(myArrayListDest);
        System.out.println("changed:" + myArrayListDest.get(0));
        System.out.println("not changed:" + myArrayListDest.get(myArrayListDest.size() - 1));

        List<Integer> myArrayListSort = new MyArrayList<>();
        for(int i = 0; i < arrSize; i++) {
            myArrayListSort.add(((Double) (Math.random()*1000)).intValue());
        }
        System.out.println("random:" + myArrayListSort);
//sort
        Collections.sort(myArrayListSort, Integer::compareTo);
        System.out.println("sorted:" + myArrayListSort);
    }



}
