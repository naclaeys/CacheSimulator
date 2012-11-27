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
            // TODO: incorrecte instr lijnen? te lang te kort etc?
        } while(line != null && !(line.startsWith("@$") && line.split(" ").length >= 3));
        
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
            //System.out.println("" + line + " " + parts.length);
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
        switch (parts[2]) {
            case "INS":
                instr = new NormalInstruction(line);
                break;
            case "MEM":
                instr = new MemoryAccess(line);
                break;
            default:
                throw new IllegalArgumentException("Illegal instruction");
        }
        return instr;
    }
}
