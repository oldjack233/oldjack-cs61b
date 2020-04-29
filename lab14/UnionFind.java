
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        // FIXME
        collection = new int[N];
        for (int i = 1; i <= N; i++) {
            collection[i-1] = i;
        }

    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {
        if (collection[v-1] == v) {
            return v;
        }
        collection[v-1] = find(collection[v-1]);
        return collection[v-1];// FIXME
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        int root = find(u);
        collection[find(v) -1]=root;
        return root;  // FIXME
    }

    // FIXME
    public int[] collection;
}
