/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import cpu.instruction.InstructionThread;

/**
 *
 * @author Nathan
 */
public abstract class Optimizer {
    
    public abstract void check(InstructionThread thread, int core);
    public abstract void addThread(InstructionThread thread);
    public abstract long getReconfigCount();
}
