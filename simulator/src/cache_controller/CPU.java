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
    
    public static final long JUMP = 50;
    
    private long cycleCount;

    private int threadCount;
    private InputReader[] instructionReader;
    private long[] waiting;
    private boolean[] finished;
    
    private Cache[] caches;

    public CPU(File input, Cache[] caches, int threadCount) {
        this.cycleCount = 0;
        this.caches = caches;
        
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
        long jumpIndex = 0;
        long previousCacheMiss[] = new long[threadCount];
        long previousCacheHits[] = new long[threadCount];
        Instruction[] instructions = new Instruction[threadCount];
        
        // cyclus
        while(!done) {
            done = true;
            
            for(int i = 0; i < threadCount; i++) {
                if(!finished[i] && waiting[i] <= 0) {
                    instructions[i] = instructionReader[i].getInstructionFromThread(i);
                    
                    finished[i] = instructions[i] == null;
                    if(!finished[i]) {
                        waiting[i] += instructions[i].getExecutionTime(caches[i]);
                    }
                }
                waiting[i]--;
                done &= finished[i] && waiting[i] <= 0;
            }
            
            cycleCount++;
            jumpIndex ++;
            if(jumpIndex == JUMP) {
                jumpIndex = 0;
                
                for(int i = 0; i < threadCount; i++) {
                    previousCacheMiss[i] = caches[i].getTotalMisses();
                    previousCacheHits[i] = caches[i].getCacheHits();
                    
                    System.out.println("" + i + " " + instructions[i].getInstructionAdress());
                    System.out.println("" + i + " " + (caches[i].getTotalMisses() - previousCacheMiss[i]));
                    System.out.println("" + i + " " + (caches[i].getCacheHits() - previousCacheHits[i]));
                    
                }
            }
        }
    }
    
}
