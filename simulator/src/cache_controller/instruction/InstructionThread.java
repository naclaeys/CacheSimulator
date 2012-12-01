/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller.instruction;

import inputreader.InstructionInputFileReader;

/**
 *
 * @author naclaeys
 */
public class InstructionThread {
    
    private long id;
    private InstructionInputFileReader reader;
    
    private Instruction instruction;
    private long waitingTime;
    
    public InstructionThread(long id, InstructionInputFileReader reader) {
        this.id = id;
        this.reader = reader;
        instruction = null;
        waitingTime = 0;
    }

    public long getId() {
        return id;
    }

    public Instruction setNextInstruction() {
        instruction = reader.getInstructionFromThread(id);
        return instruction;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }
    
    public void decreaseWaitingTime() {
        if(waitingTime > 0) {
            waitingTime--;
        }
    }

    public long getWaitingTime() {
        return waitingTime;
    }
    
}
