package avltree;


import allnearestneighbours.VoronoiPoint;

import java.util.*;

import static avltree.AVLTree.AVLNode.balance;
import static avltree.AVLTree.AVLNode.replaceSon;

/**
 * Created by Vladislav Kichatiy on 25.04.2016.
 * Concatenable queue
 */
public class AVLTree<E> extends AbstractCollection<E> implements Collection<E> {
    private Comparator<? super E> comparator;

    AVLNode<E> head;

    public AVLTree() {
        this.comparator = new ComparableComparator<>();
    }

    public AVLTree(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    public AVLTree(AVLNode<E> head) {
        this.comparator = new ComparableComparator<>();
        this.head = head;
    }

    public AVLTree(AVLNode<E> head, Comparator<E> comparator) {
        this.comparator = comparator;
        this.head = head;
    }

    public AVLTree(Collection<? extends E> c) {
        this.comparator = new ComparableComparator<>();
        this.addAll(c);
    }

    public AVLTree(Collection<? extends E> c, Comparator<E> comparator) {
        this.comparator = comparator;
        this.addAll(c);
    }

    public Comparator<? super E> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    public int size() {
        return AVLNode.getNodeCount(head);
    }

    public static <E extends Comparable<E>> AVLTree<E> join(AVLTree<E> left, AVLTree<E> right) {
        if (left == null) {
            return right;
        }

        if (right == null) {
            return left;
        }

        return new AVLTree<>(AVLNode.join(left.head, right.head));
    }

    public void retainInterval(E min, E max, boolean minOpen, boolean maxOpen) {
        if (min == null || max == null) {
            throw new NullPointerException();
        }

        if (comparator.compare(min, max) > 0) {
            removeInterval(max, min, !maxOpen, !minOpen);
            return;
        }

        List<AVLNode<E>> minList = split(head, min, minOpen);
        head = minList.get(1);
        List<AVLNode<E>> maxList = split(head, max, !maxOpen);
        head = maxList.get(0);
    }

    public void retainSegment(E min, E max) {
        this.retainInterval(min, max, false, false);
    }

    public void retainInterval(E min, E max) {
        this.retainInterval(min, max, true, true);
    }

    public void removeInterval(E min, E max, boolean minOpen, boolean maxOpen) {
        if (min == null || max == null) {
            throw new NullPointerException();
        }

        if (comparator.compare(min, max) > 0) {
            retainInterval(max, min, !maxOpen, !minOpen);
            return;
        }

        List<AVLNode<E>> minList = split(head, min, minOpen);
        head = minList.get(1);
        List<AVLNode<E>> maxList = split(head, max, !maxOpen);
        head = AVLNode.join(minList.get(0), maxList.get(1));
    }

    public void removeSegment(E min, E max) {
        this.removeInterval(min, max, false, false);
    }

    public void removeInterval(E min, E max) {
        this.removeInterval(min, max, true, true);
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }

        try {
            @SuppressWarnings("unchecked")
            E e = (E) o;
            return find(head, e) != null;
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }

        try {
            @SuppressWarnings("unchecked")
            E e = (E) o;
            int prevSize = this.size();
            head = remove(head, e);
            return prevSize != this.size();
        } catch (ClassCastException e) {
            return false;
        }
    }

    @Override
    public boolean add(E e) {
        int prevSize = this.size();
        head = insert(head, e);
        return prevSize != this.size();
    }

    public E get(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }

