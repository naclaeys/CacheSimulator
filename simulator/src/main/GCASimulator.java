/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import cache.BasicCache;
import cache.Cache;
import cache.TwoLayerCache;
import cache_controller.CPU;
import cache_controller.instruction.Instruction;
import inputreader.InstructionInputFileReader;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author naclaeys
 */
public class GCASimulator {
    
    public static final long HIT_COST_LAYER1 = 1;
    // nooit gebruikt eigenlijk, miss cost layer1 == (hit cost layer2 || miss cost layer2)
    public static final long MISS_COST_LAYER1 = 10;
    public static final long HIT_COST_LAYER2 = 10;
    public static final long MISS_COST_LAYER2 = 100;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if(args.length < 8 | args.length > 9) {
            throw new IllegalArgumentException("Usage: inputFile coreCount shareLayer2 blockCount1 ways1 blockCount2 ways2 blockSize <configurationFile>");
        }
        File input = new File(args[0]);
        if(!input.isFile()) {
            throw new IllegalArgumentException("File not found: " + args[0]);
        }
        int coreCount = Integer.parseInt(args[1]);
        boolean shared = Boolean.parseBoolean(args[2]);
        int blockCount1 = Integer.parseInt(args[3]);
        int ways1 = Integer.parseInt(args[4]);
        int blockCount2 = Integer.parseInt(args[5]);
        int ways2 = Integer.parseInt(args[6]);
        int blockSize = Integer.parseInt(args[7]);
        File configuration = null;
        if(args.length == 9) {
            configuration = new File(args[args.length - 1]);
            if(!configuration.isFile()) {
                throw new IllegalArgumentException("File not found: " + args[args.length - 1]);
            }
        }
        
        Cache[] caches = new Cache[coreCount];
        if(shared) {
            BasicCache layer2 = new BasicCache(HIT_COST_LAYER2, MISS_COST_LAYER2, blockCount2, ways2, blockSize);
            for(int i = 0; i < coreCount; i++) {
                caches[i] = new TwoLayerCache(HIT_COST_LAYER1, HIT_COST_LAYER1, blockCount1, ways1, blockSize, layer2);
            }
        } else {
            for(int i = 0; i < coreCount; i++) {
                caches[i] = new TwoLayerCache(HIT_COST_LAYER1, HIT_COST_LAYER1, blockCount1, ways1, blockSize, 
                        HIT_COST_LAYER2, MISS_COST_LAYER2, blockCount2, ways2, blockSize);
            }
        }
        
        CPU cpu = new CPU(input, caches);
        
        InstructionInputFileReader reader = new InstructionInputFileReader(input, cpu, 0);
        Instruction instr = reader.getInstruction();
        cpu.addThread(instr.getThread(), 0);
        reader.getReader().close();
        
        cpu.start();
        
        System.out.println("");
        
        System.out.println("cyclus count: " + cpu.getCycleCount());
        for(int i = 0; i < coreCount; i++) {
            caches[i].printStats();
            System.out.println("");
        }
    }
}
