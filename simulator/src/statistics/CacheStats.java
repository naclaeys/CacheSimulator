/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

/**
 *
 * @author Nathan
 */
public class CacheStats {
    
    private long cacheHits;
    private boolean hitAdded;
    
    private long coldMiss;
    private boolean coldMissAdded;
    private long conflictMiss;
    private boolean conflictMissAdded;
    
    public CacheStats() {
        cacheHits = 0;
        hitAdded = false;
        coldMiss = 0;
        coldMissAdded = false;
        conflictMiss = 0;
        conflictMissAdded = false;
    }
    
    public long getCacheHits() {
        return cacheHits;
    }

    public long getColdMiss() {
        return coldMiss;
    }

    public long getConflictMiss() {
        return conflictMiss;
    }
    
    public void addCacheHit() {
        cacheHits++;
        hitAdded = true;
    }
    
    public void addColdMiss() {
        coldMiss++;
        coldMissAdded = true;
    }
    
    public void addConflictMiss() {
        conflictMiss++;
        conflictMissAdded = true;
    }
    
    public long getTotalMisses() {
        return coldMiss + conflictMiss;
    }
    
    public void addChangeToStat(CacheStats stat) {
        if(hitAdded) {
            stat.addCacheHit();
            hitAdded = false;
        } else if(coldMissAdded) {
            stat.addColdMiss();
            coldMissAdded = false;
        } else if(conflictMissAdded) {
            stat.addConflictMiss();
            conflictMissAdded = false;
        }
    }

    @Override
    public String toString() {
        return "" + coldMiss + ":" + conflictMiss + ":" + cacheHits;
    }
    
}
