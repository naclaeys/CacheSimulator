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
public abstract class Cache {
    
    private long cacheHits;
    
    private long coldMiss;
    private long conflictMiss;
    
    public Cache() {
        cacheHits = 0;
        coldMiss = 0;
        conflictMiss = 0;
    }

    public long getCacheHits() {
        return cacheHits;
    }

    protected void setCacheHits(long cacheHits) {
        this.cacheHits = cacheHits;
    }

    public long getColdMiss() {
        return coldMiss;
    }

    protected void setColdMiss(long coldMiss) {
        this.coldMiss = coldMiss;
    }

    public long getConflictMiss() {
        return conflictMiss;
    }

    protected void setConflictMiss(long conflictMiss) {
        this.conflictMiss = conflictMiss;
    }
    
    public void addCacheHit() {
        cacheHits++;
    }
    
    public void addColdMiss() {
        coldMiss++;
    }
    
    public void addConflictMiss() {
        conflictMiss++;
    }
    
    public long getTotalMisses() {
        return coldMiss + conflictMiss;
    }
    
    public abstract long getInstructionTime(MemoryAccess instr) ;
    public abstract void clearCacheMemory();
    

    
    
}
