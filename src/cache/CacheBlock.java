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
    
    private int address;

    public CacheBlock() {
        timeStamp = -1;
        address = -1;
    }

    public void setAddress(int address, long timeStamp) {
        this.address = address;
        this.timeStamp = timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getAddress() {
        return address;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
    
    public boolean isUsed() {
        return timeStamp != -1;
    }
    
}
