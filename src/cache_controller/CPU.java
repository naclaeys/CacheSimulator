/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller;

import cache.Cache;
import cache_controller.instruction.Instruction;

/**
 *
 * @author naclaeys
 */
public class CPU {
    
    private long cycleCount;

    private InputReader instructionReader;
    private int threadCount;
    private long[] waiting;
    private boolean[] finished;
    
    private Cache cache;

    public CPU(InputReader instructionReader, Cache cache, int threadCount) {
        this.cycleCount = 0;
        this.cache = cache;
        this.instructionReader = instructionReader;
        
        this.threadCount = threadCount;
        this.waiting = new long[threadCount];
        this.finished = new boolean[threadCount];
        for(int i = 0; i < threadCount; i++) {
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
                    Instruction instr = instructionReader.getInstructionFromThread(i);
                    
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
