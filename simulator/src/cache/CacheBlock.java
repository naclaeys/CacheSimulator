/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

/**
 *
 * @author Nathan
 */
public class CacheBlock {
    
    private long timeStamp;
    
    private long address;

    public CacheBlock() {
        timeStamp = -1;
        address = -1;
    }

    public void setAddress(long address, long timeStamp) {
        this.address = address;
        this.timeStamp = timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getAddress() {
        return address;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
    
    public boolean isUsed() {
        return timeStamp != -1;
    }

    void clear() {
        timeStamp = -1;
    }
    
}
