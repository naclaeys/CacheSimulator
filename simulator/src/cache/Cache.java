/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cpu.instruction.Address;
import statistics.CacheStats;

/**
 *
 * @author Nathan
 */
public abstract class Cache {
    
    private CacheStats stats;
    
    public Cache() {
        stats = new CacheStats();
    }

    public CacheStats getStats() {
        return stats;
    }
    
    protected void addCacheHit() {
        stats.addCacheHit();
    }
    
    protected void addColdMiss() {
        stats.addColdMiss();
    }
    
    protected void addConflictMiss() {
        stats.addConflictMiss();
    }
    
    public void print() {
        System.out.println("toegangen:cold misses:conflict misses:hits");
        System.out.println("" + (stats.getTotalMisses()+stats.getCacheHits()) + ":" + stats.toString());
    }
    
    public abstract boolean wasPresent(Address address);
    
    // dit geeft enkel terug of dit in de cache zit of niet, verandert niets aan de cache
    protected abstract boolean isHit(Address adress);
    /**
     * dit zal het resultaat in de cache steken in het geval van een miss, 
     * als het een hit is zal dit de hit operatie van deze cache uitvoeren (bv, nieuwe timestamp voor LRU)
     * @param adress 
     
    protected abstract void addAddress(Address adress);
    */
    /**
     * geeft de tijd terug die de cache nodig heeft om dit adress op te halen,
     * dit voert ook alle operaties uit die verwant zijn aan iets opvragen uit het geheugen 
     *      (adress in cache steken in geval van miss, ...)
     * @param adress
     * @return 
     */
    public abstract long getFetchTime(Address adress);
    // wist alle adressen uit de cache, de cache word terug gebracht naar begin status
    public abstract void clearCacheMemory();
    
}
