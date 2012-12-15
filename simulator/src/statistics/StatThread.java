/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

import cache.Cache;
import cpu.instruction.Instruction;
import cpu.instruction.InstructionThread;

/**
 *
 * @author Nathan
 */
public class StatThread {
    
    private Cache cache;
    
    private InstructionThread thread;
    
    private AddressBlock prevBlock;
    private Instruction prevInstruction;

    public StatThread(Cache cache, InstructionThread thread) {
        this.cache = cache;
        this.thread = thread;
        prevBlock = new AddressBlock(0);
    }

    public Cache getCache() {
        return cache;
    }

    public AddressBlock getPrevBlock() {
        return prevBlock;
    }

    public void setPrevBlock(AddressBlock prevBlock) {
        this.prevBlock = prevBlock;
    }

    public Instruction getPrevInstruction() {
        return prevInstruction;
    }
    
    public long getAddressIndex(long addressBlockSize) {
        return thread.getInstruction().getInstructionAdress().divideBy(addressBlockSize);
    }
    
    public boolean wasActive() {
        return thread.getInstruction() != null && prevInstruction != thread.getInstruction();
    }
    
    public void update() {
        prevInstruction = thread.getInstruction();
    }
}
