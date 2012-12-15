/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

import java.util.HashMap;

/**
 *
 * @author Nathan
 */
public class AddressBlock {
    
    private long address;
    
    private long jumpCount;
    private HashMap<AddressBlock, Long> nextAmount;
    
    private CacheStats stats;

    public AddressBlock(long address) {
        this.address = address;
        jumpCount = 0;
        nextAmount = new HashMap<>();
        stats = new CacheStats();
    }

    public long getAddress() {
        return address;
    }
    
    public void addNext(AddressBlock block) {
        if(!block.equals(this)) {
            if(!nextAmount.containsKey(block)) {
                nextAmount.put(block, (long)0);
            }
            long amount = nextAmount.get(block);
            nextAmount.put(block, amount+1);
            
            jumpCount++;
        }
    }

    public long getJumpCount() {
        return jumpCount;
    }

    public CacheStats getStats() {
        return stats;
    }

    public HashMap<AddressBlock, Long> getNextAmount() {
        return nextAmount;
    }

    @Override
    public String toString() {
        String block = "J:" + jumpCount + System.lineSeparator();
        
        for(AddressBlock next: nextAmount.keySet()) {
            block += "N:" + address + ":" + nextAmount.get(next) + ":" + next.getAddress() + System.lineSeparator();
        }
        
        block += "C:" + stats.toString() + System.lineSeparator();
        
        return block;
    }
    
}
