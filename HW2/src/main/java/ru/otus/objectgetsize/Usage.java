package ru.otus.objectgetsize;

import ru.otus.objectgetsize.impl.GetSizeBySpecification;
import ru.otus.objectgetsize.impl.GetSizeInstrumentation;

import java.io.IOException;

/**
 * Created by sergey on 09.04.17.
 */
public class Usage {
    public static void main(String[] args) throws GettingSizeError {
        System.out.println("getObjectSize begin");
        ObjectGetSize objSizeGetterInst = new GetSizeInstrumentation();
        ObjectGetSize objSizeGetterBySpec = new GetSizeBySpecification();

        long header = GetSizeInstrumentation.getArrayHeaderSize();

        Object obj = new Object();
        System.out.println("object Size:" + objSizeGetterInst.getSize(obj) + ", specification:" + "N/A");

        String emptyString = "";
        System.out.println("emptyString:" + objSizeGetterInst.getSize(emptyString)
                + ", specification:" + objSizeGetterBySpec.getSize(emptyString));

        String notEmpty = "notEmpty";
        System.out.println("NotEmptyString:" + objSizeGetterInst.getSize(notEmpty)
                + ", specification:" + objSizeGetterBySpec.getSize(notEmpty));

        String longString = "long-long-long-long-long-long-long-long-long-long-long-long-long-long";
        System.out.println("longString:" + objSizeGetterInst.getSize(longString)
                + ", specification:" + objSizeGetterBySpec.getSize(longString));

        Object[] objArray = new Object[]{};
        System.out.println("Array []:" + objSizeGetterInst.getSize(objArray) + " (header:" + header + ") "
                + ", specification:" + objSizeGetterBySpec.getSize(objArray));

        int[] arrInt = new int[]{};
        System.out.println("ArrayInt[]:" + objSizeGetterInst.getSize(arrInt) + " (header:" + header + ") "
                + ", specification:" + objSizeGetterBySpec.getSize(arrInt));


        int[] arrInt1000 = new int[1000];
        System.out.println("ArrayInt1000[]:" + objSizeGetterInst.getSize(arrInt1000) + " (header:" + header + ") "
                + ", specification:" + objSizeGetterBySpec.getSize(arrInt1000));

        short[] arrShort1000 = new short[1000];
        System.out.println("ArrayShort1000[]:" + objSizeGetterInst.getSize(arrShort1000) + " (header:" + header + ") "
                + ", specification:" + objSizeGetterBySpec.getSize(arrShort1000));


        int intVal = 5;
        System.out.println("int:" + objSizeGetterInst.getSize(intVal)
                + ", specification:" + objSizeGetterBySpec.getSize(intVal));

        long longVal = 5L;
        System.out.println("long:" + objSizeGetterInst.getSize(longVal)
                + ", specification:" + objSizeGetterBySpec.getSize(longVal));

        byte byteVal = 5;
        System.out.println("byte:" + objSizeGetterInst.getSize(byteVal)
                + ", specification:" + objSizeGetterBySpec.getSize(byteVal));

        byte[] byteArr2 = new byte[2];
        System.out.println("byte[2]:" + objSizeGetterInst.getSize(byteArr2) + " (header:" + header + ") "
                + ", specification:" + objSizeGetterBySpec.getSize(byteArr2));

        byte[] byteArr4 = new byte[4];
        System.out.println("byte[4]:" + objSizeGetterInst.getSize(byteArr4) + " (header:" + header + ") "
                + ", specification:" + objSizeGetterBySpec.getSize(byteArr4));

        System.out.println("getObjectSize end");
    }
}
