/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller;

import cache.Cache;
import cache_controller.instruction.Instruction;
import java.io.File;

/**
 *
 * @author naclaeys
 */
public class CPU {
    
    private long cycleCount;

    private int threadCount;
    private InputReader[] instructionReader;
    private long[] waiting;
    private boolean[] finished;
    
    private Cache cache;

    public CPU(File input, Cache cache, int threadCount) {
        this.cycleCount = 0;
        this.cache = cache;
        
        this.threadCount = threadCount;
        this.instructionReader = new InstructionInputFileReader[threadCount];
        this.waiting = new long[threadCount];
        this.finished = new boolean[threadCount];
        for(int i = 0; i < threadCount; i++) {
            instructionReader[i] = new InstructionInputFileReader(input);
            waiting[i] = 0;
            finished[i] = false;
        }
    }

    public long getCycleCount() {
        return cycleCount;
    }

    public void start() {
        boolean done = false;
        
        // cyclus
        while(!done) {
            done = true;
            for(int i = 0; i < threadCount; i++) {
                if(!finished[i] && waiting[i] <= 0) {
                    Instruction instr = instructionReader[i].getInstructionFromThread(i);
                    
                    finished[i] = instr == null;
                    if(!finished[i]) {
                        waiting[i] += instr.getExecutionTime(cache);
                    }
                }
                waiting[i]--;
                done &= finished[i] && waiting[i] <= 0;
            }
            
            cycleCount++;
        }
    }
    
}
