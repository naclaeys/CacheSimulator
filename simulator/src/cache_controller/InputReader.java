/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller;

import cache_controller.instruction.Instruction;

/**
 *
 * @author naclaeys
 */
public interface InputReader {
    
    public Instruction getInstructionFromThread(int thread);
    
}
