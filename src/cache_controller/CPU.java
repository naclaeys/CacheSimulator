/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller;

import cache.Cache;
import cache_controller.instruction.Instruction;
import cache_controller.instruction.InstructionThread;
import main.GCASimulator;

/**
 *
 * @author naclaeys
 */
public class CPU {
    
    private long cycleCount;

    private InputReader instructionReader;
    private InstructionThread[] threads;
    private long[] waiting;
    
    private Cache cache;

    public CPU(InputReader instructionReader, Cache cache, int threadCount) {
        this.cycleCount = 0;
        this.cache = cache;
        this.instructionReader = instructionReader;
        this.threads = new InstructionThread[threadCount];
        this.waiting = new long[threadCount];
        for(int i = 0; i < threadCount; i++) {
            threads[i] = new InstructionThread(i);
            waiting[i] = 0;
        }
    }

    public long getCycleCount() {
        return cycleCount;
    }

    public void start() {
        boolean done = false;
        boolean readingLeft = true;
        
        // cyclus
        while(!done) {
            if(readingLeft) {
                for(int i = 0; i < threads.length; i++) {
                    InstructionThread thread = threads[i];

                    while(thread.getInstructions().isEmpty() && readingLeft) {
                        readingLeft = !instructionReader.addInstructionsToThreads(GCASimulator.READING_AMOUNT, threads);
                    }
                }
            }
            done = !readingLeft;
            
            for(int i = 0; i < threads.length; i++) {
                InstructionThread thread = threads[i];
                
                if(!thread.getInstructions().isEmpty() && waiting[i] <= 0) {
                    Instruction instr = thread.getInstructions().removeFirst();
                    
                    waiting[i] += instr.getExecutionTime(cache);
                }
                waiting[i]--;
                done &= thread.getInstructions().isEmpty() && waiting[i] <= 0;
            }
            
            cycleCount++;
        }
    }
    
}
