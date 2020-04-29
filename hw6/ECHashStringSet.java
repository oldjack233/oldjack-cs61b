import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author ziyuan tang
 */

/**
 * EC本质上是一个hashing的StringSet
 * 也即是说，用bucket + linklist组成的一个储存数据的结构
 * 用size/bucket.length 判定overload
 * linklist可以直接add
 * index用s.hashcode（）获取，注意转正
 * resize的话，double bucket的length，重新put
 * contains的话，可以直接用linklist的contains进行判定
 * arraylist可以直接addall linklist
 */


class ECHashStringSet implements StringSet{
    private int _size;
    private LinkedList<String>[] _buckets;

    ECHashStringSet(){
        _size = 0;
        double mini_load = 0.2;
        _buckets = new LinkedList[((int)(1/mini_load))];
    }

    @Override
    public void put(String s){
        int max_load = 5;
        if (_size/_buckets.length > max_load) {
           resize();
        }
        _size++;
        int index = s.hashCode()& 0x7fffffff % _buckets.length;
        if (_buckets[index] == null) {
            _buckets[index] = new LinkedList<>();
        }
        _buckets[index].add(s);
    }

    public void resize() {
        LinkedList<String>[] old_buckets = _buckets;
        _buckets = new LinkedList[2*_buckets.length];
        _size = 0;
        for (LinkedList<String> linklist : old_buckets) {
            if (linklist != null) {
                for (String item : linklist) {
                    put(item);
                }
            }
        }
    }

    @Override
    public boolean contains(String s){
        if (s != null) {
            int index = s.hashCode()& 0x7fffffff % _buckets.length;
            if (_buckets[index] != null) {
                return _buckets[index].contains(s);
            }
        }
        return false;
    }

    @Override
    public List<String> asList(){
        ArrayList<String> list = new ArrayList<>();
        for (LinkedList linklist : _buckets) {
            if (linklist != null) {
                list.addAll(linklist);
            }
        }
        return list;
    }
}