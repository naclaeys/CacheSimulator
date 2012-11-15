/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller.instruction;

import cache.Cache;

/**
 *
 * @author naclaeys
 */
public abstract class Instruction {
    
    // description van instructie in trace file
    private String description;
    
    public Instruction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
    
    public abstract long getExecutionTime(Cache cache);
    
}
