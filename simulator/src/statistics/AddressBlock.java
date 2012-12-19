/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package statistics;

import cpu.instruction.Address;
import cpu.instruction.Instruction;
import cpu.instruction.MemoryAccess;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Nathan
 */
public class AddressBlock {
    
    private long address;
    private int cacheBlockSize;
    
    private long jumpCount;
    private HashMap<AddressBlock, Long> nextAmount;
    
    private HashSet<Address> memoryAccess;
    private HashSet<Long> firstTags;
    
    private CacheStats stats;

    public AddressBlock(long address, int cacheBlockSize) {
        this.address = address;
        this.cacheBlockSize = cacheBlockSize;
        jumpCount = 0;
        nextAmount = new HashMap<>();
        stats = new CacheStats();
        memoryAccess = new HashSet<>();
        firstTags = new HashSet<>();
    }

    public long getAddress() {
        return address;
    }
    
    public void addJumpCount() {
        jumpCount++;
    }
    
    public void addNext(AddressBlock block) {
        if(!block.equals(this)) {
            if(!nextAmount.containsKey(block)) {
                nextAmount.put(block, (long)0);
            }
            long amount = nextAmount.get(block);
            nextAmount.put(block, amount+1);
            
            block.addJumpCount();
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
    
    public long getMemoryCountPerJump() {
        return firstTags.size();
    }

    @Override
    public String toString() {
        String block = "J:" + address + ":" + jumpCount + System.lineSeparator();
        
        for(AddressBlock next: nextAmount.keySet()) {
            block += "N:" + address + ":" + nextAmount.get(next) + ":" + next.getAddress() + System.lineSeparator();
        }
        
        block += "C:" + address + ":" + stats.toString() + System.lineSeparator();
        
        return block;
    }

    public void addInstruction(Instruction instruction) {
        if(instruction instanceof MemoryAccess) {
            MemoryAccess access = (MemoryAccess)instruction;
            
            if(!memoryAccess.contains(access.getInstructionAdress())) {
                // eerste keer dat we dit bezoeken, potentiele cold misses hier bij tellen
                memoryAccess.add(access.getInstructionAdress());
                
                Address[] cacheAddress = access.getAdress();
                for(int i = 0; i < cacheAddress.length; i++) {
                    long tag = cacheAddress[i].divideBy((long)cacheBlockSize);
                    firstTags.add(tag);
                }
            }
        }
    }
    
}
