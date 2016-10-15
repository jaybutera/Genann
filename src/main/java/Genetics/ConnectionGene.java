package Genetics;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public final class ConnectionGene extends Gene{

    int[] vect = new int[2];
    double weight;

    public ConnectionGene(int from, int to, double weight, boolean enabled, int id) {
        super(enabled, id);
        vect = new int[]{from,to};
        this.weight = weight;
        this.enabled = enabled;
        this.id = id;
    }
    
    public ConnectionGene(int from, int to, double weight, boolean enabled) {
        this(from, to, weight, enabled, -1);
    }
    
    ConnectionGene setWeight(double w) {
        return new ConnectionGene(vect[0], vect[1], w, this.enabled, this.id);
    }


    @Override
    Gene mutate(double rate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString(){
        return id+":"+Arrays.toString(vect);
        
    }
    @Override
    public int hashCode() {
        return Arrays.hashCode(vect); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConnectionGene other = (ConnectionGene) obj;
        return Arrays.equals(this.vect, other.vect);
    }
    
    

}
