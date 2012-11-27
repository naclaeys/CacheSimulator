/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

/**
 *
 * @author Nathan
 */
public class TwoLayerCache extends Cache {

    public static final long CACHE_HIT_LAYER2 = 10;
    public static final long CACHE_MISS_LAYER2 = 100;
    
    private Cache layer1;
    private Cache layer2;
    
    public TwoLayerCache(Cache layer1, Cache layer2) {
        this.layer1 = layer1;
        this.layer2 = layer2;
    }
    
    public TwoLayerCache(int blockCount1, int ways1, int blockCount2, int ways2) {
        this(new BasicCache(blockCount1, ways1), new BasicCache(blockCount2, ways2));
    }
    
    public TwoLayerCache(int blockCount1, int blockCount2, int ways) {
        this(blockCount1, ways, blockCount2, ways);
    }
    
    public TwoLayerCache(int blockCount, int ways) {
        this(blockCount, ways, blockCount, ways);
    }

    @Override
    protected boolean isHit(long adress) {
        return layer1.isHit(adress) || layer2.isHit(adress);
    }

    @Override
    protected void addAddress(long adress) {
        layer2.addAddress(adress);
        layer1.addAddress(adress);
    }

    @Override
    public long getFetchTime(long adress) {
        long time;
        
        if(layer1.isHit(adress)) {
            time = HIT_COST;
            layer1.addCacheHit();
            addCacheHit();
        } else {
            layer1.addColdMiss();
            if(layer2.isHit(adress)) {
                time = CACHE_HIT_LAYER2;
                layer2.addCacheHit();
                addCacheHit();
            } else {
                time = CACHE_MISS_LAYER2;
                layer2.addColdMiss();
                addColdMiss();
            }
        }
        // instellen van cache missers en hits + toevoegen + refreshen
        addAddress(adress);
        
        return time;
    }

    @Override
    public void clearCacheMemory() {
        layer1.clearCacheMemory();
        layer2.clearCacheMemory();
    }

    @Override
    public void printStats() {
        super.printStats();
        System.out.println("layer1 toegangen: " + (layer1.getTotalMisses() + layer1.getCacheHits()));
        System.out.println("layer1 missers: " + layer1.getTotalMisses());
        System.out.println("layer1 hits: " + layer1.getCacheHits());
        
        System.out.println("layer2 toegangen: " + (layer2.getTotalMisses() + layer2.getCacheHits()));
        System.out.println("layer2 missers: " + layer2.getTotalMisses());
        System.out.println("layer2 hits: " + layer2.getCacheHits());
    }
    
}
