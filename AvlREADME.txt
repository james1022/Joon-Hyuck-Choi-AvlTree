Name: Joon Hyuck Choi
JHED: jchoi100

Requirement: O(k + logN), where k is the number of nodes
	     with the values in the range, and N is the 
	     total number of nodes in the tree.

Big Theta analysis AND explanation:

In terms of Big Omega, the algorithm can't get better than when
the tree is empty, and the code executes just one line of code that
throws an exception. So in this case, k = 0 and N = 0, so 
Big Omega of the code is k + logN = 1.
Also, even in the case when we simply go down exactly one path 
in the tree (i.e. only making recursive calls to only one of either
left or right child), we still end up with k + logN work.

In terms of Big O, the code determines whether the current node has
a value that's in between the range, smaller than the low bound value,
or greater than the high bound value. In the case when the current node
has a value that's stricty in between the range, the code splits the
recursion to both the left child and right child of the current node.
In the case when the current node's value is strictly less than 
the low boundary range value, the code makes a recursive call only to
the right child of the current node. Similarly, when the current node
has a value strictly greater than the high boundary range value, it
makes a recursive call only to the left child of the current node.
When the node's value is exactly the same as either the low or high
boundary range values, it merely adds the value to the ArrayList.
So the code can't get worse than logN in its recursive calls.
Adding the k (number of times that we add the right values in range
to the ArrayList) to the logN we obtained, we still get a O(k + logN).

Moreover, even if we get into the worst case scenario (i.e. the algorithm
visits the recursion in which we split the recursion to BOTH left and right
children and add the current node value to the ArrayList), the maximum 
number of "splits" that the algorithm can take for a tree with N nodes
is logN. Again, the work required then would be logN in the splitting process
and k in the process of adding values to the ArrayList.

Thus, we have a Big Omega(k + logN) and a O(k + logN).
This leads us to conclude that the code has a Big Theta of k + logN.

<END>