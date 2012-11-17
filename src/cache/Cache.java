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
    private long conflictMiss;
    
    public Cache() {
        coldMiss = 0;
        conflictMiss = 0;
    }

    public long getColdMiss() {
        return coldMiss;
    }

    public long getConflictMiss() {
        return conflictMiss;
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

    

    
    
}
