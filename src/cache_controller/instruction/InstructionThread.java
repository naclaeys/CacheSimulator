/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller.instruction;

import java.util.LinkedList;

/**
 *
 * @author naclaeys
 */
public class InstructionThread implements Comparable<InstructionThread> {
    
    private int id;
    private LinkedList<Instruction> instructions;

    public InstructionThread(int id) {
        instructions = new LinkedList<Instruction>();
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LinkedList<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(LinkedList<Instruction> instructions) {
        this.instructions = instructions;
    }

    @Override
    public int compareTo(InstructionThread o) {
        if(id < o.getId()) {
            return -1;
        } else if(id == o.getId()) {
            return 0;
        } else {
            return 1;
        }
    }
    
}
