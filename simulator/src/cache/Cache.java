/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cpu.instruction.Address;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
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
        fireStateChanged();
    }
    
    protected void addColdMiss() {
        stats.addColdMiss();
        fireStateChanged();
    }
    
    protected void addConflictMiss() {
        stats.addConflictMiss();
        fireStateChanged();
    }
    
    public void print() {
        System.out.println("toegangen:cold misses:conflict misses:hits");
        System.out.println("" + (stats.getTotalMisses()+stats.getCacheHits()) + ":" + stats.toString());
    }
    
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
