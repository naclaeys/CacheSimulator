/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import cache.BasicCache;
import cache.TwoLayerCache;
import configuration.CacheOptimizer;
import configuration.DummyOptimizer;
import configuration.ManualConfiguration;
import configuration.Optimizer;
import cpu.CPU;
import cpu.instruction.Instruction;
import inputreader.InstructionInputFileReader;
import java.io.File;
import java.io.IOException;
import statistics.Stats;

/**
 *
 * @author naclaeys
 */
public class GCASimulator {
    
    public static final long HIT_COST_LAYER1 = 1;
    // nooit gebruikt eigenlijk, miss cost layer1 == (hit cost layer2 || miss cost layer2)
    public static final long MISS_COST_LAYER1 = 3;
    public static final long HIT_COST_LAYER2 = 3;
    public static final long MISS_COST_LAYER2 = 100;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if(args.length < 10 | args.length > 11) {
            throw new IllegalArgumentException("Usage: inputFile linePrintMark coreCount shareLayer2 blockCount1 ways1 blockCount2 ways2 blockSize at_run_time_config <configurationFile>");
        }
        String input = args[0];
        long addressBlockSize = Integer.parseInt(args[1]);
        int coreCount = Integer.parseInt(args[2]);
        boolean shared = Boolean.parseBoolean(args[3]);
        int blockCount1 = Integer.parseInt(args[4]);
        int ways1 = Integer.parseInt(args[5]);
        int blockCount2 = Integer.parseInt(args[6]);
        if(!shared) {
            blockCount2 /= coreCount;
        }
        int ways2 = Integer.parseInt(args[7]);
        int blockSize = Integer.parseInt(args[8]);
        boolean dynamic = Boolean.parseBoolean(args[9]);
        File configuration = null;
        if(args.length == 11) {
            configuration = new File(args[args.length - 1]);
            if(!configuration.isFile()) {
                throw new IllegalArgumentException("File not found: " + args[args.length - 1]);
            }
        }
        
        System.out.println("input " + input);
        System.out.println("addressBlockSize " + addressBlockSize);
        System.out.println("coreCount " + coreCount);
        System.out.println("shared " + shared);
        System.out.println("cacheBlockSize " + blockSize);
        System.out.println("blockCount1 L1 " + blockCount1);
        System.out.println("ways1 L1 " + ways1);
        System.out.println("blockCount2 L2 " + blockCount2);
        System.out.println("ways2 L2 " + ways2);
        System.out.println("at run time config " + dynamic);
        if(configuration != null) {
            System.out.println("configuration" + configuration.getName());
        }
        
        TwoLayerCache[] caches = new TwoLayerCache[coreCount];
        Optimizer[] optimizers = new Optimizer[coreCount];
        int[] assoc = new int[] {1,4,16,64,256,1024};
        TwoLayerCache[][] simCaches = new TwoLayerCache[coreCount][assoc.length];
        int currentConfig = 0;
        if(shared) {
            BasicCache layer2 = new BasicCache(HIT_COST_LAYER2, MISS_COST_LAYER2, blockCount2, ways2, blockSize);
            
            BasicCache[] simLayer2 = new BasicCache[assoc.length];
            for(int i = 0; i < assoc.length; i++) {
                simLayer2[i] = new BasicCache(HIT_COST_LAYER2, MISS_COST_LAYER2, blockCount2, assoc[i], blockSize);
                if(assoc[i] == ways2) {
                    currentConfig = i;
                }
            }
            CacheOptimizer opt = new CacheOptimizer(caches, currentConfig, simCaches, addressBlockSize);
            
            for(int i = 0; i < coreCount; i++) {
                caches[i] = new TwoLayerCache(HIT_COST_LAYER1, HIT_COST_LAYER1, blockCount1, ways1, blockSize, layer2);
                
                for(int k = 0; k < assoc.length; k++) {
                    simCaches[i][k] = new TwoLayerCache(HIT_COST_LAYER1, HIT_COST_LAYER1, blockCount1, ways1, blockSize, simLayer2[k]);
                }
                optimizers[i] = opt;
            }
        } else {
            for(int i = 0; i < coreCount; i++) {
                caches[i] = new TwoLayerCache(HIT_COST_LAYER1, HIT_COST_LAYER1, blockCount1, ways1, blockSize, 
                        HIT_COST_LAYER2, MISS_COST_LAYER2, blockCount2, ways2, blockSize);
                
                for(int j = 0; j < assoc.length; j++) {
                    simCaches[i][j] = new TwoLayerCache(HIT_COST_LAYER1, HIT_COST_LAYER1, blockCount1, ways1, blockSize, 
                        HIT_COST_LAYER2, MISS_COST_LAYER2, blockCount2, assoc[j], blockSize);
                    if(assoc[j] == ways2) {
                        currentConfig = j;
                    }
                }
                optimizers[i] = new CacheOptimizer(caches, currentConfig, simCaches, addressBlockSize);
            }
        }
        
        if(!dynamic) {
            if(configuration != null) {
                if(shared) {
                    ManualConfiguration conf = new ManualConfiguration(configuration, caches, addressBlockSize, 0);
                    for(int i = 0; i < optimizers.length; i++) {
                        optimizers[i] = conf;
                    }
                } else {
                    for(int i = 0; i < optimizers.length; i++) {
                        optimizers[i] = new ManualConfiguration(configuration, caches, addressBlockSize, i);
                    }
                }
            } else {
                for(int i = 0; i < optimizers.length; i++) {
                    optimizers[i] = new DummyOptimizer();
                }
            }
        }
        
        Stats stats = new Stats(addressBlockSize);        
        CPU cpu = new CPU(caches, optimizers, stats);
        
        InstructionInputFileReader reader = new InstructionInputFileReader(new File(input + ".txt"), input, cpu);
        Instruction instr = reader.getInstruction();
        reader.close();
        // nieuwe reader om eerste instr niet te missen
        cpu.addThread(instr.getThread(), new InstructionInputFileReader(new File(input + ".txt"), input, cpu));
        
        cpu.start();
        
        System.out.println("--");
        System.out.println("cyclus count: " + cpu.getCycleCount());
        System.out.println("");
        for(int i = 0; i < coreCount; i++) {
            System.out.println("core " + i + ": " + optimizers[i].getReconfigCount());
            System.out.println("");
            caches[i].print();
            System.out.println("");
        }
        System.out.println("");
        
        System.out.println("--");
        System.out.println("");
        stats.print();
    }
}