        return AVLNode.get(head, index);
    }

    public AVLNode<E> insert(AVLNode<E> p, E value) {
        if (p == null) {
            return new AVLNode<>(value);
        }

        if (comparator.compare(value, p.value) < 0) {
            if (p.left != null) {
                p.left = insert(p.left, value);
            } else {
                AVLNode.setLeft(p, new AVLNode<>(value, null, null, p.prev, p));
            }
        } else {
            if (p.right != null) {
                p.right = insert(p.right, value);
            } else {
                AVLNode.setRight(p, new AVLNode<>(value, null, null, p, p.next));
            }
        }

        return balance(p);
    }

    public AVLNode<E> find(AVLNode<E> p, E value) {
        if (p == null) {
            return null;
        }

        int cmp = comparator.compare(value, p.value);
        if (cmp < 0) {
            return find(p.left, value);
        } else if (cmp > 0) {
            return find(p.right, value);
        }

        return p;
    }

    public AVLNode<E> remove(AVLNode<E> p, E value) {
        if (p == null) {
            return null;
        }

        int cmp = comparator.compare(value, p.value);
        if (cmp < 0) {
            p.left = remove(p.left, value);
        } else if (cmp > 0) {
            p.right = remove(p.right, value);
        } else {
            AVLNode<E> q = p.left;
            AVLNode<E> r = p.right;

            if (r == null) {
                replaceSon(p.parent, p, q);
                AVLNode.setNext(p.prev, p.next);
                return q;
            }

            AVLNode<E> min = AVLNode.findMin(r);
            replaceSon(p.parent, p, min);
            AVLNode.setNext(p.prev, p.next);
            AVLNode.setRight(min, AVLNode.removeMin(r));
            AVLNode.setLeft(min, q);
            return balance(min);
        }

        return balance(p);
    }

    public List<AVLNode<E>> split(AVLNode<E> head, E value, boolean valueLeft) {
        if (head == null) {
            List<AVLNode<E>> res = new ArrayList<>();
            res.add(null);
            res.add(null);
            return res;
        }

        AVLNode<E> min = AVLNode.findMin(head);
        AVLNode<E> max = AVLNode.findMax(head);
        AVLNode<E> left = null;
        AVLNode<E> right = null;
        AVLNode<E> node = head;
        while (node != null) {
            int cmp = comparator.compare(value, node.value);
            if (cmp < 0 || cmp == 0 && !valueLeft) {
                if (right == null) {
                    node.parent = null;
                } else {
                    AVLNode.setLeft(right, node);
                }

                right = node;
                node = node.left;
            } else {
                if (left == null) {
                    node.parent = null;
                } else {
                    AVLNode.setRight(left, node);
                }

                left = node;
                node = node.right;
            }
        }

        AVLNode.setNext(left, min);
        AVLNode.setNext(max, right);
        AVLNode.setRight(left, null);
        AVLNode.setLeft(right, null);
        List<AVLNode<E>> res = new ArrayList<>();
        res.add(AVLNode.balanceBranch(left));
        res.add(AVLNode.balanceBranch(right));
        return res;
    }

    private class Itr implements Iterator<E> {
        AVLNode<E> start = AVLNode.findMin(head);
        AVLNode<E> next = start;

        int startingSize = AVLTree.this.size();
        int moveCount;

        boolean moved;

        @Override
        public boolean hasNext() {
            return moveCount < startingSize;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            next = next.next;
            ++moveCount;
            moved = true;
            return next.prev.value;
        }

        @Override
        public void remove() {
            if (!moved) {
                throw new IllegalStateException();
            }

            if (next != null) {
                head = AVLTree.this.remove(head, next.prev.value);
            } else {
                head = null;
            }

            moved = false;
        }
    }

    public static List<VoronoiPoint> getUpperPoints(AVLTree<VoronoiPoint> leftCH, AVLTree<VoronoiPoint> rightCH){
        AVLNode upperSupportLeft = leftCH.head;
        AVLNode upperSupportRight = rightCH.head;
        boolean changedLeft = true, changedRight = true;
        /*while (changedLeft || changedRight){
            // find case: leftLeft, rightRight, leftRightLessPi, leftRightGreaterPi
            //recount changeLeft, changeRight
        }
        List<VoronoiPoint> upper =  new ArrayList<>();
        upper.add(upperSupportLeft.value);
        upper.add(upperSupportRight.value);*/

        return null;
    }

    public static List<VoronoiPoint> getLowerPoints(AVLTree<VoronoiPoint> leftCH, AVLTree<VoronoiPoint> rightCH){
        return null;
    }

    public static class AVLNode<T> {
        public static final int MIN_BALANCE_FACTOR = -1;
        public static final int MAX_BALANCE_FACTOR = 1;

        public T value;
        private int childrenCount;
        private int height;

        public AVLNode<T> left;
        public AVLNode<T> right;

        public AVLNode<T> parent;
        public AVLNode<T> next;
        public AVLNode<T> prev;

        public AVLNode(T value) {
            this(value, null, null);
        }

        public AVLNode(T value, AVLNode<T> left, AVLNode<T> right) {
            this(value, left, right, null, null);
            AVLNode.setPrev(this, this);
        }

        public AVLNode(T value, AVLNode<T> left, AVLNode<T> right, AVLNode<T> prev, AVLNode<T> next) {
            this.value = value;
            AVLNode.setLeft(this, left);
            AVLNode.setRight(this, right);
            AVLNode.update(this);
            AVLNode.setPrev(this, prev);
            AVLNode.setNext(this, next);
        }

        public static <T> void setLeft(AVLNode<T> node, AVLNode<T> left) {
            if (node != null) {
                node.left = left;
                if (left != null) {
                    left.parent = node;
                }
            }
        }

        public static <T> void setRight(AVLNode<T> node, AVLNode<T> right) {
            if (node != null) {
                node.right = right;
                if (right != null) {
                    right.parent = node;
                }
            }
        }

        public static <T> void setPrev(AVLNode<T> node, AVLNode<T> prev) {
            if (node != null) {
                node.prev = prev;
                if (prev != null) {
                    prev.next = node;
                }
            }
        }

        public static <T> void setNext(AVLNode<T> node, AVLNode<T> next) {
            if (node != null) {
                node.next = next;
                if (next != null) {
                    next.prev = node;
                }
            }
        }

        public static <T> boolean isLeftSon(AVLNode<T> parent, AVLNode<T> son) {
            return parent != null && son == parent.left;
        }

        public static <T> boolean isRightSon(AVLNode<T> parent, AVLNode<T> son) {
            return parent != null && son == parent.right;
        }

        public static <T> void replaceSon(AVLNode<T> parent, AVLNode<T> son, AVLNode<T> repl) {
            if (son == null) {
                return;
            }

            if (parent == null) {
                repl.parent = null;
            }

            if (AVLNode.isLeftSon(parent, son)) {
                AVLNode.setLeft(parent, repl);
            }

            if (AVLNode.isRightSon(parent, son)) {
                AVLNode.setRight(parent, repl);
            }
        }

        public static <T> int getHeight(AVLNode<T> node) {
            return node == null ? 0 : node.height;
        }

        public static <T> int getBalanceFactor(AVLNode<T> node) {
            return node == null ? 0 : getHeight(node.right) - getHeight(node.left);
        }

        public static <T> boolean isBalanced(AVLNode<T> node) {
            int balanceFactor = AVLNode.getBalanceFactor(node);
            return MIN_BALANCE_FACTOR <= balanceFactor && balanceFactor <= MAX_BALANCE_FACTOR;
        }

        public static <T> int getNodeCount(AVLNode<T> node) {
            return node == null ? 0 : node.childrenCount + 1;
        }

        private static <T> void update(AVLNode<T> node) {
            if (node == null) {
                return;
            }

            node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
            node.childrenCount = getNodeCount(node.left) + getNodeCount(node.right);
        }

        private static <T> AVLNode<T> rotateRight(AVLNode<T> p) {
            AVLNode<T> q = p.left;
            AVLNode.setLeft(p, q.right);
            AVLNode.setRight(q, p);

            update(p);
            update(q);
            return q;
        }

        private static <T> AVLNode<T> rotateLeft(AVLNode<T> q) {
            AVLNode<T> p = q.right;
            AVLNode.setRight(q, p.left);
            AVLNode.setLeft(p, q);

            update(q);
            update(p);
            return p;
        }

        static <T> AVLNode<T> balance(AVLNode<T> p) {
            update(p);

            if (getBalanceFactor(p) > MAX_BALANCE_FACTOR) {
                if (getBalanceFactor(p.right) < 0) {
                    AVLNode.setRight(p, rotateRight(p.right));
                }

                return rotateLeft(p);
            }

            if (getBalanceFactor(p) < MIN_BALANCE_FACTOR) {
                if (getBalanceFactor(p.left) > 0) {
                    AVLNode.setLeft(p, rotateLeft(p.left));
                }

                return rotateRight(p);
            }

            return p;
        }

        public static <T> AVLNode<T> findMin(AVLNode<T> p) {
            return p == null || p.left == null ? p : findMin(p.left);
        }

        public static <T> AVLNode<T> findMax(AVLNode<T> p) {
            return p == null || p.right == null ? p : findMax(p.right);
        }

        private static <T> AVLNode<T> removeMin(AVLNode<T> p) {
            if (p == null) {
                return null;
            }

            if (p.left == null) {
                return p.right;
            }

            p.left = removeMin(p.left);
            return balance(p);
        }

        public static <T> AVLNode<T> join(AVLNode<T> left, AVLNode<T> right) {
            if (left == null) {
                return right;
            }

            if (right == null) {
                return left;
            }

            AVLNode<T> head = findMin(right);
            setNext(findMax(left), head);
            setNext(findMax(right), findMin(left));
            setRight(head, removeMin(right));
            setLeft(head, left);
            head.parent = null;

            return balanceBranch(head);
        }

        public static <T> AVLNode<T> balanceBranch(AVLNode<T> node) {
            AVLNode<T> last = node;
            while (node != null) {
                update(node);
                while (!isBalanced(node)) {
                    replaceSon(node.parent, node, balance(node));
                }

                last = node;
                node = node.parent;
            }

            update(last);
            return last;
        }

        public static <T> T get(AVLNode<T> node, int index) {
            if (index < 0 || index >= getNodeCount(node)) {
                throw new IndexOutOfBoundsException();
            }

            if (index == getNodeCount(node.left)) {
                return node.value;
            } else {
                return index < getNodeCount(node.left) ? get(node.left, index) :
                        get(node.right, index - getNodeCount(node.left) - 1);
            }
        }

        @Override
        public String toString() {
            return "Node: " + value;
        }
    }
}
