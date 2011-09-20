import java.util.ArrayList;


public class Entry {
	
	//Variables
	private int pmid;
	private ArrayList<Integer> geneList;
	private ArrayList<Integer> diseaseList;
	public static int statementsCount;
	
	//Constructor
	public Entry(int pmid){
		this.pmid = pmid;
		this.geneList = new ArrayList<Integer>();
		this.diseaseList = new ArrayList<Integer>();
	}
	
	//Getter
	public int getID(){
		return pmid;
	}
	
	
	public ArrayList<Integer> getGeneList(){
		if (!geneList.isEmpty()){
			return geneList;
		}
		else {
			System.err.println("No Genes found!");
			return null;
		}
	}
	
	public ArrayList<Integer> getDiseaseList(){
		if (!diseaseList.isEmpty()){
			return diseaseList;
		}
		else {
			System.err.println("No Diseases found!");
			return null;
		}
	}
	
	//Add Gene/Disease
	public void addGene(int id){
		if (!geneList.contains(id)){
			geneList.add(id);
		}
	}
	
	public void addDisease(int id){
		if (!diseaseList.contains(id)){
			diseaseList.add(id);
		}
	}
	
	//Relevance
	public boolean isRelevant(){
		if (geneList.isEmpty() || diseaseList.isEmpty()){
			return false;
		}
		else {
			return true;
		}
	}

	public String getInsertStatements() {
		String statement="";
		//System.out.println("PMID: "+this.pmid+"Gene size:"+geneList.size()+"disease size: "+diseaseList.size());
		for(Integer genID : geneList){
			for(Integer diseaseID: diseaseList){
				statement += "INSERT INTO publication (abstractId, geneId, diseaseId, timestamp) " +
						"VALUES(\"%s\", \"%s\", \"%s\", now()); ";
				statement = String.format(statement, this.pmid,genID,diseaseID);
				statement += "\n";
				statementsCount++;
				//System.out.println("Gefundene Gene: "+geneList.toString());
			}
		}
		return statement;
		
	}
}
