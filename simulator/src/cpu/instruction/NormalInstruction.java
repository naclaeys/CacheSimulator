/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu.instruction;

import cache.Cache;

/**
 *
 * @author naclaeys
 */
public class NormalInstruction extends Instruction {

    private long amount;
    
    public NormalInstruction(String description, long thread, String instructionAdress) {
        this(description, thread, instructionAdress, 1);
    }
    
    public NormalInstruction(String description, long thread, String instructionAdress, long amount) {
        super(description, thread, instructionAdress);
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }
    
    @Override
    public long getExecutionTime(Cache cache) {
        return amount;
    }
    
}
