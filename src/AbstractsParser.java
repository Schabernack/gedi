import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.stream.*;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.util.XMLEventAllocator;

import org.xml.sax.SAXException;

import com.sun.xml.internal.stream.events.XMLEventAllocatorImpl;

public class AbstractsParser {

	private int pmid;
	private String abstractText;
	private String journalTitle;
	private String abstractTitle;
	
	private int offsetBegin;
	private int offsetEnd;
	
	private AbstractAnalyzer analyzer;
	private ArrayList<Entry> entries;
	private int abstractCount;
	
	private File sqlFile;
	private XMLStreamReader eventReader;
	private XMLEventAllocator allocator;
	
	private final int OFFSET_BEGIN_DIF =0; //-7;
	private final int OFFSET_END_DIF = 0; //OFFSET_BEGIN_DIF-15;
	
	private ArrayList<Offset> offsets=new ArrayList<Offset>();
	
	public AbstractsParser() throws IOException, SAXException{
		
		entries = new ArrayList<Entry>();
		analyzer = new AbstractAnalyzer();
	}
	
	public void parse(File file) throws XMLStreamException, IOException{
		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setEventAllocator(new XMLEventAllocatorImpl());
		allocator = factory.getEventAllocator();
		eventReader = factory.createXMLStreamReader(new FileReader(file));
		while(eventReader.hasNext()){
			int eventType = eventReader.next();
			switch(eventType){
				case XMLStreamConstants.START_ELEMENT:
					this.startElement(getXMLEvent(eventReader).asStartElement());
					break;				
				case XMLStreamConstants.END_DOCUMENT:
					this.generateSQLDump();		
					break;
				
			}
		}
	}
	
	private void startElement(StartElement element) throws XMLStreamException{
		String name = element.getName().getLocalPart();		
		if ("PMID".equals(name)){
			this.pmid = Integer.parseInt(this.eventReader.getElementText());
		}
		if ("Title".equals(name)){
			this.journalTitle = this.eventReader.getElementText();
		}
		if ("AbstractText".equals (name)){
			this.abstractCount++;			
			this.offsetBegin = this.eventReader.getLocation().getCharacterOffset();			
			this.abstractText=this.eventReader.getElementText();			
			this.endElement(name);
		}
		if("ArticleTitle".equals(name)){
			this.abstractTitle=this.eventReader.getElementText();
		}
	}
	
	private void endElement(String name){
		if ("AbstractText".equals (name)){
			this.offsetEnd = this.eventReader.getLocation().getCharacterOffset();
			this.analyze();
		}
	}
		
	private void analyze(){
		try {
			analyzer.constructTree(this.abstractTitle+" "+this.abstractText);
			Entry entry = new Entry(this.pmid);
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
			System.out.print("\r Analyzing abstract: "+abstractCount);
			
			int offset_start = this.offsetBegin+this.OFFSET_BEGIN_DIF;
			int offset_end = this.offsetEnd+this.OFFSET_END_DIF;
			offsets.add(new Offset(this.pmid, offset_start, offset_end,this.abstractTitle, this.journalTitle));
			//System.out.printf("Offset start: %d offset end: %d \n",this.offsetBegin+this.OFFSET_BEGIN_DIF,this.offsetEnd+this.OFFSET_END_DIF);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
	}
	
	private void generateSQLDump() throws IOException {	
		System.out.println("\nStarting generation of " +sqlFile.getPath());
		FileWriter fileWriter = new FileWriter(sqlFile, true);
		for(Entry entry: entries){
			fileWriter.write(entry.getInsertStatements());
		}
		fileWriter.close();
		System.out.printf("sqldump generated with %d statements \n",Entry.statementsCount);
		
		System.out.println("\nStarting generation of Offset SQL FILE");
		FileWriter fw = new FileWriter(new File(sqlFile.getPath().substring(0,sqlFile.getPath().length()-4)+"-offset.sql"), false);
		for(Offset offset:offsets){
			fw.write("INSERT INTO abstract (pmid, startoffset, endoffset, title, journaltitle, filename) VALUES ("+offset.getPmid()+","+offset.getStartoffset()+","+offset.getEndoffset()+",\""+offset.getTitle().replace("\"", "\\\"").replace(";", "\\;")+"\",\""+offset.getJtitle().replace("\"", "\\\"").replace(";", "\\;")+"\",\""+sqlFile.getName()+"\");\n" );
		}
		fw.close();
		System.out.println("offsetgenerierung abgeschlossen");
		entries = new ArrayList<Entry>();
		offsets = new ArrayList<Offset>();
		abstractCount = 0;
	}
	
	public void setSqlFile(File file){
		this.sqlFile= file;
	}
	
	private XMLEvent getXMLEvent(XMLStreamReader reader) throws XMLStreamException {
			return allocator.allocate(reader);
	}
}
