public class Creature {
    Creature(Genome g) {
        this.g = g;
        net = new RNN(g);
    }

    public Double fitness = 0.0;
    public Genome g;
    public RNN net;
}
