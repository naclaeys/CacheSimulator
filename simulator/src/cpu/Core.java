/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu;

import cache.TwoLayerCache;
import configuration.Optimizer;
import cpu.instruction.Instruction;
import cpu.instruction.InstructionThread;
import java.util.Iterator;
import java.util.LinkedList;
import statistics.Stats;

/**
 *
 * @author Nathan
 */
public class Core {
    
    private int coreId;
    
    private Stats stats;
    private Optimizer optimizer;
    
    private TwoLayerCache cache;
    private int index;
    private LinkedList<InstructionThread> threads;
    
    public Core(int id, TwoLayerCache cache, Optimizer opt, Stats stats) {
        this.coreId = id;
        this.cache = cache;
        this.stats = stats;
        this.optimizer = opt;
        
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
        int previousIndex = index;
        
        Iterator<InstructionThread> it = threads.listIterator(index);
        while(it.hasNext() && chosenThread == null) {
            increaseIndex();
            InstructionThread thread = it.next();
            if(thread.getWaitingTime() == 0) {
                chosenThread = thread;
            }
        }
        if(chosenThread == null && index == 0) {
            it = threads.listIterator(index);
            while(it.hasNext() && chosenThread == null && index < previousIndex) {
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
                
                optimizer.check(thread, coreId);
                
                if(instr == null) {
                    // geen instructies over, thread is klaar
                    int threadIndex = threads.indexOf(thread);
                    if(threadIndex < index) {
                        index--;
                    } else if(threadIndex == index) {
                        increaseIndex();
                    }
                    threads.remove(thread);
                } else {
                    thread.setWaitingTime(instr.getExecutionTime(cache));
                    
                    stats.threadAction(thread, cache);
                }
            }
        }        
    }
    
    public void addThread(InstructionThread thread) {
        threads.add(index, thread);
        optimizer.addThread(thread);
    }
    
    public int getThreadCount() {
        return threads.size();
    }
    
    public Optimizer getOptimizer() {
        return optimizer;
    }
        
}
