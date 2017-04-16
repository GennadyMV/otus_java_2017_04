package ru.otus.mytypes;

import org.junit.Test;

import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.*;

/**
 * Created by sergey on 15.04.17.
 */
public class MyArrayListTest {
    @Test
    public void listIterator() throws Exception {
        List<String> myArrayList = new MyArrayList<>(2);
        final String val1 = "val1";
        final String val2 = "val2";
        final String val3 = "val3";

        myArrayList.add(val1);
        myArrayList.add(val2);
        myArrayList.add(val3);
        ListIterator<String> iterator = myArrayList.listIterator();
        assertTrue("HasNext", iterator.hasNext());
        assertTrue("next_val1", val1.equals(iterator.next()));
        assertTrue("next_val2", val2.equals(iterator.next()));
        assertTrue("next_val3", val3.equals(iterator.next()));
        assertFalse("HasNext", iterator.hasNext());

    }

    @Test
    public void listIterator1() throws Exception {
    }

    @Test
    public void add() throws Exception {
        List<String> myArrayList = new MyArrayList<>(2);

        assertEquals("size_0", 0, myArrayList.size() );

        boolean result = myArrayList.add("test1");
        assertTrue("changed", result);
        assertEquals("size_1", 1, myArrayList.size() );

        result = myArrayList.add("test2");
        assertTrue("changed", result);
        assertEquals("size_2", 2, myArrayList.size() );

        result = myArrayList.add("test3");
        assertTrue("changed", result);
        assertEquals("size_3", 3, myArrayList.size() );

        result = myArrayList.add("test4");
        assertTrue("changed", result);
        assertEquals("size_4", 4, myArrayList.size() );

        result = myArrayList.add("test5");
        assertTrue("changed", result);
        assertEquals("size_5", 5, myArrayList.size() );
    }

    @Test
    public void size() throws Exception {
        MyArrayList<String> myArrayList = new MyArrayList<>();
        assertEquals("size_0", 0, myArrayList.size() );

        myArrayList.add("test1");
        assertEquals("size_1", 1, myArrayList.size() );
    }

    @Test
    public void isEmpty() throws Exception {
        MyArrayList<String> myArrayList = new MyArrayList<>();
        assertTrue("isEmpty_true", myArrayList.isEmpty());

        myArrayList.add("test1");
        assertFalse("isEmpty_false", myArrayList.isEmpty());
    }

}