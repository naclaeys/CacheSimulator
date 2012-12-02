/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller;

import cache.Cache;
import cache.TwoLayerCache;
import cache_controller.instruction.Instruction;
import cache_controller.instruction.InstructionThread;
import inputreader.InstructionInputFileReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Nathan
 */
public class Core {

    private TwoLayerCache cache;
    
    private long previousCacheColdMiss1;
    private long previousCacheConflictMiss1;
    private long previousCacheHits1;
    private long previousCacheColdMiss2;
    private long previousCacheConflictMiss2;
    private long previousCacheHits2;
    
    private int index;
    private LinkedList<InstructionThread> threads;
    
    public Core(TwoLayerCache cache) {
        this.cache = cache;
        
        previousCacheColdMiss1 = 0;
        previousCacheConflictMiss1 = 0;
        previousCacheHits1 = 0;
        previousCacheColdMiss2 = 0;
        previousCacheConflictMiss2 = 0;
        previousCacheHits2 = 0;
        
        index = 0;
        threads = new LinkedList<>();
    }
    
    private void increaseIndex() {
        index++;
        if(index == threads.size()) {
            index = 0;
        }
    }
    
    private InstructionThread getExecutingThread() {
        InstructionThread chosenThread = null;
        
        Iterator<InstructionThread> it = threads.listIterator(index);
        int count = 0;
        while(it.hasNext() && chosenThread == null) {
            increaseIndex();
            count++;
            InstructionThread thread = it.next();
            if(thread.getWaitingTime() == 0) {
                chosenThread = thread;
            }
        }
        if(chosenThread == null && count < threads.size()) {
            it = threads.listIterator(index);
            while(it.hasNext() && chosenThread == null) {
                increaseIndex();
                InstructionThread thread = it.next();
                if(thread.getWaitingTime() == 0) {
                    chosenThread = thread;
                }
            }
        }
        
        return chosenThread;
    }
    
    public void execute() {
        if(!threads.isEmpty()) {
            for(InstructionThread thread: threads) {
                thread.decreaseWaitingTime();
            }
            
            InstructionThread thread = getExecutingThread();
            
            if(thread != null) {
                thread.setNextInstruction();
                Instruction instr = thread.getInstruction();
                if(instr == null) {
                    // geen instructies over, thread is klaar
                    threads.remove(thread);
                } else {
                    thread.setWaitingTime(instr.getExecutionTime(cache));
                }
            }
        }        
    }
    
    public void addThread(long thread, InstructionInputFileReader reader) {
        threads.add(index, new InstructionThread(thread, reader));
    }
    
    public int getThreadCount() {
        return threads.size();
    }
    
    public String print(long id) {
        long coldMissDiff1 = cache.getLayer1().getColdMiss() - previousCacheColdMiss1;
        long conflictMissDiff1 = cache.getLayer1().getConflictMiss() - previousCacheConflictMiss1;
        long hitDiff1 = cache.getLayer1().getCacheHits() - previousCacheHits1;
        
        long coldMissDiff2 = cache.getLayer2().getColdMiss() - previousCacheColdMiss2;
        long conflictMissDiff2 = cache.getLayer2().getConflictMiss() - previousCacheConflictMiss2;
        long hitDiff2 = cache.getLayer2().getCacheHits() - previousCacheHits2;
        
        String print = "";
        if(coldMissDiff1 != 0 || conflictMissDiff1 != 0 || hitDiff1 != 0 
                || coldMissDiff2 != 0 || conflictMissDiff2 != 0 || hitDiff2 != 0) {
            print = "" + id + " " + coldMissDiff1 + " " + conflictMissDiff1 + " " + hitDiff1 
                    + " " + coldMissDiff2 + " " + conflictMissDiff2 + " " + hitDiff2;
            Iterator<InstructionThread> it = threads.iterator();
            while(it.hasNext()) {
                Instruction instr = it.next().getInstruction();
                if(instr != null) {
                    print += " " + instr.getInstructionAdress();
                }
            }
        }
        
        previousCacheColdMiss1 = cache.getLayer1().getColdMiss();
        previousCacheConflictMiss1 = cache.getLayer1().getConflictMiss();
        previousCacheHits1 = cache.getLayer1().getCacheHits();
        
        previousCacheColdMiss2 = cache.getLayer2().getColdMiss();
        previousCacheConflictMiss2 = cache.getLayer2().getConflictMiss();
        previousCacheHits2 = cache.getLayer2().getCacheHits();
        
        return print;
    }
    
}
