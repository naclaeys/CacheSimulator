/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import cache.BasicCache;
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
    public static final long MISS_COST_LAYER1 = 3;
    public static final long HIT_COST_LAYER2 = 3;
    public static final long MISS_COST_LAYER2 = 100;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if(args.length < 9 | args.length > 10) {
            throw new IllegalArgumentException("Usage: inputFile linePrintMark coreCount shareLayer2 blockCount1 ways1 blockCount2 ways2 blockSize <configurationFile>");
        }
        String input = args[0];
        int linePrintMark = Integer.parseInt(args[1]);
        int coreCount = Integer.parseInt(args[2]);
        boolean shared = Boolean.parseBoolean(args[3]);
        int blockCount1 = Integer.parseInt(args[4]);
        int ways1 = Integer.parseInt(args[5]);
        int blockCount2 = Integer.parseInt(args[6]);
        int ways2 = Integer.parseInt(args[7]);
        int blockSize = Integer.parseInt(args[8]);
        File configuration = null;
        if(args.length == 10) {
            configuration = new File(args[args.length - 1]);
            if(!configuration.isFile()) {
                throw new IllegalArgumentException("File not found: " + args[args.length - 1]);
            }
        }
        
        System.out.println("input " + input);
        System.out.println("linePrintMark " + linePrintMark);
        System.out.println("coreCount " + coreCount);
        System.out.println("shared " + shared);
        System.out.println("blockCount1 L1 " + blockCount1);
        System.out.println("ways1 L1 " + ways1);
        System.out.println("blockCount2 L2 " + blockCount2);
        System.out.println("ways2 L2 " + ways2);
        if(configuration != null) {
            System.out.println("configuration" + configuration.getName());
        }
        
        TwoLayerCache[] caches = new TwoLayerCache[coreCount];
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
        
        Stats stats = new Stats();
        CPU cpu = new CPU(linePrintMark, caches, stats);
        
        InstructionInputFileReader reader = new InstructionInputFileReader(new File(input + ".txt"), input, cpu);
        Instruction instr = reader.getInstruction();
        cpu.addThread(instr.getThread(), reader);
        
        cpu.start();
        
        System.out.println("--");
        System.out.println("cyclus count: " + cpu.getCycleCount());
        for(int i = 0; i < coreCount; i++) {
            caches[i].printStats();
            System.out.println("");
        }
    }
}
