public class NodeGene {
    public int id;

    NodeGene(int geneCount){
        this.id = geneCount;
    }

    @Override
    public boolean equals (Object o){
        NodeGene c = (NodeGene) o ;
        return this.id == c.id;
    }


    public boolean equals (NodeGene c){
        return this.id == c.id;
    }
}
