package ru.otus.mytypes;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sergey on 15.04.17.
 */
public class MyArrayList<T> implements List<T> {
    private static final int INITIAL_SIZE = 10;

    @SuppressWarnings("unchecked")
    private T[] data = (T[]) new Object[INITIAL_SIZE];
    private int pointerFreeSpace = 0;

    public MyArrayList() {

    }

    @SuppressWarnings("unchecked")
    public MyArrayList(int initSize) {
        data = (T[]) new Object[initSize];
    }

    @Override
    public int size() {
        return pointerFreeSpace;
    }

    @Override
    public boolean isEmpty() {
        return pointerFreeSpace == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        if (pointerFreeSpace == data.length) {
            extend();
        }
        data[pointerFreeSpace++] = t;
        return true;
    }

    @SuppressWarnings("unchecked")
    private void extend() {
        data = Arrays.copyOf(data, data.length *2);
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public T get(int index) {
        return data[index];
    }

    @Override
    public T set(int index, T element) {
        T prev = data[index];
        data[index] = element;
        return prev;
    }

    @Override
    public void add(int index, T element) {
        data[index] = element;
    }

    @Override
    public T remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIter(-1);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListIter(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public String toString() {
        String out = Arrays.stream(data).limit(pointerFreeSpace).map(Object::toString).collect(Collectors.joining(", "));
        return "MyArrayList{[" + out + "]}";
    }

    private class ListIter implements ListIterator<T> {
        int current;

        ListIter(int current) {
            this.current = current;
        }


        @Override
        public boolean hasNext() {
            return current + 1 < pointerFreeSpace;
        }

        @Override
        public T next() {
            return data[++current];
        }

        @Override
        public boolean hasPrevious() {
            return current > 0;
        }

        @Override
        public T previous() {
            return data[current--];
        }

        @Override
        public int nextIndex() {
            return current + 1;
        }

        @Override
        public int previousIndex() {
            return current - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            data[current] = t;
        }

        @Override
        public void add(T t) {
            MyArrayList.this.add(t);
        }
    }


    @Override
    public void sort(Comparator<? super T> c) {
        T[] limitedArray = Arrays.copyOfRange(data, 0, pointerFreeSpace);
        Arrays.sort(limitedArray, c);
        System.arraycopy(limitedArray, 0, data, 0, limitedArray.length);
    }
}
