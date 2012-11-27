/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Nathan
 */
public abstract class Cache {
    
    public static final long HIT_COST = 1;
    public static final long MISS_COST = 10;
    
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
    
    protected void addCacheHit() {
        cacheHits++;
        fireStateChanged();
    }
    
    protected void addColdMiss() {
        coldMiss++;
        fireStateChanged();
    }
    
    protected void addConflictMiss() {
        conflictMiss++;
        fireStateChanged();
    }
    
    public long getTotalMisses() {
        return coldMiss + conflictMiss;
    }
    
    public void printStats() {
        System.out.println("cache toegangen: " + (getTotalMisses() + getCacheHits()));
        System.out.println("cache missers: " + getTotalMisses());
        System.out.println("cache hits: " + getCacheHits());
    }
    
    // dit geeft enkel terug of dit in de cache zit of niet, verandert niets aan de cache
    protected abstract boolean isHit(long adress);
    /**
     * dit zal het resultaat in de cache steken in het geval van een miss, 
     * als het een hit is zal dit de hit operatie van deze cache uitvoeren (bv, nieuwe timestamp voor LRU)
     * @param adress 
     */
    protected abstract void addAddress(long adress);
    
    /**
     * geeft de tijd terug die de cache nodig heeft om dit adress op te halen,
     * dit voert ook alle operaties uit die verwant zijn aan iets opvragen uit het geheugen 
     *      (adress in cache steken in geval van miss, ...)
     * @param adress
     * @return 
     */
    public abstract long getFetchTime(long adress);
    // wist alle adressen uit de cache, de cache word terug gebracht naar begin status
    public abstract void clearCacheMemory();
    
    EventListenerList listenerList = new EventListenerList();
    ChangeEvent changeEvent = null;

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
        fireStateChanged();
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /*
     * Notify all listeners that have registered interest for
     * notification on this event type.  The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     */
    protected void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener)listeners[i+1]).stateChanged(changeEvent);
            }
        }
    }
}
