/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu;

import cache.TwoLayerCache;
import cpu.instruction.InstructionThread;
import inputreader.InstructionInputFileReader;
import java.util.HashSet;
import statistics.Stats;

/**
 *
 * @author naclaeys
 */
public class CPU {
    
    private Stats stats;
    
    private long cycleCount;
    
    private Core[] cores;
    private HashSet<Long> threadsDiscovered;
    
    public CPU(TwoLayerCache[] caches, Stats stats) {
        this.cycleCount = 0;
        this.stats = stats;
        
        this.cores = new Core[caches.length];
        for(int i = 0; i < cores.length; i++) {
            cores[i] = new Core(caches[i]);
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
            stats.addThread(t, min);
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
            
            stats.nextCycle(cycleCount);
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
