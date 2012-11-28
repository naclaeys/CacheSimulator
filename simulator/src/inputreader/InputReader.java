/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputreader;

import cache_controller.instruction.Instruction;

/**
 *
 * @author naclaeys
 */
public interface InputReader {
    
    public Instruction getInstructionFromThread(long thread);
    
}
