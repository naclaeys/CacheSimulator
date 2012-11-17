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
public class NormalInstruction extends Instruction {

    public NormalInstruction(String description) {
        super(description);
    }

    @Override
    public long getExecutionTime(Cache cache) {
        return 1;
    }
    
}