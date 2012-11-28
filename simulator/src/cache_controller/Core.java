/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller;

import cache.Cache;
import cache_controller.instruction.Instruction;
import cache_controller.instruction.InstructionThread;
import inputreader.InstructionInputFileReader;
import java.util.LinkedList;

/**
 *
 * @author Nathan
 */
public class Core {

    private Cache cache;
    
    private long previousCacheMiss;
    private long previousCacheHits;
    
    private LinkedList<InstructionThread> threads;
    private InstructionThread currentThread;
    
    public Core(Cache cache) {
        this.cache = cache;
        
        previousCacheMiss = 0;
        previousCacheHits = 0;
        
        threads = new LinkedList<>();
    }
    
    private void selectCurrentThread() {
        boolean chosen = false;
        InstructionThread thread = null;

        for(int i = 0; i < getThreadCount(); i++) {
            thread = threads.pop();
            threads.addLast(thread);

            thread.decreaseWaitingTime();
            if(thread.getWaitingTime() == 0 && !chosen) {
                currentThread = thread;
                chosen = true;
            }
        }

        do {
            thread = threads.pop();
            threads.addLast(thread);
        } while(thread.getId() != currentThread.getId());
    }
    
    public void execute() {
        if(!threads.isEmpty()) {
            selectCurrentThread();
            
            currentThread.setNextInstruction();
            Instruction instr = currentThread.getInstruction();
            if(instr == null) {
                // geen instructies over, thread is klaar
                threads.remove(currentThread);
            } else {
                currentThread.setWaitingTime(instr.getExecutionTime(cache));
            }
        }        
    }
    
    public void addThread(int thread, InstructionInputFileReader reader) {
        threads.addFirst(new InstructionThread(thread, reader));
    }
    
    public int getThreadCount() {
        return threads.size();
    }
    
    public void print(int id) {
        //System.out.println("" + id + " " + instruction.getInstructionAdress());
        System.out.println("" + id + " " + (cache.getTotalMisses() - previousCacheMiss));
        System.out.println("" + id + " " + (cache.getCacheHits() - previousCacheHits));
        
        previousCacheMiss = cache.getTotalMisses();
        previousCacheHits = cache.getCacheHits();
    }
    
}
