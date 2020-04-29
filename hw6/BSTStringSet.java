import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author
 */
public class BSTStringSet implements StringSet, Iterable<String>, SortedStringSet {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        _root = helpput(_root, s);
        // FIXME: PART A
    }

    public Node helpput(Node root, String s){
        if (root == null) {
            return new Node(s);
        } else {
            if (s.compareTo(root.s) < 0) {
                root.left = helpput(root.left, s);
            }
            if (s.compareTo(root.s) > 0) {
                root.right = helpput(root.right, s);
            }
        }
        return root;
    }


    @Override
    public boolean contains(String s) {
        return helpcontains(s, _root); // FIXME: PART A
    }

    public boolean helpcontains(String s, Node root){
        if (root == null) {
            return false;
        } else if (s.compareTo(root.s) == 0) {
            return true;
        } else if (s.compareTo(root.s) < 0) {
            return helpcontains(s, root.left);
        } else  {
            return helpcontains(s, root.right);
        }
    }

    @Override
    public List<String> asList() {
        ArrayList<String> result = new ArrayList<String>();
        helpasList(_root, result);
        return result; // FIXME: PART A
    }

    public void helpasList(Node root, ArrayList<String> list) {
        if (root != null) {
            if (root.left != null) {
                helpasList(root.left, list);
            } list.add(root.s);
            if (root.right != null) {
            helpasList(root.right, list);
            }

        }
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B

    private static class BSTBoundIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();
        private String _low;
        private String _high;
        /** A new iterator over the labels in NODE. */
        BSTBoundIterator(Node node,String low,String high) {
            _low = low;
            _high = high;
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node node = _toDo.pop();
            addTree(node.right);
            while (node.s.compareTo(_low) < 0) {
                node = _toDo.pop();
                addTree(node.right);
            }
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                if (node.s.compareTo(_high) <= 0) {
                    _toDo.push(node);
                }
                    node = node.left;
            }
        }
    }
    @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTBoundIterator(_root, low, high);  // FIXME: PART B
    }


    /** Root node of the tree. */
    private Node _root;
}
