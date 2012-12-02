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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 *
 * @author Nathan
 */
public class InstructionInputFileReader implements InputReader {
    
    private BufferedReader reader;
    private long lineNumber;
    private boolean closed;
    
    private HashSet<Long> threadsDiscovered;
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
        threadsDiscovered = new HashSet<>();
        
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
        Instruction instr = null;
        try {
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
        } catch(Exception ex) {
            System.err.println("" + line);
        }
        
        return instr;
    }
    
    /**
     * haal valid instruction line op
     * NIET AANPASSEN, noodzakelijk voor trace prep
     * @return 
     */
    public Instruction getInstruction() {
        Instruction instr = null;
        String line = null;
        
        do {
            try {
                line = reader.readLine();
                lineNumber++;
                if(line != null) {
                    // invalid instr worden null
                    instr = createInstruction(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        } while(line != null && instr == null);
        
        return instr;
    }
    
    public String getLine(long thread) {
        String line = null;
        String[] parts;
        long tempThread;;
        do {
            try {
                line = reader.readLine();
                lineNumber++;
                
                if(line != null) {
                    parts = line.split(" ");
                    if(parts.length >= 3) {
                        try {
                            tempThread = Long.parseLong(parts[1]);
                            if(!threadsDiscovered.contains(tempThread) || tempThread == thread) {
                                return line;
                            }
                        } catch(Exception e) {
                            System.err.println(line);
                        }
                    }
                }
            } catch(IOException ex) {
                throw new RuntimeException(ex);
            }
        } while(line != null);
        
        return line;
    }
    
    @Override
    public Instruction getInstructionFromThread(long thread) {
        if(closed) {
            return null;
        }
        
        String line = null;
        Instruction instr = null;
        do {
            line = getLine(thread);
            if(line != null) {
                instr = createInstruction(line);
                if(instr != null && instr.getThread() != thread) {
                    threadsDiscovered.add(instr.getThread());
                    cpu.addThread(instr.getThread(), lineNumber);
                    instr = null;
                }
            } else {
                instr = null;
            }
        } while(line != null && instr == null);
        
        /*
        do{
            instr = getInstruction();
            if(instr != null) {
                cpu.addThread(instr.getThread(), lineNumber-1);
            }
        } while(instr != null && instr.getThread() != thread);
        */
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
