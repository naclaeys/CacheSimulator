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

    public int[] adress;
    
    public MemoryAccess(String description) {
        super(description);
        
        String[] splitDescr = description.split(" ");
        int index = 0;
        while(!splitDescr[splitDescr.length-1 - index].equals("MEM")) {
            index++;
        }
        
        adress = new int[index];
        for(int i = 0; i < index; i++) {
            adress[i] = Integer.parseInt(splitDescr[splitDescr.length-1 - i]);
        }
    }

    public int[] getAdress() {
        return adress;
    }

    @Override
    public long getExecutionTime(Cache cache) {
        return cache.getInstructionTime(this);
    }
    
}
