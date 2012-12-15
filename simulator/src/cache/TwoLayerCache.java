/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cpu.instruction.Address;

/**
 *
 * @author Nathan
 */
public class TwoLayerCache extends Cache {
    
    private BasicCache layer1;
    private BasicCache layer2;
    
    public TwoLayerCache(BasicCache layer1, BasicCache layer2) {
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
    protected boolean isHit(Address adress) {
        return layer1.isHit(adress) || layer2.isHit(adress);
    }

    @Override
    public long getFetchTime(Address adress) {
        long time;
        
        if(layer1.isHit(adress)) {
            time = layer1.getFetchTime(adress);
        } else {
            layer1.getFetchTime(adress);
            time = layer2.getFetchTime(adress);
        }
        
        return time;
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
    
}
