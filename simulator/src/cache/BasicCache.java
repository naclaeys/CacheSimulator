/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cache_controller.instruction.Address;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Nathan
 */
public class BasicCache extends Cache {
    
    private long time;
    
    private long hitCost;
    private long missCost;
    
    private int ways;
    private HashSet<Address> seen;
    private ArrayList<HashMap<Address, CacheBlock>> blocks;
    // aantal adressen dat voorkomt in 1 cache block (grootte van de cache lijn)
    private int blockSize;
    
    public BasicCache(long hitCost, long missCost, int blockCount, int ways, int blockSize) {
        super();
        time = 0;
        this.hitCost = hitCost;
        this.missCost = missCost;
        
        this.ways = ways;
        this.blockSize = blockSize;
        seen = new HashSet<>();
        int size = (blockCount+ways-1)/ways;
        blocks = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            blocks.add(new HashMap<Address, CacheBlock>((int)(ways/0.75)+1));
        }
    }

    public long getHitCost() {
        return hitCost;
    }

    public long getMissCost() {
        return missCost;
    }
    
    private Address getCacheAddress(Address address) {
        return new Address("" + address.divideBy((long)blockSize));
    }
    
    private HashMap<Address, CacheBlock> getCorrectMap(Address cacheAddress) {
        return blocks.get((int)(cacheAddress.modOf((long)blocks.size())));
    }
    
    @Override
    protected boolean isHit(Address address) {
        Address cacheAddress = getCacheAddress(address);
        HashMap<Address, CacheBlock> map = getCorrectMap(cacheAddress);
        return map.containsKey(cacheAddress);
    }

    @Override
    public long getFetchTime(Address address) {
        time++;
        
        Address cacheAddress = getCacheAddress(address);
        HashMap<Address, CacheBlock> map = getCorrectMap(cacheAddress);
        
        if(map.containsKey(cacheAddress)) {
            map.get(cacheAddress).setTimeStamp(time);
            addCacheHit();
            return hitCost;
        } else {
            CacheBlock block;
            if(map.size() == ways) {
                block = selectCacheBlockLRU(map);
                map.remove(block.getAddress());
            } else {
                block = new CacheBlock();
            }
            block.setAddress(cacheAddress, time);
            map.put(cacheAddress, block);
            
            if(seen.contains(cacheAddress)) {
                addConflictMiss();
            } else {
                seen.add(cacheAddress);
                addColdMiss();
            }
            return missCost;
        }
    }
    
    private CacheBlock selectCacheBlockLRU(HashMap<Address, CacheBlock> map) {
        Collection<CacheBlock> values = map.values();
        CacheBlock oldest = null;
        for(CacheBlock block: values) {
            if(oldest == null || oldest.getTimeStamp() > block.getTimeStamp()) {
                oldest = block;
            }
        }
        return oldest;
    }

    @Override
    public void clearCacheMemory() {
        for(HashMap map: blocks) {
            map.clear();
        }
    }
    
}
