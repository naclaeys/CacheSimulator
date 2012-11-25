/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cache_controller.instruction.MemoryAccess;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

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
        fireStateChanged();
    }
    
    public void addColdMiss() {
        coldMiss++;
        fireStateChanged();
    }
    
    public void addConflictMiss() {
        conflictMiss++;
        fireStateChanged();
    }
    
    public long getTotalMisses() {
        return coldMiss + conflictMiss;
    }
    
    public abstract long getInstructionTime(MemoryAccess instr) ;
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
