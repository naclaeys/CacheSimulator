/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cpu.instruction;

import java.math.BigInteger;

/**
 *
 * @author Nathan
 */
public class Address implements Comparable<Address> {

    private BigInteger address;
    
    public Address(String address) {
        this.address = new BigInteger(address);
        if(this.address.signum() == -1) {
            throw new IllegalArgumentException("address can not be negative");
        }
    }

    public BigInteger getAddress() {
        return address;
    }

    public long modOf(long mod) {
        return address.mod(BigInteger.valueOf(mod)).longValue();
    }
    
    public long divideBy(long divider) {
        return address.divide(BigInteger.valueOf(divider)).longValue();
    }
    
    @Override
    public String toString() {
        return address.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Address)) {
            return false;
        }
        return ((Address)obj).toString().equals(toString());
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }

    @Override
    public int compareTo(Address o) {
        return address.compareTo(o.getAddress());
    }
    
}
