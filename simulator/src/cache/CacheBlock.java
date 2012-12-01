/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import cache_controller.instruction.Address;

/**
 *
 * @author Nathan
 */
public class CacheBlock {
    
    private long timeStamp;
    
    private Address address;

    public CacheBlock() {
        timeStamp = -1;
    }

    public CacheBlock(long timeStamp, Address address) {
        this.timeStamp = timeStamp;
        this.address = address;
    }

    public void setAddress(Address address, long timeStamp) {
        this.address = address;
        setTimeStamp(timeStamp);
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Address getAddress() {
        return address;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean isUsed() {
        return address != null;
    }

    void clear() {
        address = null;
        timeStamp = -1;
    }
    
}
