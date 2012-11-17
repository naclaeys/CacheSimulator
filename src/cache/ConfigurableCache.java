/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cache_controller.instruction.MemoryAccess;
import java.io.File;
import java.util.Random;

/**
 *
 * @author Nathan
 */
public class ConfigurableCache extends Cache{
    
    private final Random RG = new Random();
    private Cache cache;
    
    private long instructionNr;
    
    private long jump;
    
    public ConfigurableCache(File config) {
        instructionNr = 0;
        jump = 10;
        cache = new BasicCache(4, RG.nextInt(4) + 1);
    }
    
    @Override
    public long getInstructionTime(MemoryAccess instr) {
        if(instructionNr % jump == 0) {
            setCacheHits(cache.getCacheHits());
            setColdMiss(cache.getColdMiss());
            setConflictMiss(cache.getConflictMiss());
            cache = new BasicCache(4, RG.nextInt(4) + 1);
        }
        
        instructionNr++;
        return cache.getInstructionTime(instr);
    }

    @Override
    public void clearCacheMemory() {
        cache.clearCacheMemory();
    }
    
}
