/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_controller.instruction;

import cache_controller.instruction.Address;

/**
 *
 * @author Nathan
 */
public class Address {

    private boolean big;
    private long address;
    
    public Address(String address) {
    }

    public long modOf(long mod) {
        //TODO big
        return address % mod;
    }
    
    public long divideBy(long divider) {
        //TODO big meetellen
        return address/divider;
    }
    
    @Override
    public String toString() {
        //TODO big meetellen
        return "" + address;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Address)) {
            return false;
        }
        return ((Address)obj).toString().equals(toString());
    }
    
}
