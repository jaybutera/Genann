import java.util.ArrayList;

public class InnovationDB {

    private ArrayList<ConnectionInv> connections;
    private ArrayList<NodeInv> nodes;

    private int nextConnId;
    private int nextNodeId;

    ConnectionGene getConnection (NodeGene g1, NodeGene g2, double weight) {
        return new ConnectionGene(g1, g2, weight, true, id);
    }


    /********************************/
    // Container innovation classes //
    /********************************/

    class ConnectionInv {
        public ConnectionInv (ConnectionGene c) {
            this.c  = c;
        }

        public boolean equals (ConnectionGene c) {
            return (c.from == this.c.from && c.to == this.c.to);
        }

        final public ConnectionGene c;
    }

    class NodeInv {
        final public NodeGene n;
        final public ConnectionGene from;
        final public ConnectionGene to;

        public NodeInv (NodeGene n, NodeGene in, NodeGene out) {
            this.n = n;
            this.in = in;
            this.out = out;
        }
    }
}
