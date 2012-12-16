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
import statistics.Stats;

/**
 *
 * @author Nathan
 */
public class CacheOptimizer extends Optimizer {
    
    private TwoLayerCache[] coreCaches;

    private int currentConfig;
    
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
    
    private AddressBlock getCurrentAddressBlock(InstructionThread thread, int configId) {
        return configStats[configId].getAddressBlocks().get(thread.getAddressIndex(addressBlockSize));
    }

    private int getBestConfig(InstructionThread thread, int core) {
        int bestIndex = currentConfig;
        long bestGain = 0;
        
        AddressBlock currentBlock = getCurrentAddressBlock(thread, currentConfig);
        if(currentBlock != null) {
            for(int i = 0; i < configStats.length; i++) {
                long gain = 0;
                for(InstructionThread t: threads) {
                    AddressBlock block = getCurrentAddressBlock(t, i);
                    // nieuwe potentiele colds
                    gain -= block.getMemoryCount();

                    gain += (currentBlock.getStats().getConflictMiss()/currentBlock.getJumpCount())
                            - (block.getStats().getConflictMiss()/block.getJumpCount());
                }

                if(gain > bestGain) {
                    bestGain = gain;
                    bestIndex = i;
                }
            }
        }
        
        return bestIndex;
    }
    
    @Override
    public void check(InstructionThread thread, int core) {
        int config = getBestConfig(thread, core);
        Cache cache = simCaches[core][config];
        if(currentConfig != config) {
            coreCaches[core].installConfiguration(cache.getConfiguration());
            cache.clearCacheMemory();
            
            currentConfig = config;
        }
        
        for(int i = 0; i < simCaches[core].length; i++) {
            thread.getInstruction().getExecutionTime(simCaches[core][i]);
            configStats[i].threadAction(thread, simCaches[core][i]);
        }
    }

    @Override
    public void addThread(InstructionThread thread) {
        threads.add(thread);
    }

}
