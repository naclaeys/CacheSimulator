/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu.instruction;

import cache.Cache;

/**
 *
 * @author naclaeys
 */
public class MemoryAccess extends Instruction {
    
    public Address[] adress;
    
    public MemoryAccess(String description, long thread, String instructionAdress) {
        super(description, thread, instructionAdress);
        
        String[] splitDescr = description.split(" ");        
        adress = new Address[splitDescr.length - 3];
        for(int i = 0; i < adress.length; i++) {
            adress[i] = new Address(splitDescr[i + 3]);
        }
    }

    public Address[] getAdress() {
        return adress;
    }
    
    public boolean isPartOfAddress(Address address) {
        int i = 0;
        while(i < adress.length && !adress[i].equals(address)) {
            i++;
        }
        return i < adress.length;
    }

    @Override
    public long getExecutionTime(Cache cache) {
        long time = 0;
        
        for(int i = 0; i < adress.length; i++) {
            time = Math.max(cache.getFetchTime(adress[i]), time);
        }
        
        return time;
    }
    
}
