import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Locator;

public class AbstractsHandler extends DefaultHandler {

	private Locator location;
	private int abstractCount;
	private boolean readChar;
	private String element;
	private String pmid;
	private String abstractText;
	private String articleTitle;
	private AbstractAnalyzer analyzer;
	private ArrayList<Entry> entries;
	private File sqlFile;

	public AbstractsHandler() throws IOException, SAXException{
		super();
		
		entries = new ArrayList<Entry>();

		analyzer = new AbstractAnalyzer();
	}

	public void startDocument(String uri, String name, String qName, Attributes atts){		

	}

	@Override
	public void endDocument ()
	{
		try {
			generateSQLDump();
			entries.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("Abstracts found: "+abstractCount);
	}

	private void generateSQLDump() throws IOException {	
		System.out.println("\nStarting generation of " +sqlFile.getPath());
		FileWriter fileWriter = new FileWriter(sqlFile, true);
		for(Entry entry: entries){
			fileWriter.write(entry.getInsertStatements());
		}
		fileWriter.close();
		System.out.printf("sqldump generated with %d statements \n",Entry.statementsCount);
	}

	@Override
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
		if ("PMID".equals(name)){
			element = "pmid";
			readChar = true;
		}
		if ("AbstractText".equals (name)){
			element = "abstracttext";
			System.out.printf("AbstractText beginnt in der Zeile %s und Spalte %s\n",this.location.getLineNumber(),this.location.getColumnNumber());
			readChar = true;
		}
		if("ArticleTitle".equals(name)){
			element = "articletitle";
			readChar = true;
		}
	}

	@Override
	public void endElement (String uri, String name, String qName)	{
		if ("PMID".equals(name)){
			this.element = "";
			this.readChar = false;
		}
		if("ArticleTitle".equals(name)){
			this.element="";
			this.readChar=false;
		}
		if ("AbstractText".equals (name)){
			System.out.printf("AbstractText endet in der Zeile %s und Spalte %s\n",this.location.getLineNumber(),this.location.getColumnNumber());

			this.abstractCount++;			
			this.element = "";
			this.readChar = false;
			try {
				analyzer.constructTree(this.articleTitle+" "+this.abstractText);
				Entry entry = new Entry(Integer.parseInt(this.pmid));
				boolean foundDiseases=false;
				for(int i: analyzer.getDiseaseIDs(this.abstractText)){					
					entry.addDisease(i);
					foundDiseases=true;
				}
				if(foundDiseases){
					for(int i : analyzer.getGeneIDs(this.abstractText))
						entry.addGene(i);
				}
				/*	Je Entry in das sql file schreiben? Entries wird vermutlich irgendwann zu groß 
				 *	für den Arbeitsspeicher und muss ausgelagert werden. Daher die lahme 
				 *	Geschwindigkeit aber ner bestimmten Größe denk ich mal!
				 */
				entries.add(entry);
				//System.out.print("\r Analyzing abstract: "+abstractCount);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}
		

	public void setSqlFile(File file){
		this.sqlFile= file;
	}

	@Override
	public void characters (char[] ch, int start, int length)
	{
		if (readChar) {	
			String str = new String(ch,start,length);
			if(element.equals("pmid"))
				this.pmid=str;
			if(element.equals("abstracttext"))
				this.abstractText=str;
			if(element.equals("articletitle"))
				this.articleTitle=str;
		}
	}
	
	public void setDocumentLocator(Locator locator){
		this.location = locator;
	}

}
