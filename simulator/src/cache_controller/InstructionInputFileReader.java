/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller;

import cache_controller.instruction.Instruction;
import cache_controller.instruction.MemoryAccess;
import cache_controller.instruction.NormalInstruction;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Nathan
 */
public class InstructionInputFileReader implements InputReader {
    
    private BufferedReader reader;
    private boolean closed;
    
    public InstructionInputFileReader(File input) {
        try {
            reader = new BufferedReader(new FileReader(input));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        closed = false;
    }
    
    /**
     * valid: "@$ <threadid> : <instructionaddress> <type: MEM/INS> <memaddr1> <memaddr2> ... <memaddrn>"
     * @param line
     * @return 
     */
    private boolean isLineValid(String line) {
        boolean valid = false;
        if(line.startsWith("@I") || line.startsWith("@M")) {
            String[] parts = line.split(" ");
            valid = parts.length >= 3;
            
            if(valid) {
                try {
                    for(int i = 0; i < parts.length; i++) {
                        valid &= Long.parseLong(parts[i]) >= 0;
                    }
                } catch(NumberFormatException ex) {
                    valid = false;
                }
            }
            
            // hoort valid te zijn
            if(!valid) {
                System.err.println("" + line);
            }
        }
        
        return valid;
    }
    
    /**
     * haal valid instruction line op
     * @return 
     */
    public String getLine() {
        String line = null;
        
        do {
            try {
                line = reader.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        } while(line != null && !isLineValid(line));
        
        return line;
    }
    
    @Override
    public Instruction getInstructionFromThread(int thread) {
        if(closed) {
            return null;
        }
        
        String line = null;
        String[] parts = null;
        do{
            line = getLine();
            if(line != null) {
                parts = line.split(" ");
            }
        } while(line != null && Integer.parseInt(parts[1]) != thread);
        
        if(line == null) {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                closed = true;
                return null;
            }
        }
        
        Instruction instr = null;
        long instructionAdress = Long.parseLong(parts[2]);
        switch (parts[0]) {
            case "@I":
                instr = new NormalInstruction(line, instructionAdress);
                break;
            case "@M":
                instr = new MemoryAccess(line, instructionAdress);
                break;
            default:
                throw new IllegalArgumentException("Illegal instruction");
        }
        return instr;
    }
}
