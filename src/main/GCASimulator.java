/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import cache.BasicCache;
import cache.Cache;
import cache_controller.CPU;
import cache_controller.InputReader;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author naclaeys
 */
public class GCASimulator {
    
    public static final int READING_AMOUNT = 3;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if(args.length != 3) {
            throw new IllegalArgumentException("Usage: inputFile outputFile configurationFile");
        }
        File input = new File(args[0]);
        if(!input.isFile()) {
            throw new IllegalArgumentException("File not found: " + args[0]);
        }
        File output = new File(args[1]);
        if(output.exists()) {
            output.delete();
        }
        output.createNewFile();
        File configuration = new File(args[2]);
        if(!input.isFile()) {
            throw new IllegalArgumentException("File not found: " + args[2]);
        }
        
        InputReader reader = new InputReader(input, 2);
        Cache cache = new BasicCache(4, 2);
        CPU cpu = new CPU(reader, cache, 2);
        
        cpu.start();
        
        System.out.println("cyclus count: " + cpu.getCycleCount());
        System.out.println("cache missers: " + cache.getTotalMisses());
    }
}
