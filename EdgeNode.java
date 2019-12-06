
//A node containing an edge to be stored in the adjacency list
public class EdgeNode{
	
	private EdgeNode nextNode;
	private Edge data;

	public EdgeNode(){
		data = null;
		nextNode = null;
	}

	public EdgeNode(EdgeNode nextNode, Edge data){
		this.data = data;
		this.nextNode = nextNode;
	}

	public void setNext(EdgeNode nextNode){
		this.nextNode = nextNode;
	}

	public EdgeNode nextNode(){
		return nextNode;
	}

	public void setData(Edge data){
		this.data = data;
	}

	public Edge getData(){
		return data;
	}

}
