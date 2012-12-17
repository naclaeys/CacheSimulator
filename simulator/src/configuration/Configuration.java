/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

/**
 *
 * @author Nathan
 */
public class Configuration {
    
    private int associativity;

    public Configuration(int associativity) {
        this.associativity = associativity;
    }

    public int getAssociativity() {
        return associativity;
    }

    @Override
    public boolean equals(Object obj) {
        Configuration conf = null;
        if(obj instanceof Configuration) {
            conf = (Configuration)obj;
        }
        return conf != null && conf.getAssociativity() == associativity ;
    }
}
