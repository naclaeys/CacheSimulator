/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller;

import cache_controller.instruction.Instruction;
import cache_controller.instruction.InstructionThread;
import cache_controller.instruction.MemoryAccess;
import cache_controller.instruction.NormalInstruction;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import main.GCASimulator;

/**
 *
 * @author naclaeys
 */
public class InputReader {
    
    BufferedReader reader;
    private boolean closed;
    
    private InstructionThread[] threads;

    public InputReader(File input, int threadCount) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(input));
        closed = false;
        this.threads = new InstructionThread[threadCount];
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new InstructionThread(i);
        }
    }
    
    private void addInstructionsToThreads() {
        int count = 0;
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        while(line != null && count < GCASimulator.READING_AMOUNT) {
            if(line.startsWith("@$")) {
                String[] parts = line.split(" ");
                int index = Integer.parseInt(parts[1]);
                
                Instruction instr = null;
                if(parts[2].equals("INS")) {
                    instr = new NormalInstruction(line);
                } else if(parts[2].equals("MEM")) {
                    instr = new MemoryAccess(line);
                } else {
                    throw new IllegalArgumentException("Illegal instruction");
                }
                threads[index].getInstructions().add(instr);
                
                count++;
            }
            
            if(count < GCASimulator.READING_AMOUNT) {
                try {
                    line = reader.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        
        if(line == null) {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                closed = true;
            }
        }
    }
    
    public Instruction getInstructionFromThread(int thread) {
        while(threads[thread].getInstructions().isEmpty() && !closed) {
            addInstructionsToThreads();
        }
        
        if(threads[thread].getInstructions().isEmpty()) {
            return null;
        } else {
            return threads[thread].getInstructions().removeFirst();
        }
    }
    
}
