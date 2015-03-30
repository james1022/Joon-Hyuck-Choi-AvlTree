//AvlTree.java
/*
 * Name: Joon Hyuck Choi
 * CS226 Homework 3
 * Date: Mar 24, 2015
 * Last edited: 10:38 03/24/15
 * email: jchoi100@jhu.edu
 */

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss, with substantial modification by Sara More.
 * 
 * @param <T> the type of data to hold in each tree node.
 *
 */
public class AvlTree<T extends Comparable<? super T>> implements Iterable<T> {

    /** The AVL balance condition allows a small imbalance
         in subtree heights; this is the amount. */
    private static final int ALLOWED_IMBALANCE = 1;
    
    /** The number of spaces for each level. 
        3 spaces is the specified number in the assignment.
        This number will be used in sidewaysPrint. */
    private static final int NUM_SKIP = 3;

    /** Indicates at what level each node is in the tree. */
    private static int level = 0;
    
    /** Contains the size (number of nodes) of the Avl tree. */
    protected int size;
    
    /** The tree root. */
    private AvlNode<T> root;

    /**
     * Construct the tree.
     */
    public AvlTree() {
        this.root = null;
        this.size = 0;
    } //end constructor

    
    /**
     * Insert into the tree; duplicates are ignored.
     * @param x the item to insert.
     */
    public void insert(T x) {
        this.root = this.insert(x, this.root);
        this.size++;
    } //end public insert()

    
    /**
     * Remove from the tree. Nothing is done if x is not found.
     * @param x the item to remove
     */
    public void remove(T x) {
        
        if (this.isEmpty()) {
            System.out.println("Empty tree.");
        }
        
        if (this.contains(x)) {
            this.root = this.remove(x, this.root);
            this.size--;
        }
        
    } //end public remove()
     
    
    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty
     */
    public T findMin() {
        if (this.isEmpty()) {
            throw new UnderflowException();
        }
        return this.findMin(this.root).element;
    } //end public findMin()

    
    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty
     */
    public T findMax() {
        if (this.isEmpty()) {
            throw new UnderflowException();
        }
        return this.findMax(this.root).element;
    } //end public findMax()

    
    /**
     * Find an item in the tree.
     * @param x the item to search for
     * @return true if x is found
     */
    public boolean contains(T x) {
        return this.contains(x, this.root);
    } //end public contains()

    
    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        this.root = null;
    } //end makeEmpty()

    
    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return this.root == null;
    } //end isEmpty()

    
    /**
     * Print the tree contents in sorted order, or the String
     * "Empty tree." if the tree contains no nodes.
     */
    public void printTree() {
        if (this.isEmpty()) {
            System.out.println("Empty tree.");
        } else {
            this.printTree(this.root);
        }
    } //end public printTree()

    
    /**
     * "Sideways-print" the tree contents, or the String
     * "Empty tree." if the tree contains no nodes.<br>
     */
    public void sidewaysPrintTree() {
        if (this.isEmpty()) {
            System.out.println("Empty tree.");
        } else {
            this.sidewaysPrintTree(this.root);
        }     
    } //end public sidewaysPrintTree()

    
    /**
     * Return all values in the range from low to high, inclusive, that
     * appear in the tree, in order.<br>
     * 
     * <b>Assignment requirement:</b>
     * The execution time of this method must be O(k + log N), where
     * N = number of nodes in tree, and k = number of nodes in range.
     *
     * Allowed to use a local ArrayList but not an instance variable ArrayList.
     *
     * @param low the lower end of the range
     * @param high the higher end of the range
     * @return a ordered list of all values present in the tree
     *      which fall into the specified range
     */
    public ArrayList<T> reportValuesInRange(T low, T high) {
        ArrayList<T> rangeList = new ArrayList<>();
        this.reportValuesInRange(rangeList, low, high, this.root);
        return rangeList;
    } //end public reportValuesInRange()
    
    
    /** 
     * Internal method to perform rotations needed to balance this subtree.
     * Assume curr is either balanced or within one of being balanced.
     * @param curr subtree that needs to become balanced
     * @return root of now-balanced subtree
     */
    private AvlNode<T> balance(AvlNode<T> curr) {
        if (curr == null) {
            return curr;
        }
        
        if (this.height(curr.left) - this.height(curr.right) 
            > ALLOWED_IMBALANCE) {
            if (this.height(curr.left.left) >= this.height(curr.left.right)) {
                curr = this.performRightRotation(curr);
            } else {
                curr = this.performLeftRightCombo(curr);
            }
        } else if (this.height(curr.right) - this.height(curr.left) 
            > ALLOWED_IMBALANCE) {

            if (this.height(curr.right.right) >= this.height(curr.right.left)) {
                curr = this.performLeftRotation(curr);
            } else {
                curr = this.performRightLeftCombo(curr);
            }
        }
        curr.height = Math.max(this.height(curr.left), 
            this.height(curr.right)) + 1;
        return curr;
    } //end private balance()
    
    
    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert
     * @param curr the node that roots the subtree
     * @return the new root of the subtree
     */
    private AvlNode<T> insert(T x, AvlNode<T> curr) {
        if (curr == null) {
            return new AvlNode<>(x, null, null);
        }
        
        int compareResult = x.compareTo(curr.element);
        
        if (compareResult < 0) {
            curr.left = this.insert(x, curr.left);
        } else if (compareResult > 0) {
            curr.right = this.insert(x, curr.right);
        }
        //else we have a duplicate; do nothing

        return this.balance(curr);
    } //end private insert()

    /**
     * Private remove method that is called by the public remove.
     * @param x the item to remove.
     * @param curr the current node the method is looking at.
     * @return the resultant tree.
     */
    private AvlNode<T> remove(T x, AvlNode<T> curr) {
        
        //check if curr is null
        if (curr == null) {
            return curr;
        } //end if
        
        //compare the node value with x
        int compResult = x.compareTo(curr.element);
            
        //keep searching for the item to remove
        if (compResult < 0) {
            curr.left = this.remove(x, curr.left);
        } else if (compResult > 0) {
            curr.right = this.remove(x, curr.right);
        } else if (curr.left != null && curr.right != null) {
            curr.element = this.findMin(curr.right).element;    
            curr.right = this.remove(curr.element, curr.right);
        } else {
            if (curr.left != null) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        
        return this.balance(curr);
        
    } //end private remove()
    
    
    /**
     * Internal method to find the smallest item in a subtree.
     * @param curr the node that roots the tree
     * @return node containing the smallest item
     */
    private AvlNode<T> findMin(AvlNode<T> curr) {
        if (curr == null) {
            return curr;
        }

        while (curr.left != null) {
            curr = curr.left;
        }
        return curr;
    } //end private findMin()


    /**
     * Internal method to find the largest item in a subtree.
     * @param curr the node that roots the tree
     * @return node containing the largest item
     */
    private AvlNode<T> findMax(AvlNode<T> curr) {
        if (curr == null) {
            return curr;
        }

        while (curr.right != null) {
            curr = curr.right;
        }
        return curr;
    } //end private findMax()


    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for
     * @param curr the node that roots the tree
     * @return true if x is found in subtree
     */
    private boolean contains(T x, AvlNode<T> curr) {
        while (curr != null) {
            int compareResult = x.compareTo(curr.element);
            
            if (compareResult < 0) {
                curr = curr.left;
            } else if (compareResult > 0) {
                curr = curr.right;
            } else {
                return true;    // Match
            }
        }

        return false;   // No match
    } //end private contains()


    /**
     * Internal method to print a subtree in sorted order.
     * @param curr the node that roots the tree
     */
    private void printTree(AvlNode<T> curr) {
        if (curr != null) {
            this.printTree(curr.left);
            System.out.println(curr.element);
            this.printTree(curr.right);
        }
    } //end private printTree()
    
    
    /**
     * Private method for printing the tree sideways.
     * @param curr the current node being visited.
     */
    private void sidewaysPrintTree(AvlNode<T> curr) {
        
        level++;
        int numSpace = NUM_SKIP * (level - 1);
        
        if (curr != null) {
            
            this.sidewaysPrintTree(curr.right);
            
            for (int i = 0; i < numSpace; i++) {
                System.out.print(" ");
            }
            System.out.println(curr.element);
            
            this.sidewaysPrintTree(curr.left);
            
        }
        level--;
    } //end private sidewaysPrintTree()
    
    
    /**
     * Internal reportValuesInRange method.
     * @param pList the ArrayList passed in as a parameter.
     * @param low the low boundary.
     * @param high the high boundary.
     * @param curr the current node being looked at.
     */
    private void reportValuesInRange(ArrayList<T> pList, 
                                      T low, T high, AvlNode<T> curr) {       
        //when the tree is empty
        if (this.root == null) {
            return;
        }
        
        //when the node being looked at is null
        if (curr == null) {
            return;
        } //end if
        
        int lowComp = curr.element.compareTo(low);
        int highComp = curr.element.compareTo(high);
        
        //All possible cases to consider
        if (highComp > 0) {
            this.reportValuesInRange(pList, low, high, curr.left);
        } else if (lowComp < 0) {
            this.reportValuesInRange(pList, low, high, curr.right);
        } else if (lowComp > 0 && highComp < 0) {
            this.reportValuesInRange(pList, low, high, curr.left);
            pList.add(curr.element);
            this.reportValuesInRange(pList, low, high, curr.right);
        } else if (lowComp == 0) {
            pList.add(curr.element);
            this.reportValuesInRange(pList, low, high, curr.right);
        } else if (highComp == 0) {
            this.reportValuesInRange(pList, low, high, curr.left);
            pList.add(curr.element);
        } //end all cases
        
    } //end private reportValuesInRange()

    
    /**
     * Return the height of node t, or -1, if null.
     * @param curr  the node whose height is wanted
     * @return the height of the given node, or -1 if null
     */
    private int height(AvlNode<T> curr) {
        if (curr == null) {
            return -1;
        }
        return curr.height;
    } //end private height()

    
    /**
     * Returns the size of the Avl tree.
     * @return the size of the Avl tree.
     */
    private int size() {
        return this.size;
    } //end size()
    

    /** 
     * Perform a "simple right" rotation; that is, rotate binary tree
     * node with left child.
     * Update heights, then return new root.
     * @param curr root of subtree where rotation should occur
     * @return root of the modified subtree
     */
    private AvlNode<T> performRightRotation(AvlNode<T> curr) {
        AvlNode<T> child = curr.left;
        curr.left = child.right;
        child.right = curr;
        curr.height = Math.max(this.height(curr.left), 
            this.height(curr.right)) + 1;
        child.height = Math.max(this.height(child.left), curr.height) + 1;
        return child;
    } //end private performRightRotation()


    /**
     * Perform a "simple left" rotation; that is, rotate binary tree
     * node with right child.
     * Update heights, then return new root.
     * @param curr root of subtree where rotation should occur
     * @return root of the modified subtree
     */
    private AvlNode<T> performLeftRotation(AvlNode<T> curr) {
        AvlNode<T> child = curr.right;
        curr.right = child.left;
        child.left = curr;
        curr.height = Math.max(this.height(curr.left), 
            this.height(curr.right)) + 1;
        child.height = Math.max(this.height(child.right), curr.height) + 1;
        return child;
    } //end private performLeftRotation()


    /**
     * Perform a "left-right combo" rotation; that is, rotate curr's
     * left child with curr's left-right grandchild, then curr with
     * its new left child.
     * Update heights, then return new root.
     * @param curr root of subtree where rotation should occur
     * @return root of the modified subtree
     */
    private AvlNode<T> performLeftRightCombo(AvlNode<T> curr) {
        curr.left = this.performLeftRotation(curr.left);
        return this.performRightRotation(curr);
    } //end private performLeftRightCombo()


    /**
     * Perform a "right-left combo" rotation; that is, rotate curr's
     * right child with curr's right-left grandchild; then curr with 
     * its new right child.
     * Update heights, then return new root.
     * @param curr root of subtree where rotation should occur
     * @return root of the modified subtree
     */
    private AvlNode<T> performRightLeftCombo(AvlNode<T> curr) {
        curr.right = this.performRightRotation(curr.right);
        return this.performLeftRotation(curr);
    }  //end private performRightLeftCombo()

    
    /**
     * A node in an AVL tree.
     */
    private static class AvlNode<T> {

        /** The data in the node. */
        T element;
        /** Left child. */
        AvlNode<T> left;
        /** Right child. */
        AvlNode<T> right;
        /** Height of node within its tree. */
        int height;


        /**
         * Construct a new leaf node containing data.
         * @param theElement data to store in the node.
         */
        AvlNode(T theElement) {
            this(theElement, null, null);
        } //end constructor1


        /**
         * Construct a new node containing data, with specified subtrees.
         * @param theElement data to store in the node
         * @param lt the root of the left subtree
         * @param rt the root of the right subtree
         */
        AvlNode(T theElement, AvlNode<T> lt, AvlNode<T> rt) {
            this.element  = theElement;
            this.left = lt;
            this.right = rt;
            this.height = 0;
        } //end constructor2

    } //end inner class AvlNode<T>


    /**
     * Iterator method for the AvlTree class.
     */
    @Override
    public Iterator<T> iterator() {
        return new AvlIterator();
    } //end iterator()
    
    
    /**
     * The helper method that saves all the elements in the Avl tree
     * in an ArrayList in a postorder fashion.
     * @param curr the current node being visited.
     * @param list the list to be filled.
     */
    private void postOrder(AvlNode<T> curr, ArrayList<T> list) {
        if (curr != null) {
            this.postOrder(curr.left, list);
            this.postOrder(curr.right, list);
            list.add(curr.element);
        }
    } //end postOrder()
    

    /**
     * Do a postfix traversal of the AVL Tree.
     * 
     * @author Joon Hyuck Choi
     *
     */
    private class AvlIterator implements Iterator<T> {

        /*******CHECK IF WE CAN USE THIS HERE*********/
        /**
         * The list that will contain the postordered elements.
         */
        private ArrayList<T> list;
        
        /**
         * The current position in the ArrayList.
         */
        private int current;
        
        /**
         * The constructor for the AvlIterator class.
         */
        public AvlIterator() {  
            this.list = new ArrayList<>(AvlTree.this.size());
            this.current = 0;
            AvlTree.this.postOrder(AvlTree.this.root, this.list);
        } //end constructor
        
        /**
         * The overridden hasNext() method.
         */
        @Override
        public boolean hasNext() {
            return this.current < AvlTree.this.size();
        } //end hasNext()

        /**
         * The overridden next() method.
         */
        @Override
        public T next() {
            if (!this.hasNext()) {
                return null;
            } else {
                T nextItem = this.list.get(this.current);
                this.current++;
                return nextItem;
            }
         
        } //end next()
        
    } //end inner class AvlIterator
    
} //end class
