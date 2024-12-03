package com.datn.hotelmanagement.utils;

import java.util.Collection;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }

    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

}