package io.lh.rpc.commom.asserts;

import java.util.List;

/**
 * The type Asserts null.
 *
 * @param <T> the type parameter
 */
public class AssertsNull<T> {

    /**
     * 判断null，然后返回null如果是空
     *
     * @param element the element
     * @return the t
     */
    public static Object nullCheck2Null(List<Object> element) {
        if (nullCheck(element)) return null;
        return element.get(0);
    }

    /**
     * Null check boolean.
     *
     * @param e the e
     * @return the boolean
     */
    public static boolean nullCheck(List<Object> e) {
        if (null == e || e.size() < 1) return true;
        return false;
    }
}
