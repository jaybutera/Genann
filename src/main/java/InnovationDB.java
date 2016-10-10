import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.Random;

public class InnovationDB {

    private ArrayList<ConnectionInv> connections;
    private ArrayList<NodeInv> nodes;

    private int nextConnId;
    private int nextNodeId;

    private  int conInvCount = 0;
    private  int nodeInvCount = 0;

    // default addconnection function
    ConnectionGene addConnection (NodeGene g1, NodeGene g2, double weight) {

        // Checks if connection gene already exists using connectionInv list
        for (ConnectionInv connectioninv: connections) {

            if (connectioninv.equals(g1, g2)) {
               return connectioninv.c;

            }

        }

        return new ConnectionGene(g1, g2, weight, true, incrementConnectionCount());
    }

    // Overrided to use random weight
    ConnectionGene addConnection (NodeGene g1, NodeGene g2) {
        return new ConnectionGene(g1, g2, new Random().nextInt(), true, incrementConnectionCount());
    }



    // Assume original connection is taken care of
    NodeInnovation addNode (NodeGene g1, NodeGene g2) {


        // Checks if node already exists using nodeInv list
        for (NodeInv nodeinv: nodes) {

            if (nodeinv.equals(g1,g2)) {
                NodeGene newGene = nodeinv.n;
                return new NodeInnovation(nodeinv.n, nodeinv.in,nodeinv.out );
            }

        }

        NodeGene  newGene = new NodeGene(incrementNodeCount());

        ConnectionGene c1 = addConnection(g1,newGene);
        ConnectionGene c2 = addConnection(newGene,g2);

        // Update node inv master list since new node was successfully created
        nodes.add (new NodeInv(newGene,c1,c2));

        return new NodeInnovation(newGene,c1,c2 );


    }


    // Keep count of Nodes for id purposes
    int incrementNodeCount () {

        return nodeInvCount++;


    }

    // Keep count of ConnectionNodes for id purposes
    int incrementConnectionCount () {

        return conInvCount++;


    }


    /********************************/
    // Container innovation classes //
    /********************************/

    class ConnectionInv {

        public ConnectionInv (ConnectionGene c) {
            this.c  = c;
        }

        public boolean equals (NodeGene g1, NodeGene g2) {
            return (g1 == this.c.from && g2 == this.c.to);
        }

        final public ConnectionGene c;
    }

    class NodeInv {
        final public NodeGene n;
        final public ConnectionGene in;
        final public ConnectionGene out;

        public boolean equals (NodeGene g1,NodeGene g2) {
            return (g1.id == in.from.id && g2.id == out.to.id);
        }

        public NodeInv (NodeGene n, ConnectionGene in, ConnectionGene out) {
            this.n = n;
            this.in = in;
            this.out = out;
        }
    }
}
