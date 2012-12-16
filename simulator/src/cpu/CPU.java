/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu;

import cache.TwoLayerCache;
import configuration.Optimizer;
import cpu.instruction.InstructionThread;
import inputreader.InstructionInputFileReader;
import java.util.HashSet;
import statistics.Stats;

/**
 *
 * @author naclaeys
 */
public class CPU {
    
    private long cycleCount;
    
    private Core[] cores;
    private HashSet<Long> threadsDiscovered;
    
    public CPU(TwoLayerCache[] caches, Optimizer[] optimizers, Stats stats) {
        this.cycleCount = 0;
        
        this.cores = new Core[caches.length];
        for(int i = 0; i < cores.length; i++) {
            cores[i] = new Core(i, caches[i], optimizers[i], stats);
        }
        threadsDiscovered = new HashSet<>();
    }

    public long getCycleCount() {
        return cycleCount;
    }

    public HashSet<Long> getThreadsDiscovered() {
        return threadsDiscovered;
    }
    
    public void addThread(long thread, InstructionInputFileReader reader) {
        if(!threadsDiscovered.contains(thread)) {
            threadsDiscovered.add(thread);
            
            int min = 0;
            for(int i = 1; i < cores.length; i++) {
                if(cores[i].getThreadCount() < cores[min].getThreadCount()) {
                    min = i;
                }
            }
            InstructionThread t = new InstructionThread(thread, reader);
            cores[min].addThread(t);
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
                done &= cores[i].getThreadCount() == 0;
            }
            
            cycleCount++;
            /*
            jumpIndex ++;
            if(jumpIndex >= linePrintMark) {
                jumpIndex = 0;
                String print = "";
                for(int i = 0; i < cores.length; i++) {
                    print += cores[i].print(i, cycleCount);                    
                }
                System.out.print(print);
            }*/
        }
    }
    
}
