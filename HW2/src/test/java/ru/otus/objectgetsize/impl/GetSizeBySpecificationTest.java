package ru.otus.objectgetsize.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Test;
import ru.otus.objectgetsize.GettingSizeError;

import static org.junit.Assert.*;

/**
 * Created by sergey on 09.04.17.
 */
public class GetSizeBySpecificationTest {

    @Test
    public void getSizeInt() throws GettingSizeError {
        int val = 5;
        long resultVal = new GetSizeBySpecification().getSize(val);
        assertEquals("val", Integer.SIZE / 8, resultVal);
    }


    @Test
    public void getSizeArrayMixed() throws GettingSizeError {
        Object[] arrMix2 = new Object[]{4L,5};
        long resultArrMix2 = new GetSizeBySpecification().getSize(arrMix2);
        assertEquals("arrMix[2]", (Long.SIZE + Integer.SIZE) / 8, resultArrMix2);
    }

    @Test
    public void getSizeArraySimple() throws GettingSizeError {
        byte[] byteArr2 = new byte[2];
        long resultArr2 = new GetSizeBySpecification().getSize(byteArr2);
        assertEquals("Byte[2]", (byteArr2.length * Byte.SIZE) / 8, resultArr2);

        byte[] byteArr500 = new byte[500];
        long resultArr500 = new GetSizeBySpecification().getSize(byteArr500);
        assertEquals("Byte[500]", (byteArr500.length * Byte.SIZE) / 8, resultArr500);

        int[] intArr2 = new int[2];
        resultArr2 = new GetSizeBySpecification().getSize(intArr2);
        assertEquals("int[2]", (intArr2.length * Integer.SIZE) / 8, resultArr2);
    }

    @Test
    public void getSizeArrayArray() throws GettingSizeError {
        byte[][] byteArr2_2 = new byte[2][2];
        long resultArr2_2 = new GetSizeBySpecification().getSize(byteArr2_2);
        assertEquals("Byte[2_2]", (byteArr2_2.length * byteArr2_2.length * Byte.SIZE) / 8, resultArr2_2);

        byte[][] byteArr50_50 = new byte[50][50];
        long resultArr50_50 = new GetSizeBySpecification().getSize(byteArr50_50);
        assertEquals("Byte[50_50]", (byteArr50_50.length * byteArr50_50.length * Byte.SIZE) / 8, resultArr50_50);
    }

    @Test
    public void getSizeString() throws GettingSizeError {
        String str = "stringa";
        long resultStr = new GetSizeBySpecification().getSize(str);
        assertEquals("stringa", (Character.SIZE * str.toCharArray().length) / 8, resultStr);

        String empty = "";
        long resultEmpty = new GetSizeBySpecification().getSize(empty);
        assertEquals("emptyString", (Character.SIZE * empty.toCharArray().length) / 8, resultEmpty);
    }

    @Test(expected = GettingSizeError.class)
    public void getSizeBool() throws GettingSizeError {
        new GetSizeBySpecification().getSize(true);
    }

    @Test(expected = GettingSizeError.class)
    public void getSizeObject() throws GettingSizeError {
        new GetSizeBySpecification().getSize(new Object());
    }

    @Test
    public void getSizeObjectFloat() throws GettingSizeError {
        Object objFloat = new Float(5);
        long resultFloat = new GetSizeBySpecification().getSize(objFloat);
        assertEquals("objectFloat", (Float.SIZE) / 8, resultFloat);
    }

    @Test
    public void getSizeClass() throws GettingSizeError {
        class ClassForSize {
            private int fieldInt;
            private short fieldShort;
        }

        ClassForSize objClass = new ClassForSize();
        long resultObjClass = new GetSizeBySpecification().getSize(objClass);
        assertEquals("resultObjClass", (Integer.SIZE + Short.SIZE) / 8, resultObjClass);
    }

}