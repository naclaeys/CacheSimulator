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
    
    public MemoryAccess(String description, long thread, long instructionAdress) {
        super(description, thread, instructionAdress);
        
        String[] splitDescr = description.split(" ");        
        adress = new long[splitDescr.length - 3];
        for(int i = 0; i < adress.length; i++) {
            adress[i] = Long.parseLong(splitDescr[i + 3]);
        }
    }

    public long[] getAdress() {
        return adress;
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
