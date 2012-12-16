/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

import cache.Cache;
import cpu.instruction.InstructionThread;
import java.util.HashMap;


/**
 *
 * @author Nathan
 */
public class Stats {
    
    private long addressBlockSize;
    
    private HashMap<Long, AddressBlock> addressBlocks;
    
    public Stats(long addressBlockSize) {
        this.addressBlockSize = addressBlockSize;
        
        addressBlocks = new HashMap<>();
    }

    public HashMap<Long, AddressBlock> getAddressBlocks() {
        return addressBlocks;
    }
    
    public void threadAction(InstructionThread thread, Cache cache) {
        long address = thread.getAddressIndex(addressBlockSize);
        if(!addressBlocks.containsKey(address)) {
            addressBlocks.put(address, new AddressBlock(address));
        }

        AddressBlock block = addressBlocks.get(address);
        block.addInstruction(thread.getInstruction());
        thread.getPrevBlock().addNext(block);
        thread.setPrevBlock(block);

        CacheStats cacheStats = cache.getStats();
        cacheStats.addChangeToStat(block.getStats());
        cacheStats.forgetChanges();
    }
    
    public void print() {
        for(AddressBlock block: addressBlocks.values()) {
            // is een multi line string die zelf voor line separation zorgt!
            System.out.print(block.toString());
        }
    }
    
}
