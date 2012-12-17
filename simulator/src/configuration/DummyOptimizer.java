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
public class DummyOptimizer extends Optimizer {


    @Override
    public void check(InstructionThread thread, int core) {}

    @Override
    public void addThread(InstructionThread thread) {}

    @Override
    public long getReconfigCount() {
        return 0;
    }
    
}
