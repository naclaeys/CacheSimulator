/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cache_controller.instruction.Address;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Nathan
 */
public class BasicCache extends Cache {
    
    private long time;
    
    private long hitCost;
    private long missCost;
    
    private int ways;
    private ArrayList<HashMap<Address, CacheBlock>> blocks;
    private CacheBlock[][] cacheBlocks;
    // aantal adressen dat voorkomt in 1 cache block (grootte van de cache lijn)
    private int blockSize;
    
    public BasicCache(long hitCost, long missCost, int blockCount, int ways, int blockSize) {
        super();
        time = 0;
        this.hitCost = hitCost;
        this.missCost = missCost;
        
        this.ways = ways;
        int size = (blockCount+ways-1)/ways;
        blocks = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            blocks.add(new HashMap<Address, CacheBlock>((int)(ways/0.75)+1));
        }
        cacheBlocks = new CacheBlock[size][ways];
        this.blockSize = blockSize;
        
        int count = 0;
        int i = 0;
        int j = 0;
        while(i < cacheBlocks.length && count < blockCount) {
            j = 0;
            while(j < cacheBlocks[i].length && count < blockCount) {
                cacheBlocks[i][j] = new CacheBlock();
                count++;
                j++;
            }
            if(count < blockCount) {
                i++;
            }
        }
        
        int k = 0;
        while(j < cacheBlocks[i].length) {
            cacheBlocks[i][j] = cacheBlocks[0][k];
            k++;
            j++;
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
            } else {
                block = new CacheBlock();
                map.put(cacheAddress, block);
            }
            block.setAddress(cacheAddress, time);
            
            // TODO vervangen door juiste misser
            addColdMiss();
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
        for(int i = 0; i < cacheBlocks.length; i++) {
            for(int j = 0; j < cacheBlocks[i].length; j++) {
                cacheBlocks[i][j].clear();
            }
        }
    }
    
}
