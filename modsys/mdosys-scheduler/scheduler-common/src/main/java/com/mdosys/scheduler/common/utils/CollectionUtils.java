package com.mdosys.scheduler.common.utils;

import org.apache.commons.beanutils.BeanMap;

import java.util.*;

/**
 * Provides utility methods and decorators for {@link Collection} instances.
 * <p>
 * Various utility methods might put the input objects into a Set/Map/Bag. In case
 * the input objects override {@link Object#equals(Object)}, it is mandatory that
 * the general contract of the {@link Object#hashCode()} method is maintained.
 * <p>
 * NOTE: From 4.0, method parameters will take {@link Iterable} objects when possible.
 *
 * @version $Id: CollectionUtils.java 1686855 2015-06-22 13:00:27Z tn $
 * @since 1.0
 */
public class CollectionUtils {

    private CollectionUtils() {
        throw new UnsupportedOperationException("Construct CollectionUtils");
    }

    /**
     * returns {@code true} iff the given {@link Collection}s contain
     * exactly the same elements with exactly the same cardinalities.
     *
     * @param a the first collection
     * @param b the second collection
     * @return Returns true iff the given Collections contain exactly the same elements with exactly the same cardinalities.
     * That is, iff the cardinality of e in a is equal to the cardinality of e in b, for each element e in a or b.
     */
    public static boolean equalLists(Collection<?> a, Collection<?> b) {
        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return org.apache.commons.collections.CollectionUtils.isEqualCollection(a, b);
    }

    /**
     * Removes certain attributes of each object in the list
     *
     * @param originList origin list
     * @param exclusionSet exclusion set
     * @param <T> T
     * @return removes certain attributes of each object in the list
     */
    public static <T extends Object> List<Map<String, Object>> getListByExclusion(List<T> originList, Set<String> exclusionSet) {
        List<Map<String, Object>> instanceList = new ArrayList<>();
        if (exclusionSet == null) {
            exclusionSet = new HashSet<>();
        }
        if (originList == null) {
            return instanceList;
        }
        Map<String, Object> instanceMap;
        for (T instance : originList) {
            BeanMap beanMap = new BeanMap(instance);
            instanceMap = new LinkedHashMap<>(16, 0.75f, true);
            for (Map.Entry<Object, Object> entry : beanMap.entrySet()) {
                if (exclusionSet.contains(entry.getKey())) {
                    continue;
                }
                instanceMap.put((String) entry.getKey(), entry.getValue());
            }
            instanceList.add(instanceMap);
        }
        return instanceList;
    }

}
