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
public abstract class Instruction {
    
    // description van instructie in trace file
    private String description;
    
    private long thread;
    private Address instructionAdress;
    
    public Instruction(String description, long thread, String instructionAdress) {
        this.description = description;
        this.thread = thread;
        this.instructionAdress = new Address(instructionAdress);
    }

    public String getDescription() {
        return description;
    }

    public long getThread() {
        return thread;
    }

    public Address getInstructionAdress() {
        return instructionAdress;
    }
    
    public abstract long getExecutionTime(Cache cache);
}
