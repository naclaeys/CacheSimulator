/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

import cache.Cache;
import cpu.instruction.InstructionThread;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author Nathan
 */
public class Stats {
    
    private ArrayList<StatThread> threads;
    private long addressBlockSize;
    
    private Cache[] caches;
    
    private HashMap<Long, AddressBlock> addressBlocks;
    
    public Stats(long addressBlockSize, Cache[] caches) {
        this.addressBlockSize = addressBlockSize;
        this.caches = caches;
        
        threads = new ArrayList<>();
        addressBlocks = new HashMap<>();
    }
    
    public void addThread(InstructionThread thread, int coreIndex) {
        threads.add(new StatThread(caches[coreIndex], thread));
    }
    
    public void nextCycle(long currentCycle) {
        for(StatThread thread: threads) {
            if(thread.wasActive()) {
                Long address = thread.getAddressIndex(addressBlockSize);
                if(!addressBlocks.containsKey(address)) {
                    addressBlocks.put(address, new AddressBlock(address));
                }
            
                AddressBlock block = addressBlocks.get(address);
                thread.getPrevBlock().addNext(block);
                thread.setPrevBlock(block);
                
                thread.getCache().getStats().addChangeToStat(block.getStats());
            }
        }
    }
    
    public void print() {
        for(AddressBlock block: addressBlocks.values()) {
            System.out.println(block.toString());
        }
    }
    
}
