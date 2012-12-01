/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cache_controller.instruction.Address;

/**
 *
 * @author Nathan
 */
public class BasicCache extends Cache {
    
    private long time;
    
    private long hitCost;
    private long missCost;
    
    private CacheBlock[][] blocks;
    // aantal adressen dat voorkomt in 1 cache block (grootte van de cache lijn)
    private int blockSize;
    
    public BasicCache(long hitCost, long missCost, int blockCount, int ways, int blockSize) {
        super();
        time = 0;
        this.hitCost = hitCost;
        this.missCost = missCost;
        
        blocks = new CacheBlock[(blockCount+ways-1)/ways][ways];
        this.blockSize = blockSize;
        
        int count = 0;
        int i = 0;
        int j = 0;
        while(i < blocks.length && count < blockCount) {
            j = 0;
            while(j < blocks[i].length && count < blockCount) {
                blocks[i][j] = new CacheBlock();
                count++;
                j++;
            }
            if(count < blockCount) {
                i++;
            }
        }
        
        int k = 0;
        while(j < blocks[i].length) {
            blocks[i][j] = blocks[0][k];
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
    
    private CacheBlock[] getCorrectSet(Address address) {
        return blocks[(int)(getCacheAddress(address).modOf((long)blocks.length))];
    }
    
    private int getSetIndex(CacheBlock[] set, Address address) {
        int j = 0;
        while(j < set.length && set[j].isUsed() && !set[j].getAddress().equals(getCacheAddress(address))) {
            j++;
        }
        return j;
    }
    
    @Override
    protected boolean isHit(Address address) {
        CacheBlock[] set = getCorrectSet(address);
        int index = getSetIndex(set, address);
        return index < set.length && set[index].isUsed();
    }
    
    @Override
    protected void addAddress(Address address) {
        Address cacheAddress = getCacheAddress(address);
        CacheBlock[] set = getCorrectSet(address);
        int index = getSetIndex(set, address);
        
        CacheBlock block;
        if(index >= set.length) {
            block = selectCacheBlockLRU(set);
        } else {
            block = set[index];
        }
        block.setAddress(cacheAddress, time);
    }

    @Override
    public long getFetchTime(Address address) {
        time++;
        
        Address cacheAddress = getCacheAddress(address);
        CacheBlock[] set = getCorrectSet(address);
        int index = getSetIndex(set, address);
        
        if(index < set.length && set[index].getAddress().equals(cacheAddress)) {
            set[index].setTimeStamp(time);
            addCacheHit();
            return hitCost;
        } else {
            CacheBlock block;
            if(index >= set.length) {
                block = selectCacheBlockLRU(set);
            } else {
                block = set[index];
            }
            block.setAddress(cacheAddress, time);
            
            // TODO vervangen door juiste misser
            addColdMiss();
            return missCost;
        }
    }
    
    private CacheBlock selectCacheBlockLRU(CacheBlock[] set) {
        int oldestIndex = 0;
        for(int i = 1; i < set.length; i++) {
            if(set[oldestIndex].getTimeStamp() > set[i].getTimeStamp()) {
                oldestIndex = i;
            }
        }
        return set[oldestIndex];
    }

    @Override
    public void clearCacheMemory() {
        for(int i = 0; i < blocks.length; i++) {
            for(int j = 0; j < blocks[i].length; j++) {
                blocks[i][j].clear();
            }
        }
    }
    
}
