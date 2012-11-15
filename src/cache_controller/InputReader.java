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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author naclaeys
 */
public class InputReader {
    
    BufferedReader reader;

    public InputReader(File input) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(input));
    }
    
    public boolean addInstructionsToThreads(int amount, InstructionThread[] threadList) {
        Map<Integer, List<Instruction>> threads = new HashMap<Integer, List<Instruction>>();
        
        int count = 0;
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        while(line != null && count < amount) {
            if(line.startsWith("@$")) {
                String[] parts = line.split(" ");
                
                int key = Integer.parseInt(parts[1]);
                if(!threads.containsKey(key)) {
                    threads.put(key, new ArrayList<Instruction>(key));
                }
                
                Instruction instr = null;
                if(parts[2].equals("INS")) {
                    instr = new NormalInstruction(line);
                } else if(parts[2].equals("MEM")) {
                    instr = new MemoryAccess(line);
                } else {
                    throw new IllegalArgumentException("Illegal instruction");
                }
                threads.get(key).add(instr);
                
                count++;
            }
            
            if(count < amount) {
                try {
                    line = reader.readLine();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new RuntimeException(ex);
                }
            }
        }
        
        for(int i = 0; i < threadList.length; i++) {
            if(threads.containsKey(threadList[i].getId())) {
                threadList[i].getInstructions().addAll(threads.get(threadList[i].getId()));
            }
        }
        return line == null;
    }
    
    public void close() throws IOException {
        reader.close();
    }
    
}
