/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputreader;

import cache_controller.CPU;
import cache_controller.instruction.Instruction;
import cache_controller.instruction.MemoryAccess;
import cache_controller.instruction.NormalInstruction;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

/**
 *
 * @author Nathan
 */
public class InstructionInputFileReader implements InputReader {
    
    private String basicName;
    
    private long totalSize;
    private long read;
    private int count;
    private JFrame window;
    private JProgressBar bar;
    
    private BufferedReader reader;
    private boolean closed;
    private CPU cpu;
    
    public InstructionInputFileReader(File input, String basicName, CPU cpu) {
        totalSize = input.length();
        read = 0;
        count = 0;
        window = new JFrame(input.getName());
        window.setVisible(true);
        window.setLayout(new FlowLayout());
        bar = new JProgressBar(0, 1000);
        window.add(bar);
        window.pack();
        try {
            reader = new BufferedReader(new FileReader(input));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        closed = false;
        
        this.basicName = basicName;
        this.cpu = cpu;
    }
    
    public BufferedReader getReader() {
        return reader;
    }
    
    private void increaseRead(String line) {
        read += line.length() + 1;
        count++;
        
        if(count == 100) {
            count = 0;
            long max = (long)bar.getMaximum();
            bar.setValue((int)((max*read)/totalSize));
        }
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
                case "@S":
                    instr = new NormalInstruction(line, thread, "0");
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
                if(line != null) {
                    increaseRead(line);
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
    
    @Override
    public Instruction getInstructionFromThread(long thread) {
        if(closed) {
            return null;
        }
        
        Instruction instr = null;
        do {
            instr = getInstruction();
            if(instr != null && instr.getThread() != thread) {
                cpu.addThread(instr.getThread(), new InstructionInputFileReader(new File(basicName + instr.getThread() + ".txt"), basicName, cpu));
            }
        } while(instr != null && instr.getThread() != thread);
        
        if(instr == null) {
            try {
                reader.close();
                window.dispose();
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
