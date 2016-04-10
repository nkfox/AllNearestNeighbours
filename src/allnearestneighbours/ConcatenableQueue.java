package allnearestneighbours;

/**
 * Created by Nataliia Kozoriz on 10.04.2016.
 * Class for convex hull
 */
public class ConcatenableQueue<T extends Comparable<T>> {
    private static class CQNode<T extends Comparable<T>> {
        private T value;
        private int childrenCount;
        private int height;

        private CQNode<T> left;
        private CQNode<T> right;

        public CQNode(T value) {
            this.value = value;
            height = 1;
            childrenCount = 0;
            left = null;
            right = null;
        }

        private static <T extends Comparable<T>> int getHeight(CQNode<T> node) {
            return node == null ? 0 : node.height;
        }

        private static <T extends Comparable<T>> int getBalanceFactor(CQNode<T> node) {
            return node == null ? 0 : getHeight(node.right) - getHeight(node.left);
        }

        public static <T extends Comparable<T>> int getNodeCount(CQNode<T> node) {
            return node == null ? 0 : node.childrenCount + 1;
        }

        private static <T extends Comparable<T>> void update(CQNode<T> node) {
            node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
            node.childrenCount = getNodeCount(node.left) + getNodeCount(node.right);
        }

        private static <T extends Comparable<T>> CQNode<T> rotateRight(CQNode<T> p) {
            CQNode<T> q = p.left;
            p.left = q.right;
            q.right = p;
            update(p);
            update(q);
            return q;
        }

        private static <T extends Comparable<T>> CQNode<T> rotateLeft(CQNode<T> q) {
            CQNode<T> p = q.right;
            q.right = p.left;
            p.left = q;
            update(q);
            update(p);
            return p;
        }

        private static <T extends Comparable<T>> CQNode<T> balance(CQNode<T> p) {
            update(p);

            if (getBalanceFactor(p) >= 2) {
                if (getBalanceFactor(p.right) < 0) {
                    p.right = rotateRight(p.right);
                }

                return balance(rotateLeft(p));
            }

            if (getBalanceFactor(p) <= -2) {
                if (getBalanceFactor(p.left) > 0) {
                    p.left = rotateLeft(p.left);
                }

                return balance(rotateRight(p));
            }

            return p;
        }

        private static <T extends Comparable<T>> CQNode<T> insert(CQNode<T> p, T value) {
            if (p == null) {
                return new CQNode<>(value);
            }

            if (value.compareTo(p.value) < 0) {
                p.left = insert(p.left, value);
            } else {
                p.right = insert(p.right, value);
            }

            return balance(p);
        }

        private static <T extends Comparable<T>> CQNode<T> concatenate(CQNode<T> p, CQNode<T> node) {
            if (p == null) {
                return node;
            }

            if (node.value.compareTo(p.value) < 0) {
                p.left = concatenate(p.left, node);
            } else {
                p.right = concatenate(p.right, node);
            }

            return balance(p);
        }

        private static <T extends Comparable<T>> CQNode<T> findMin(CQNode<T> p) {
            return p.left == null ? p : findMin(p.left);
        }

        private static <T extends Comparable<T>> CQNode<T> removeMin(CQNode<T> p) {
            if (p.left == null) {
                return p.right;
            }

            p.left = removeMin(p.left);
            return balance(p);
        }

        private static <T extends Comparable<T>> CQNode<T> remove(CQNode<T> p, T value) {
            if (p == null) {
                return null;
            }

            if (value.compareTo(p.value) < 0) {
                p.left = remove(p.left, value);
            } else if (value.compareTo(p.value) > 0) {
                p.right = remove(p.right, value);
            } else {
                CQNode<T> q = p.left;
                CQNode<T> r = p.right;

                if (r == null) {
                    return q;
                }

                CQNode<T> min = findMin(r);
                min.right = removeMin(r);
                min.left = q;
                return balance(min);
            }

            return balance(p);
        }

        private static <T extends Comparable<T>> CQNode<T> split(CQNode<T> p, T value, boolean isLeft) {
            if (p == null) {
                return null;
            }

            if (value.compareTo(p.value) < 0) {
                if (isLeft)
                    p = split(p.left, value, isLeft);
                else
                    p.left = split(p.left, value, isLeft);
            } else if (value.compareTo(p.value) > 0) {
                if (!isLeft)
                    p = split(p.right, value, isLeft);
                else
                    p.right = split(p.right, value, isLeft);
            } else {
                if (isLeft)
                    p.right = null;
                else
                    p.left = null;
            }

            return balance(p);
        }

        private static <T extends Comparable<T>> T kthElement(CQNode<T> node, int k) {
            if (k <= 0 || k > getNodeCount(node)) {
                return null;
            }

            if (k == getNodeCount(node.left) + 1) {
                return node.value;
            } else {
                return k < getNodeCount(node.left) + 1 ? kthElement(node.left, k) :
                        kthElement(node.right, k - getNodeCount(node.left) - 1);
            }
        }
    }

    private CQNode<T> head;

    public ConcatenableQueue() {
        head = null;
    }

    public void insert(T value) {
        if (head == null) {
            head = new CQNode<T>(value);
        } else {
            head = CQNode.insert(head, value);
        }
    }

    public void remove(T value) {
        if (head != null) {
            head = CQNode.remove(head, value);
        }
    }

    public T kthElement(int k) {
        return CQNode.kthElement(head, k);
    }

    public void concatenate(ConcatenableQueue<T> tree) {
        if (head == null) {
            head = tree.head;
        } else {
            head = CQNode.concatenate(head, tree.head);
        }
    }

    public void split(T value, boolean isLeft) {
        if (head != null) {
            head = CQNode.split(head, value, isLeft);
        }
    }
}
