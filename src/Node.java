import java.util.ArrayList;



public class Node{
	
	//Instanzvariablen
	private Node parent;
	private String incomingEdge;
	private int stringDepth;
	private ArrayList<Node> childs;
	private ArrayList<String> edges;
	
	//Konstruktor
	public Node(String edge, Node parent){
		this.parent = parent;
		this.incomingEdge = edge;
		if (parent==null){
			this.stringDepth=0;
		}
		else {
			this.stringDepth=parent.getStringDepth()+incomingEdge.length();
		}
		childs = new ArrayList<Node>();
		edges = new ArrayList<String>();
	}
	
	//Setter
	public void setParent(Node parent){
		this.parent = parent;
	}
	
	//Add Child
	public void addChild(String edge, Node parent){
		childs.add(new Node(edge,parent));
		edges.add(edge);
	}
	
	public void addChild(Node v){
		childs.add(v);
		edges.add(v.incomingEdge);
	}
	
	
	
	//Getter
	public Node getParent(){
		return this.parent;
	}
	
	public int getStringDepth(){
		return this.stringDepth;
	}
	
	public Node getChild(String e){
		return childs.get(edges.indexOf(e));
		
	}
	
	public ArrayList<String> getEdges(){
		return this.edges;
	}
	
	public String getIncomingEdge(){
		return this.incomingEdge;
	}
	
	public String getEdge(String substring){
		for (String edge : edges){
			if (edge.charAt(0)==substring.charAt(0)){
				return edge;
			}
		}
		return null;
	}
	
	
	
	
	//Edit
	public String setIncomingEdge(String e){
		String tmp = incomingEdge;
		incomingEdge = e;
		return tmp;
	}

	

	public void replaceChild(Node v, Node x) {
		edges.remove(v.incomingEdge);
		edges.add(x.incomingEdge);
		childs.remove(v);
		childs.add(x);
		
	}	
}	
	

