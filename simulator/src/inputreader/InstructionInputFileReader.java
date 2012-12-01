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
    private long lineNumber;
    private boolean closed;
    
    private CPU cpu;
    
    public InstructionInputFileReader(File input, CPU cpu, long lineNumber) {
        try {
            reader = new BufferedReader(new FileReader(input));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        this.lineNumber = 0;
        skipToLine(lineNumber);
        closed = false;
        
        this.cpu = cpu;
    }
    
    private void skipToLine(long lineNumber) {
        while(this.lineNumber < lineNumber) {
            try {
                reader.readLine();
                this.lineNumber++;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public BufferedReader getReader() {
        return reader;
    }
    
    private Instruction createInstruction(String line) {
        Instruction instr;
        String[] parts = line.split(" ");
        
        long thread = Long.parseLong(parts[1]);
        switch (parts[0]) {
            case "@I":
                if(parts.length == 3) {
                    instr = new NormalInstruction(line, thread, parts[2]);
                } else if(parts.length == 4) {
                    instr = new NormalInstruction(line, thread, parts[2], Long.parseLong(parts[3]));
                } else {
                    throw new IllegalArgumentException("Illegal instruction");
                }
                break;
            case "@M":
                instr = new MemoryAccess(line, thread, parts[2]);
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
                lineNumber++;
                if(line != null) {
                    try {
                        instr = createInstruction(line);
                        valid = true;
                    } catch(Exception ex) {
                        valid = false;
                        System.err.println("" + line);
                    }
                    if(valid) {
                        cpu.addThread(instr.getThread(), lineNumber-1);
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
    public Instruction getInstructionFromThread(long thread) {
        if(closed) {
            return null;
        }
        
        Instruction instr = null;
        do{
            instr = getInstruction();
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
