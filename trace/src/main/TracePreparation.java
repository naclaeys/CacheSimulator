/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import cache_controller.instruction.Address;
import cache_controller.instruction.Instruction;
import cache_controller.instruction.NormalInstruction;
import inputreader.InstructionInputFileReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Nathan
 */
public class TracePreparation {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            throw new IllegalArgumentException("Usage: inputFile outputFile");
        }
        File input = new File(args[0]);
        if(!input.isFile()) {
            throw new IllegalArgumentException("File not found: " + args[0]);
        }
        String output = args[args.length - 1];
        
        InstructionInputFileReader reader = new InstructionInputFileReader(input, null);
        HashMap<Long, BufferedWriter> writers = new HashMap<>();
        
        Instruction instr = reader.getInstruction();
        long firstThread = instr.getThread();
        writers.put(firstThread, new BufferedWriter(new FileWriter(createOutputFile(output))));
        while(instr != null) {    
            
            while(instr instanceof NormalInstruction) {
                checkThread(output, instr.getThread(), writers, firstThread);
                
                long counting = ((NormalInstruction)instr).getAmount() - 1;
                long threadId = instr.getThread();
                ArrayList<Address> addressList = new ArrayList<>();
                
                while(instr instanceof NormalInstruction && instr.getThread() == threadId) {
                    counting++;
                    addressList.add(instr.getInstructionAdress());
                    instr = reader.getInstruction();
                }
                
                Collections.sort(addressList);
                Address instrAddress = addressList.get(addressList.size()/2);
                String description = "@I " + threadId + " " + instrAddress.toString() + " " + counting;
                writeInstruction(description, writers.get(threadId));
            }
            
            if(instr != null) {
                checkThread(output, instr.getThread(), writers, firstThread);
                writeInstruction(instr.getDescription(), writers.get(instr.getThread()));
                
                instr = reader.getInstruction();
            }
        }
        
        for(BufferedWriter writer: writers.values()) {
            writer.close();
        }
    }
    
    private static File createOutputFile(String name) throws IOException {
        File outputFile = new File(name + ".txt");
        if(outputFile.exists() && !outputFile.isFile()) {
            throw new IllegalArgumentException("wrong file name: " + name);
        }
        if(outputFile.exists()) {
            outputFile.delete();
        }
        outputFile.createNewFile();
        return outputFile;
    }
    
    private static void checkThread(String output, long thread, HashMap<Long, BufferedWriter> writers, long firstThread) throws IOException {
        if(!writers.containsKey(thread)) {
            writers.put(thread, new BufferedWriter(new FileWriter(createOutputFile(output + "" + thread))));
            
            writeInstruction("@S " + thread, writers.get(firstThread));
        }
    }
    
    private static void writeInstruction(String description, BufferedWriter writer) throws IOException {
        writer.write(description);
        writer.newLine();
    }
    
}
