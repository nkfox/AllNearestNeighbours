package avltree;

import java.util.Comparator;

/**
 * Created by Vladislav Kichatiy on 25.04.2016.
 * Comparator for sorting points in convex hull
 */
class ComparableComparator<E> implements Comparator<E> {
    public ComparableComparator() { }

    @Override
    public int compare(E o1, E o2) {
        if (o1 == null || o2 == null) {
            throw new NullPointerException();
        }

        @SuppressWarnings("unchecked")
        int res = ((Comparable<E>) o1).compareTo(o2);
        return res;
    }
}
