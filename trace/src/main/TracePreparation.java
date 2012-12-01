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
        File output = null;
        output = new File(args[args.length - 1]);
        if(output.exists() && !output.isFile()) {
            throw new IllegalArgumentException("File not found: " + args[1]);
        }
        if(output.exists()) {
            output.delete();
        }
        output.createNewFile();
        
        InstructionInputFileReader reader = new InstructionInputFileReader(input, null);
        BufferedWriter writer = new BufferedWriter(new FileWriter(output));
        
        Instruction instr = reader.getInstruction();
        while(instr != null) {    
            
            while(instr instanceof NormalInstruction) {
                long counting = 0;
                long threadId = instr.getThread();
                ArrayList<Address> addressList = new ArrayList<>();
                
                while(instr instanceof NormalInstruction && instr.getThread() == threadId) {
                    counting++;
                    addressList.add(instr.getInstructionAdress());
                    instr = reader.getInstruction();
                }
                
                Collections.sort(addressList);
                Address instrAddress = addressList.get(addressList.size()/2);
                String description = "@I " + instrAddress.toString() + " " + threadId + " " + counting; //TODO (eigenlijk fout, maar voorlopig betere optie)
                writer.write(description);
            }
            if(instr != null) {
                writer.write(instr.getDescription());
                
                instr = reader.getInstruction();
            }
        }
        
        writer.close();
    }
    
}
