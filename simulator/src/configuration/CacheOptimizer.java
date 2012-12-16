/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import cache.Cache;
import cache.TwoLayerCache;
import cpu.instruction.InstructionThread;
import java.util.LinkedList;
import statistics.AddressBlock;
import statistics.CacheStats;
import statistics.Stats;

/**
 *
 * @author Nathan
 */
public class CacheOptimizer {
    
    private TwoLayerCache[] coreCaches;

    // [i][j]: i is core index en j is configuratie index
    private TwoLayerCache[][] simCaches;
    
    private Stats[] configStats;
    private long addressBlockSize;
    
    private LinkedList<InstructionThread> threads;
    
    public CacheOptimizer(TwoLayerCache[] caches, TwoLayerCache[][] simCaches, long addressBlockSize) {
        this.coreCaches = caches;
        this.simCaches = simCaches;
        this.addressBlockSize = addressBlockSize;
        configStats = new Stats[simCaches[0].length];
        for(int i = 0; i < configStats.length; i++) {
            configStats[i] = new Stats(addressBlockSize);
        }
        threads = new LinkedList<>();
    }

    private Cache getBestCache(InstructionThread thread, int core) {
        int bestIndex = 0;
        long best = Long.MAX_VALUE;
        for(int i = 0; i < configStats.length; i++) {
            CacheStats cacheStats = configStats[i].getAddressBlocks().get(thread.getAddressIndex(addressBlockSize)).getStats();
            if(cacheStats.getTotalMisses() < best) {
                best = cacheStats.getTotalMisses();
                bestIndex = i;
            }
        }
        
        return simCaches[core][bestIndex];
    }
    
    public void check(InstructionThread thread, int core) {
        Cache cache = getBestCache(thread, core);
        if(!coreCaches[core].getConfiguration().equals(cache.getConfiguration())) {
            coreCaches[core].installConfiguration(cache.getConfiguration());
            cache.clearCacheMemory();
        }
        
        for(int i = 0; i < simCaches[core].length; i++) {
            thread.getInstruction().getExecutionTime(simCaches[core][i]);
            configStats[i].threadAction(thread, simCaches[core][i]);
        }
    }

    public void addThread(InstructionThread thread) {
        threads.add(thread);
    }

}
