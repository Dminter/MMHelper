package com.zncm.dminter.mmhelper.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListUtils {

    public interface Predicate<T> {
        boolean apply(T type);
    }

    public static <T> List<T> filter(Collection<T> target, Predicate<T> predicate) {
        List<T> result = new ArrayList<T>();
        for (T element: target) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }
    
    public static <T> ArrayList<T> makeNullList(int size) {
        ArrayList<T> l = new ArrayList<T>(size);
        for (int i = 0; i < size; ++i) {
            l.add(null);
        }
        return l;
    }

}
