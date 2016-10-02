import java.util.Hashtable;
public class InnovationDB{
	private int connectionID = 1;
  private Hashtable<Integer,Hashtable<Integer,Integer>> connectionDB;
	private Hashtable<Integer,Integer> splitDB;
	InnovationDB(){
		db = new Hashtable();
	}

	ConnectionGene createConnection(NodeGene g1, NodeGene g2, float weight){
		int id = innov(g1.id,g2.id);
		return new ConnectionGene(g1,g2,weight,true,connectionID);
	}

	int innov(int id1, int id2){
		Hashset to = connectionDB.get(id1);
		if(to == null){
			to = new Hashset();
			connectionsDB.put(id1,to);
		}
		if(!to.containsKey(id2)){
			to.add(id2,++connectionID);
			return connectionID;
		}
		return to.get(id2);

	}
}
