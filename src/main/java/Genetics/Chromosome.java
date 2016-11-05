package Genetics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.BiFunction;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author shanemendez
 */
public class Chromosome<T extends Gene> implements Collection<T> {

    private ArrayList<T> genelist = new ArrayList();
    private TreeMap<Comparable, T> genes = new TreeMap();
    private static Random random = new Random();
    private static double disRate = .5;
    private static double exRate = .2;

    public Chromosome() {

    }

    public Chromosome(ArrayList<T> temp) {
        this.setGenes(temp);
    }

    public Chromosome(Chromosome<T> t) {
        t.iterator().forEachRemaining((elem) -> this.add(elem));
    }

    /**
     * [0] matching [1] disjoint [2] excess matching: both chromosomes have gene
     * disjoint: mate doesnt have chromosome, but gene not excess excess:
     * mate.genes outside this.genes id range
     *
     * creates sets of genes in either chromosome and removes matches the two
     * new sets are now disjoint from the mate set find excess
     *
     * @param mate
     * @return
     */
    public ArrayList<ArrayList<T>> overlap(Chromosome mate) {
        ArrayList<ArrayList<T>> temp = new ArrayList();

        ArrayList<T> match = new ArrayList();
        ArrayList<T> disjoint = new ArrayList();
        ArrayList<T> excess = new ArrayList();

        temp.add(match);
        temp.add(disjoint);
        temp.add(excess);

        HashSet<T> mateSet = new HashSet(mate.toList());
        HashSet<T> thisSet = new HashSet(this.toList());
        for (Iterator<T> iter = this.iterator(); iter.hasNext();) {
            T next = iter.next();
            //remove matchs from both sets
            if (mateSet.contains(next)) {
                mateSet.remove(next);
                thisSet.remove(next);
                match.add(next);
            }
        }
        //thisSet[x] exists -> mateSet[x] does not exist
        //mateSet[x] exists -> thisSet[x] does not exist
        //there is not any element in thisSet that is also in mateSet and viceversa
        // thisSet +  mateSet - excess = disjoint
        // excess = everything in mateSet with id > maxid

        int maxid = this.get(this.size() - 1).getId();
        mateSet.forEach((gene) -> {
            if (gene.getId() > maxid) {
                excess.add(gene);
                return;
            }
            disjoint.add(gene); // disjoint = mateSet - exess
        });
        disjoint.addAll(thisSet); // disjoint += thisSet
        return temp;
    }

    static Chromosome crossover(Chromosome strong, Chromosome mate) {
        ArrayList<ArrayList<Gene>> overlap = strong.overlap(mate);
        Chromosome child = new Chromosome();
        child.addAll(overlap.get(0));
        overlap.get(1).stream().filter((next) -> (random.nextDouble() <= disRate)).forEach((next) -> {
            System.out.format("\tAdding disjoint %s\n", next);
            child.add(next);
        });
        overlap.get(2).stream().filter((next) -> (random.nextDouble() <= exRate)).forEach((next) -> {
            System.out.format("\tAdding excess %s\n", next);
            child.add(next);
        });
        System.out.println();
        return child;
    }

    public Chromosome crossover(Chromosome mate) {
        return Chromosome.crossover(this, mate);
    }

    Chromosome mutate(double mrate) {
        ArrayList temp = new ArrayList();
        Random random = new Random();
        for (T g : this) {
            Gene gm = g;
            if (mrate > random.nextDouble()) {
                gm = g.mutate();
            }
            temp.add(gm);
        }
        this.setGenes(temp);
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        return genes.values().iterator();
    }

    private void setGenes(List<T> g) {
        this.genes = new TreeMap();
        this.addAll(g);
    }

    @Override
    public int size() {
        return this.genes.size();
    }

    @Override
    public boolean isEmpty() {
        return this.genes.isEmpty();
    }

    @Override
    public boolean contains(Object mate) {
        return genes.containsValue((T) mate);
    }

    @Override
    public Object[] toArray() {
        return genelist.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.genelist.toArray(a);
    }

    public boolean add(T g) {
        this.genes.put(g, g);
        this.genelist = new ArrayList(this.genes.values());

        return true;
    }

    public boolean addAll(Collection<? extends T> gs) {
        gs.forEach((elem) -> this.add(elem));
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean remove(Object gene) {
        this.genes.remove((T) gene);
        this.genelist = new ArrayList(this.genes.values());
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> genes) {
        genes.forEach((elem) -> this.remove((T) elem));
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean equals(Object o) {
        Chromosome c = (Chromosome) o;
        return this.genes.equals(c.genes);
    }

    @Override
    public void clear() {
        this.genelist = new ArrayList();
        this.genes = new TreeMap();
    }

    public T get(int i) {
        return genelist.get(i);
    }

    public ArrayList<T> toList() {
        return new ArrayList(this.genelist);
    }

    @Override
    public String toString() {
        return genes.values().toString();
    }

    public Chromosome clone() {
        return new Chromosome(this.genelist);
    }

}
