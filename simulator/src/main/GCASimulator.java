/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import cache.BasicCache;
import cache.Cache;
import cache.ConfigurableCache;
import cache.TwoLayerCache;
import cache_controller.CPU;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author naclaeys
 */
public class GCASimulator {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if(args.length < 3 | args.length > 4) {
            throw new IllegalArgumentException("Usage: inputFile threadCount blockCount <configurationFile>");
        }
        File input = new File(args[0]);
        if(!input.isFile()) {
            throw new IllegalArgumentException("File not found: " + args[0]);
        }
        int threadCount = Integer.parseInt(args[1]);
        int blockCount = Integer.parseInt(args[2]);
        File configuration = null;
        if(args.length == 4) {
            configuration = new File(args[3]);
            if(!configuration.isFile()) {
                throw new IllegalArgumentException("File not found: " + args[3]);
            }
        }
        
        //Cache c1 = new BasicCache(blockCount);
        //Cache c2 = new TwoLayerCache(100, 1000, 4);
        Cache[] c3 = new Cache[threadCount];
        Cache layer2 = new BasicCache(blockCount, 4);
        for(int i = 0; i < threadCount; i++) {
            c3[i] = new TwoLayerCache(blockCount, 4, layer2);
        }
        
        CPU cpu = new CPU(input, c3, threadCount);
        
        cpu.start();
        
        System.out.println("");
        
        System.out.println("cyclus count: " + cpu.getCycleCount());
        for(int i = 0; i < threadCount; i++) {
            c3[i].printStats();
            System.out.println("");
        }
    }
}
