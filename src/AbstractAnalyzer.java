
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

public class AbstractAnalyzer {
	
	private ArrayList<Disease> diseaseList;
	private ArrayList<Gene> geneList;
	private SuffixTree stree;
	private StringFormatter formatter ;
	
	String tmp;
	
	public AbstractAnalyzer() throws IOException, SAXException{
		
		formatter = new StringFormatter();
		
		try {
			DBHandler dbh = new DBHandler();
			diseaseList = dbh.getDiseases();
			geneList = dbh.getGenes();
			System.out.println("Initialized Genes ("+ geneList.size()+")" + "and Diseases("+diseaseList.size()+")");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public List<Integer> getGeneIDs(String abstractText) throws IOException{		
		List<Integer> geneIDs = new ArrayList<Integer>();
		for (Gene x:geneList){
			
			/*if(stree.contains(" "+x.getName().toLowerCase()+" ")){
				geneIDs.add(x.getId());
			}*/
			if(stree.contains(" "+formatter.formatWord(x.getName())+" ")){
				geneIDs.add(x.getId());
			}		
		}
		return geneIDs;
	}
	
	public List<Integer> getDiseaseIDs(String abstractText) throws IOException{
		
		
		List<Integer> diseaseIDs = new ArrayList<Integer>();

		for (Disease x:diseaseList){			
			/*if(stree.contains(" "+x.getName().toLowerCase()+" ")){
				diseaseIDs.add(x.getId());
			}*/
			if(stree.contains(" "+formatter.formatWord(x.getName())+" ")){
				diseaseIDs.add(x.getId());
			}
		}
		return diseaseIDs;
	}

	public void constructTree(String abstractText) {
		//stree =  new SuffixTree(abstractText.toLowerCase());
		//Leerzeichen an Anfang und Ende damit er das erste Wort findet
		stree =  new SuffixTree(" "+formatter.formatText(abstractText)+" ");
		}
}
