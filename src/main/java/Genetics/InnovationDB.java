package Genetics;

import Genetics.ConnectionGene;
import java.util.Arrays;
import java.util.Hashtable;

/**
 * 
 * @author shanemendez
 * used to assign ID's to new genes.
 */
public class InnovationDB {

    private int nextId = 0;
    private Hashtable<Gene, Integer> db;

    public InnovationDB() {
        db = new Hashtable();
    }
    public InnovationDB(Hashtable db){
        this.db = db;
    }

    //innovation has occured if gene hash exists inside hashtable
    //adds the gene to the table if it has not occurred.
    int get(Gene g) {
        if (db.containsKey(g)) {
            return db.get(g);
        }
        db.put(g, nextId);

        return nextId++;
    }

    public int size() {
        return db.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InnovationDB other = (InnovationDB) obj;
        return this.db.equals(other.getDB());
    

    }

    public Hashtable getDB() {
        return this.db;
    }
    public String toString(){
        return db.toString();
    }

}
