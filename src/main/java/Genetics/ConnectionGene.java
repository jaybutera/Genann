package Genetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public final class ConnectionGene extends Gene {

    int[] vect = new int[2];
    double weight;
    private static Random r = new Random();

    public ConnectionGene(int from, int to, double weight, boolean enabled, int id) {
        super(enabled, id);
        vect = new int[]{from, to};
        this.weight = weight;
        this.id = id;
    }

    public ConnectionGene(int from, int to, double weight, boolean enabled) {
        this(from, to, weight, enabled, -1);
    }

    public ConnectionGene(int from, int to) {
        this(from, to, r.nextDouble(), true);
    }

    ConnectionGene setWeight(double w) {
        return new ConnectionGene(vect[0], vect[1], w, this.isEnabled(), this.id);
    }

    @Override
    public Gene mutate() {
        Random r = new Random();
        return setWeight(r.nextDouble()).setEnabled(true);
    }

    @Override
    public Gene setEnabled(boolean e) {
        return new ConnectionGene(vect[0], vect[1], weight, e, id);
    }

    @Override
    public String toString() {
        return id + ":" + Arrays.toString(vect) + ":" + String.format("%.4f", this.weight);

    }

    /**
     *
     * @param node Node to connect between this gene;
     * @return
     */
    public ArrayList<ConnectionGene> split(int node) {
        ArrayList<ConnectionGene> conns = new ArrayList();
        conns.add(new ConnectionGene(this.from(), node, this.weight, this.isEnabled()));
        conns.add(new ConnectionGene(node, this.to(), this.weight, this.isEnabled()));
        return conns;
    }

    //used inside InnovationDB
    //assigns 2 genes, with same start and end, same hash

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
//        System.out.println(Arrays.toString(this.vect)+" "+Arrays.toString(other.vect));
        return Arrays.equals(this.vect, other.vect);
    }

    public int to() {
        return vect[1];
    }

    public int from() {
        return vect[0];
    }

    @Override
    public int compareTo(Object o) {
        ConnectionGene g2 = (ConnectionGene) o;
//        if (this.from() == g2.from()) {
//            return this.to() - g2.to();
//        }
//        return this.from() - g2.from();
        return this.id - g2.id;
    }

}
