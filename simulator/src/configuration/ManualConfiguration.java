/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import cache.TwoLayerCache;
import cpu.instruction.InstructionThread;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Nathan
 */
public class ManualConfiguration extends Optimizer {

    private long reconfigCount;
    
    private TwoLayerCache[] coreCaches;
    
    private HashMap<Long, Configuration> configurations;
    private long addressBlockSize;
    
    public ManualConfiguration(File configFile, TwoLayerCache[] caches, long addressBlockSize) throws FileNotFoundException, IOException {
        this.addressBlockSize = addressBlockSize;
        this.coreCaches = caches;
        this.reconfigCount = 0;
        
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        configurations = createConfigurations(reader);
    }
    
    private HashMap<Long, Configuration> createConfigurations(BufferedReader reader) throws IOException {
        HashMap<Long, Configuration> conf = new HashMap<>();
        
        String line = reader.readLine();
        while(line != null) {
            String[] parts = line.split(":");
            long address = Long.parseLong(parts[0]);
            int associativity = Integer.parseInt(parts[1]);
            conf.put(address, new Configuration(associativity));
            line = reader.readLine();
        }
        
        return conf;
    }
    
    @Override
    public void check(InstructionThread thread, int core) {
        if(thread.getInstruction() != null) {
            long address = thread.getAddressIndex(addressBlockSize);
            if(configurations.containsKey(address)) {
                Configuration conf = configurations.get(address);
                if(!coreCaches[core].getConfiguration().equals(conf)) {
                    coreCaches[core].installConfiguration(conf);
                    reconfigCount++;
                }
            }
        }
    }

    @Override
    public void addThread(InstructionThread thread) {}

    @Override
    public long getReconfigCount() {
        return reconfigCount;
    }
    
}
