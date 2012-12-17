/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import configuration.Configuration;
import cpu.instruction.Address;

/**
 *
 * @author Nathan
 */
public class TwoLayerCache extends Cache {
    
    private BasicCache layer1;
    private BasicCache layer2;
    
    public TwoLayerCache(BasicCache layer1, BasicCache layer2) {
        super();
        this.layer1 = layer1;
        this.layer2 = layer2;
    }
    
    public TwoLayerCache(long hitCost, long missCost, int blockCount, int ways, int blockSize, BasicCache layer2) {
        this(new BasicCache(hitCost, missCost, blockCount, ways, blockSize), layer2);
    }
    
    public TwoLayerCache(long hitCost1, long missCost1, int blockCount1, int ways1, int blockSize1, long hitCost2, long missCost2, int blockCount2, int ways2, int blockSize2) {
        this(new BasicCache(hitCost1, missCost1, blockCount1, ways1, blockSize1), 
                new BasicCache(hitCost2, missCost2, blockCount2, ways2, blockSize2));
    }
    
    public TwoLayerCache(long hitCost, long missCost, int blockCount, int ways, int blockSize) {
        this(hitCost, missCost, blockCount, ways, blockSize, hitCost, missCost, blockCount, ways, blockSize);
    }

    public BasicCache getLayer1() {
        return layer1;
    }

    public BasicCache getLayer2() {
        return layer2;
    }

    @Override
    public boolean wasPresent(Address address) {
        return layer2.wasPresent(address);
    }
    
    @Override
    protected boolean isHit(Address adress) {
        return layer1.isHit(adress) || layer2.isHit(adress);
    }

    @Override
    public long getFetchTime(Address address) {
        addAccess();
        
        if(layer1.isHit(address)) {
            addCacheHit();
            return layer1.getFetchTime(address);
        } else {
            layer1.getFetchTime(address);
            if(layer2.isHit(address)) {
                addCacheHit();
            } else if(wasPresent(address)) {
                addConflictMiss();
            } else {
                addColdMiss();
            }
            
            return layer2.getFetchTime(address);
        }
    }

    @Override
    public void clearCacheMemory() {
        layer1.clearCacheMemory();
        layer2.clearCacheMemory();
    }

    @Override
    public void print() {
        System.out.println("layer1:");
        layer1.print();
        System.out.println("layer2:");
        layer2.print();
    }

    @Override
    public void installConfiguration(Configuration conf) {
        layer1.clearCacheMemory();
        layer2.installConfiguration(conf);
    }

    @Override
    public Configuration getConfiguration() {
        return layer2.getConfiguration();
    }

    @Override
    public int getBlockSize() {
        return layer1.getBlockSize();
    }
    
}
