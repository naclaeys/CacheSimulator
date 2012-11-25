/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller.instruction;

import cache.Cache;

/**
 *
 * @author naclaeys
 */
public class MemoryAccess extends Instruction {

    public long[] adress;
    
    public MemoryAccess(String description) {
        super(description);
        
        String[] splitDescr = description.split(" ");
        int index = 0;
        while(!splitDescr[splitDescr.length-1 - index].equals("MEM")) {
            index++;
        }
        
        adress = new long[index];
        for(int i = 0; i < index; i++) {
            // TODO: negatieve adressen?
            adress[i] = Math.abs(Long.parseLong(splitDescr[splitDescr.length-1 - i]));
        }
    }

    public long[] getAdress() {
        return adress;
    }

    @Override
    public long getExecutionTime(Cache cache) {
        return cache.getInstructionTime(this);
    }
    
}
