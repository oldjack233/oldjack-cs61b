/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        // FIXME: Implement this method and return correct value
        int size = 0;
        DNode neww = new DNode (null, 0, _front);
        if(neww._next == null){
            return 0;
        }
        else{
            while(neww._next != null){
                size += 1;
                neww = neww._next;///
            }
        }
        return size;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        DNode frt = new DNode(null, 0 ,_front);
        DNode bck = new DNode(_back, 0 ,null);
        if(i >= 0){
            for(int j = 0; j <= i; j++){
                frt._val = frt._next._val;
                frt._next = frt._next._next;
        }
            return frt._val;
        }
        else{
            for(int j = 0; j > i;j--){
                bck._val = bck._prev._val;
                bck._prev = bck._prev._prev;
            }
            return bck._val;

        }

        // FIXME: Implement this method and return correct value

    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        if(_front != null) {
            _front = new DNode(null, d, _front);
            _front._next._prev = _front;
        }
        else{
            _front=_back= new DNode(null, d,null);

        // FIXME: Implement this method
    }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
            if(_back != null) {
                _back = new DNode(_back, d, null);
                _back._prev._next= _back;
            }
            else{
                _front=_back= new DNode(null, d,null);

                // FIXME: Implement this method
    }}

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        // FIXME: Implement this method
        DNode newwf = new DNode(null, d, _front);
        DNode newwb = new DNode(_back, d,null);
        int size = size();

        if(index==0 || size() == 0 || index == -(size()) -1 ){
            insertFront(d);
        }
        else if(index == size || index == -1){
            insertBack(d);
        }
        else if(index > 0){
            for(int i = index; i>0; i--){
                newwf._next = newwf._next._next;
        }
            newwf._prev = newwf._next._prev;
            newwf._prev._next = newwf;
            newwf._next._prev = newwf;

    }
        else {
            for(int i = index; i<-1; i++){
                newwb._prev = newwb._prev._prev;
            }
            newwb._next = newwb._prev._next;
            newwb._prev._next = newwb;
            newwb._next._prev = newwb;

        }

    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        // FIXME: Implement this method and return correct value
        int val = _front._val;
        if(_front == _back){
            _front = _back =null;}
        else {
            _front = _front._next;
            _front._prev =null;
        }
        return val;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        // FIXME: Implement this method and return correct value
        int val = _back._val;
        if(_front == _back){
            _front = _back =null;}
        else {
            _back = _back._prev;
            _back._next =null;
        }
        return val;

    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        DNode newwf = new DNode(null, 0, _front);
        DNode newwb = new DNode(_back, 0, null);
        newwf._val = newwf._next._val;
        int val = 0;
            if(index == 0 || index ==-(size())){
                val = _front._val;
                deleteFront();
                return val;
            }
            else if(index == size()-1 || index == -1){
                val = _back._val;
                deleteBack();
                return val;
        }
            else if(size() ==1){
                val = _front._val;
                deleteBack();
                return val;

            }
            else if(index > 0) {

                for(int i = index; i>0; i--){
                    newwf._val = newwf._next._val;
                    newwf._next = newwf._next._next;
            }
                val = newwf._next._val;
                newwf._prev = newwf._next._prev;
                newwf._prev._next = newwf._next._next;

                newwf._next = newwf._next._next;
                newwf._next._prev = newwf._prev;

        }
            else if(index < 0) {

                for(int i = index; i<-1; i++){
                    newwb._val = newwb._prev._val;
                    newwb._prev = newwb._prev._prev;
                }
                val = newwb._prev._val;
                newwb._next = newwb._prev._next;
                newwb._next._prev = newwb._prev._prev;

                newwb._prev = newwb._prev._prev;
                newwb._prev._next=newwb._next;


            }
        return val;
        }
        // FIXME: Implement this method and return correct value


    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        String result = "[";
        if (size() == 0) {
            result += "]";
        } else {
            for (int i = 1; i < size(); i++) {
                result += get(i - 1);
                result += ", ";
            }
            result += get(size()-1);
            result += "]";
        }
        return result;
    }


        // FIXME: Implement this method to return correct value


    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
