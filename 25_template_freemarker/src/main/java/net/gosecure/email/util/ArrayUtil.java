package net.gosecure.email.util;

import java.lang.reflect.Array;

public class ArrayUtil {

    public static <T> T[] append(T[] array, T value) {
        Class<?> arrayClass = array.getClass();

        T[] newArray = (T[])Array.newInstance(
                arrayClass.getComponentType(), array.length + 1);

        System.arraycopy(array, 0, newArray, 0, array.length);

        newArray[array.length] = value;

        return newArray;
    }

    public static <T> T[] append(T[] array1, T[] array2) {
        Class<?> array1Class = array1.getClass();

        T[] newArray = (T[]) Array.newInstance(
                array1Class.getComponentType(), array1.length + array2.length);

        System.arraycopy(array1, 0, newArray, 0, array1.length);

        System.arraycopy(array2, 0, newArray, array1.length, array2.length);

        return newArray;
    }

    public static <T> T[][] append(T[][] array1, T[] value) {
        Class<?> array1Class = array1.getClass();

        T[][] newArray = (T[][])Array.newInstance(
                array1Class.getComponentType(), array1.length + 1);

        System.arraycopy(array1, 0, newArray, 0, array1.length);

        newArray[array1.length] = value;

        return newArray;
    }

    public static <T> T[][] append(T[][] array1, T[][] array2) {
        Class<?> array1Class = array1.getClass();

        T[][] newArray = (T[][])Array.newInstance(
                array1Class.getComponentType(), array1.length + array2.length);

        System.arraycopy(array1, 0, newArray, 0, array1.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);

        return newArray;
    }

}
