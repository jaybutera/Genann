public final class ConnectionGene {

	NodeGene from;
  NodeGene to;
  float weight;
  boolean enabled;
	int id;

  ConnectionGene(NodeGene from, NodeGene to, float weight, boolean enabled, int id){
		this.from = from;
    this.to = to;
		this.weight = weight;
    this.enabled = enabled;
		this.id = id;

  }
  ConnectionGene(ConnectionGene c){
    this(c.from,c.to,c.weight,c.enabled,c.id);
  }
  ConnectionGene flip(){
    return new ConnectionGene(c.from,c.to,c.weight,!c.enabled,c.id);
  }
  ConnectionGene setWeight(float w){
    return new ConnectionGene(c.from,c.to,w,c.enabled,c.id);
  }
  boolean isEnabled(){
    return enabled;
  }


}
