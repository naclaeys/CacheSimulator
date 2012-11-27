/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cache_controller.instruction.MemoryAccess;

/**
 *
 * @author Nathan
 */
public class BasicCache extends Cache{
    
    private long time;
    
    private CacheBlock[][] blocks;
    
    public BasicCache(int blockCount) {
        this(blockCount, 1);
    }
    
    public BasicCache(int blockCount, int ways) {
        super();
        time = 0;
        
        blocks = new CacheBlock[(blockCount+ways-1)/ways][ways];
        
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
    
    @Override
    protected boolean isHit(long adress) {
        CacheBlock[] set = blocks[(int)(adress % ((long)blocks.length))];
        
        int j = 0;
        while(j < set.length && set[j].isUsed() && set[j].getAddress() != adress) {
            j++;
        }
        
        return j < set.length && set[j].isUsed();
    }
    
    @Override
    protected void addAddress(long adress) {
        CacheBlock[] set = blocks[(int)(adress % ((long)blocks.length))];
        
        int j = 0;
        while(j < set.length && set[j].isUsed() && set[j].getAddress() != adress) {
            j++;
        }
        
        CacheBlock block;
        if(j >= set.length) {
            block = selectCacheBlockLRU(set);
        } else {
            block = set[j];
        }
        block.setAddress(adress, time);
    }

    @Override
    public long getFetchTime(long adress) {
        time++;
        
        CacheBlock[] set = blocks[(int)(adress % ((long)blocks.length))];

        int j = 0;
        while(j < set.length && set[j].isUsed() && set[j].getAddress() != adress) {
            j++;
        }

        if(j < set.length && set[j].getAddress() == adress) {
            set[j].setTimeStamp(time);
            addCacheHit();
            return HIT_COST;
        } else {
            CacheBlock block;
            if(j == set.length) {
                block = selectCacheBlockLRU(set);
            } else {
                block = set[j];
            }
            block.setAddress(adress, time);
            
            // TODO vervangen door juiste misser
            addColdMiss();
            return MISS_COST;
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
