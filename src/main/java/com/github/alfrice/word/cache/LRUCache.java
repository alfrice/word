package com.github.alfrice.word.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by alice.martin
 * Developer: alice.martin
 * Date: 12/8/18
 * Time: 12:54 PM
 * Description: In memory self evicting LRU cache
 */
public class LRUCache extends LinkedHashMap{


    final long capacity;
    public LRUCache(int capacity) {

        super(capacity, 1.1f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size()>capacity-1;
    }

}
