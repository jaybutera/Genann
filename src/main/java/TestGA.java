
import java.util.Arrays;

public class TestGA {
    public static void main(String args[]) {
        XOR f = new XOR();
        NEATPop p = new NEATPop(100, .1, .1, .1, .3, 1.3, 2, 1, f);

        p = p.nextGen();
        Genome init_fit = p.getMostFit();

        // Run through 100 generations
        for (int i = 0; i < 5; i++) {
            System.out.println("Running generation " + (i+1) + "...");
            //System.out.println("Number of species: " + p.getNumSpecies());
            p = p.nextGen();
            System.out.println("Most fit genome:\n\n" + p.getMostFit());
            System.out.println("Top fitness: " + f.simulate( new RNN(p.getMostFit()) ));
        }

        Genome g = p.getMostFit();
        System.out.println(g);
        RNN n = new RNN(g);
        System.out.println(n);
        System.out.print("Output: ");
        Double[] outs = f.getOuts();
        for (int i = 0; i < outs.length; i++)
            System.out.print(outs[i] + ", ");
        System.out.println("");
        //System.out.println("Convergence success: " + (1 - g.fitness / init_fit.fitness) + " %");
 


        
    }
}
