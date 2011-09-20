public class SuffixTree {

	private Node root;
	
	public SuffixTree(String text){
		//text=" "+text.replaceAll("[^\\sa-zA-Z0-9_-]","")+" ";
		// Tested '$',' ' now 'Ï' 
		if (text.charAt(text.length()-1)!='¾'){
			text += '¾';
		}
		int n = text.length();
		root = new Node("",null);
		for (int i=1;i<=n;i++){
			String tmpText = text.substring(i-1);
			Node v = matchAtRoot(tmpText);
			if (v == null){
				root.addChild(tmpText,root);
			}
			else {
				Node check = v;
				// Folge Kanten, bis Mismatch zwangslŠufig in vorheriger Kante
				v = getNextNode(tmpText,v); 				
				if (check!=v){
					tmpText=tmpText.substring(v.getParent().getStringDepth());
				}
				// Ermittle Index des Mismatch
				int mismatch = getMismatch(tmpText,v);
				//Mismatch an Knoten
				if (mismatch < 0){
					v.addChild(tmpText.substring(-mismatch),v);
				}
				//Mismatch an Kante
				else { 
					String alphaBeta = v.getIncomingEdge();
					String alpha = alphaBeta.substring(0,mismatch);
					String beta = alphaBeta.substring(mismatch);
					Node x = new Node(alpha,v.getParent());
					v.getParent().replaceChild(v,x);
					v.setParent(x);
					v.setIncomingEdge(beta);
					x.addChild(v);
					x.addChild(text.substring(i-1+mismatch),x);
				}
			}
		}
	}
	
	//Tree Hilsfunktionen
	private Node matchAtRoot(String text) {
		for (String edge:root.getEdges()){
			if (text.charAt(0)==edge.charAt(0)){
				return root.getChild(edge);
			}
		}
		return null;
	}
	
	private Node getNextNode(String suffix, Node v){
		int n = v.getIncomingEdge().length();
			if (n >= suffix.length())	{
				return v;
			}
			else {
				if (v.getIncomingEdge().equals(suffix.substring(0,n))){
					suffix = suffix.substring(n);
					if (v.getEdge(suffix)!=null){
						v=v.getChild(v.getEdge(suffix));
						v=getNextNode(suffix,v);
					}
				}
			}
		return v;
	}

	private int getMismatch(String suffix,Node v) {
		String iEdge = v.getIncomingEdge();
		int i=0;
		try {
			for (i=0;i<iEdge.length();i++){
				if (iEdge.charAt(i)!=suffix.charAt(i)){
					return i;
				}
			}
		}
		catch (Exception e){
			System.err.println("Exception in getMismatch");
			System.err.println("Current State:");
			System.err.println("Suffix "+suffix+" L="+suffix.length());
			System.err.println("Incoming Edge "+v.getIncomingEdge()+ "L="+v.getIncomingEdge().length());			
		}
		return -i;
	}
	
	//Bereitgestellte FunktionalitŠt
	public boolean contains(String pattern){
		return contains(pattern,this.root);
	}
	
	private boolean contains(String pattern,Node start){
		Node check = start;
		try {
			start = getNextNode(pattern,start);
			if (check==start){
				return false;
			}
			else {
				pattern=pattern.substring(start.getParent().getStringDepth());
				if (pattern.equals(start.getIncomingEdge())){
					return true;
				}
				else if(pattern.length()<=start.getIncomingEdge().length() ){
					if (pattern.equals(start.getIncomingEdge().substring(0,pattern.length()))){
						return true;
					}
					else {
						return false;
					}
				}
				else{
					return false;
				}
			}
		}
		catch (Exception e){
			System.err.println("Exception in contains");
			System.err.println("Current State:");
			System.err.println("Pattern "+pattern+" L="+pattern.length());
			System.err.println("Incoming Edge "+start.getIncomingEdge()+ "L="+start.getIncomingEdge().length());
			return false;
		}
	}
}