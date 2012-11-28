/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller;

import cache.Cache;
import cache_controller.instruction.Instruction;
import inputreader.InstructionInputFileReader;
import java.io.File;
import java.util.HashSet;

/**
 *
 * @author naclaeys
 */
public class CPU {
    
    public static final long JUMP = 50;
    
    private long cycleCount;
    
    private File input;
    
    private Core[] cores;
    private HashSet<Integer> threadsDiscovered;
    
    public CPU(File input, Cache[] caches) {
        this.cycleCount = 0;
        this.input = input;
        
        this.cores = new Core[caches.length];
        for(int i = 0; i < cores.length; i++) {
            cores[i] = new Core(caches[i]);
        }
        threadsDiscovered = new HashSet<>();
    }

    public long getCycleCount() {
        return cycleCount;
    }
    
    public void addThread(int thread) {
        if(!threadsDiscovered.contains(thread)) {
            threadsDiscovered.add(thread);
            
            int min = 0;
            for(int i = 1; i < cores.length; i++) {
                if(cores[i].getThreadCount() < cores[min].getThreadCount()) {
                    min = i;
                }
            }
            cores[min].addThread(thread, new InstructionInputFileReader(input, this));
        }
    }

    public void start() {
        boolean done = false;
        long jumpIndex = 0;
        
        // cyclus
        while(!done) {
            done = true;
            
            for(int i = 0; i < cores.length; i++) {
                if(cores[i].getThreadCount() > 0) {
                    cores[i].execute();
                }
                done &= cores[i].getThreadCount() > 0;
            }
            
            cycleCount++;
            jumpIndex ++;
            if(jumpIndex == JUMP) {
                jumpIndex = 0;
                for(int i = 0; i < cores.length; i++) {
                    cores[i].print(i);
                }
            }
        }
    }
    
}
