
import Genetics.ConnectionGene;
import Genetics.Gene;
import Genetics.Genome;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestGA {

    public static void print(String s) {
        System.out.println(s);
    }

    public static void main(String args[]) {

        ArrayList<Genome> pop = new ArrayList();
        Consumer<ArrayList> listln = (l) -> {
            l.forEach((e) -> System.out.println(e));
        };
        Function<Integer, Gene> testGene = (id) -> {
            return new Gene(true, id) {
                double value = 0;

                @Override
                public Gene mutate() {
                    this.value += 1;
                    return this;
                }

                @Override
                public Gene setEnabled(boolean e) {
                    this.enabled = true;
                    return this;
                }

            };
        };
        BiFunction<Integer, Integer, Supplier<Genome>> getGenomeFactory = (in, out) -> {
            return () -> {
                final Random r = new Random();
                final ArrayList<ConnectionGene> init = new ArrayList();
                Genome g = new TestGenome(in + out);
                for (int i = 0; i < in; i++) {
                    for (int j = out; j < in + out; j++) {
                        Gene conn = new ConnectionGene(i, j, r.nextDouble(), true);
                        g.add(conn);
                    }
                }
                return g;
            };
        };
        Supplier<Genome> genomeFactory = getGenomeFactory.apply(3, 3);
        System.out.println("Creating genome with 3 input, 3 output nodes");
        Genome original = genomeFactory.get();
        Genome mutated = original.clone();
        System.out.println("Genome clone equals original: " + mutated.equals(original));
        mutated.mutate();
        System.out.println("Original: " + original);
        System.out.println("Mutated: " + mutated);
        System.out.println("Changing original doesnt affect clone: " + !mutated.equals(original));
        System.out.println();
        System.out.println("Overlap original vs mutated");
        listln.accept(original.chromosome.overlap(mutated.chromosome));
        System.out.println();

        System.out.println("overlap mutated vs original");
        listln.accept(mutated.chromosome.overlap(original.chromosome));
        System.out.println();

        System.out.printf("Creating children: \n\tStrong Original: %s\n\tStrong Mutated: %s",
                original.chromosome.crossover(mutated.chromosome), mutated.chromosome.crossover(original.chromosome));

    }
//    public static void XOR(){
//        XOR f = new XOR();
////        NEATPop p = new NEATPop(100, .1, .1, .1, .3, 1.3, 2, 1, f);
//
//        p = p.nextGen();
//        Genome init_fit = p.getMostFit();
//
//        // Run through 100 generations
//        for (int i = 0; i < 5; i++) {
//            System.out.println("Running generation " + (i+1) + "...");
//            //System.out.println("Number of species: " + p.getNumSpecies());
//            p = p.nextGen();
//            System.out.println("Most fit genome:\n\n" + p.getMostFit());
//            System.out.println("Top fitness: " + f.simulate( new Network(p.getMostFit()) ));
//        }
//
//        Genome g = p.getMostFit();
//        System.out.println(g);
//        Network n = new Network(g);
//        System.out.println(n);
//        System.out.print("Output: ");
//        Double[] outs = f.getOuts();
//        for (int i = 0; i < outs.length; i++)
//            System.out.print(outs[i] + ", ");
//        System.out.println("");
//        //System.out.println("Convergence success: " + (1 - g.fitness / init_fit.fitness) + " %");
//    }

    static class TestGenome extends Genome {

        Random r = new Random();
        ArrayList<ConnectionGene> splits;

        TestGenome(int i) {
            super(i);
        }

        @Override
        public ArrayList<ConnectionGene> split(ConnectionGene g) {
            ArrayList<ConnectionGene> into = super.split(g);
            splits.addAll(into);
//            System.out.println("Splitting on "+g+" into "+into);
            return into;
        }

        @Override
        public Genome split() {
            splits = new ArrayList();
            return super.split();
        }

    }
    String katie_is_hundry = "kello whats upp m;y name is katie and i llike to eat food im really hundry i like to eat hot cheetos they are really good and yummy";

}
