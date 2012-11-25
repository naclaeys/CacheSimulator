/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import cache.BasicCache;
import cache.Cache;
import cache.ConfigurableCache;
import cache_controller.CPU;
import cache_controller.InputReader;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author naclaeys
 */
public class GCASimulator {
    
    public static final int READING_AMOUNT = 1000;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if(args.length < 3 | args.length > 4) {
            throw new IllegalArgumentException("Usage: inputFile threadCount blockCount configurationFile");
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
        
        InputReader reader = new InputReader(input, threadCount);
        //Cache c1 = new BasicCache(blockCount);
        Cache c2 = new ConfigurableCache(blockCount, configuration);
        CPU cpu = new CPU(reader, c2, threadCount);
        
        cpu.start();
        
        System.out.println("cyclus count: " + cpu.getCycleCount());
        System.out.println("cache toegangen: " + (c2.getTotalMisses() + c2.getCacheHits()));
        System.out.println("cache missers: " + c2.getTotalMisses());
        System.out.println("cache hits: " + c2.getCacheHits());
        System.out.println("verhouding: " + (double)c2.getCacheHits()/(double)c2.getTotalMisses());
    }
}
