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
    
    private long coldMiss;
    private long capacityMiss;
    private long conflictMiss;
    
    public Cache() {
        coldMiss = 0;
        capacityMiss = 0;
        conflictMiss = 0;
    }
    
    public long getTotalMisses() {
        return coldMiss + capacityMiss + conflictMiss;
    }

    public long getColdMiss() {
        return coldMiss;
    }

    public long getCapacityMiss() {
        return capacityMiss;
    }

    public long getConflictMiss() {
        return conflictMiss;
    }
    
    public void addColdMiss() {
        coldMiss++;
    }
    
    public void addCapacityMiss() {
        capacityMiss++;
    }
    
    public void addConflictMiss() {
        conflictMiss++;
    }
    
    public abstract long getInstructionTime(MemoryAccess instr) ;

    
    
}
