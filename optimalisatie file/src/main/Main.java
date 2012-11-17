/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;

/**
 *
 * @author Nathan
 */
public class Main {
    
    public static void main(String[] args) {
        if(args.length != 2) {
            throw new IllegalArgumentException("Usage: inputFile outputFile");
        }
        File input = new File(args[0]);
        if(!input.isFile()) {
            throw new IllegalArgumentException("File not found: " + args[0]);
        }
        
        int instructionJump = 10;
        int blockCount = 4;
        
        
    }
    
}
