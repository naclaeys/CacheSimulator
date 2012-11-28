/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputreader;

import cache_controller.CPU;
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
    
    private CPU cpu;
    
    public InstructionInputFileReader(File input, CPU cpu) {
        try {
            reader = new BufferedReader(new FileReader(input));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        closed = false;
        
        this.cpu = cpu;
    }
    
    /**
     * valid: "@$ <threadid> : <instructionaddress> <type: MEM/INS> <memaddr1> <memaddr2> ... <memaddrn>"
     * @param line
     * @return 
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
    }*/
    
    private Instruction createInstruction(String line) {
        Instruction instr;
        String[] parts = line.split(" ");
        
        int thread = Integer.parseInt(parts[1]);
        long instructionAdress = Long.parseLong(parts[2]);
        switch (parts[0]) {
            case "@I":
                instr = new NormalInstruction(line, thread, instructionAdress);
                break;
            case "@M":
                instr = new MemoryAccess(line, thread, instructionAdress);
                break;
            default:
                throw new IllegalArgumentException("Illegal instruction");
        }
        
        return instr;
    }
    
    /**
     * haal valid instruction line op
     * @return 
     */
    public Instruction getInstruction() {
        Instruction instr = null;
        boolean valid = true;
        String line;
        
        do {
            try {
                line = reader.readLine();
                if(line != null) {
                    try {
                        instr = createInstruction(line);
                    } catch(Exception ex) {
                        valid = false;
                        System.err.println("" + line);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        } while(line != null && !valid);
        
        return instr;
    }
    
    @Override
    public Instruction getInstructionFromThread(int thread) {
        if(closed) {
            return null;
        }
        
        Instruction instr = null;
        String[] parts = null;
        do{
            instr = getInstruction();
            cpu.addThread(instr.getThread());
        } while(instr != null && instr.getThread() != thread);
        
        if(instr == null) {
            try {
                reader.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                closed = true;
                return null;
            }
        }
        
        return instr;
    }
}
