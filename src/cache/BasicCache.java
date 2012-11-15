/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cache_controller.instruction.MemoryAccess;
import java.io.File;

/**
 *
 * @author Nathan
 */
public class BasicCache extends Cache{
    
    public static final int HIT_COST = 1;
    public static final int MISS_COST = 10;
    
    private long time;
    
    private int ways;
    private int blockCount;
    
    private CacheBlock[][] blocks;
    
    public BasicCache(File config) {
        super();
        time = 0;
        
        ways = 1;
        blockCount = 1;
        
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
    public long getInstructionTime(MemoryAccess instr) {
        int timeSum = 0;
        
        for(int i = 0; i < instr.getAdress().length; i++) {
            int adress = instr.getAdress()[i];
            CacheBlock[] set = blocks[adress % blocks.length];
            
            int j = 0;
            while(j < set.length && set[j].isUsed() && set[j].getAddress() != adress) {
                j++;
            }
            
            if(j < set.length && set[j].getAddress() == adress) {
                set[j].setTimeStamp(time);
                
                timeSum += HIT_COST;
            } else {
                CacheBlock block;
                if(j == set.length) {
                    block = selectCacheBlockLRU(set);
                } else {
                    block = set[j];
                }
                block.setAddress(adress, time);
                
                timeSum += MISS_COST;
                // TODO vervangen door juiste misser
                addColdMiss();
            }
        }
        
        time++;
        return timeSum;
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
    
}
