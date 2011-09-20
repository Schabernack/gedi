import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.xml.stream.XMLStreamException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

class Indexer {


	private XMLReader xmlReader;
	private AbstractsHandler xmlHandler;
	private FileReader fr;
	
	public Indexer() throws IOException, SAXException{
		
		/*
		//SaX INIT
		xmlReader = XMLReaderFactory.createXMLReader();
		xmlHandler = new AbstractsHandler();		
		xmlReader.setContentHandler(xmlHandler);
		xmlReader.setErrorHandler(xmlHandler);	
		*/
	}
	
	public void indexDirectory(File xmlfileDirectory) throws IOException, SAXException{
	//SaX
		double start,stop, time;
		start = System.currentTimeMillis();
	
		for(File file : xmlfileDirectory.listFiles()){	
			if (file.isFile() && file.getName().substring(file.getName().length()-3).equals("xml")){
				File sqlFile = new File(file.getPath()+".sql");
				if(!sqlFile.exists()){
					sqlFile.createNewFile();
				}
				xmlHandler.setSqlFile(sqlFile);
				fr = new FileReader(file);	
				System.out.println(new Date(System.currentTimeMillis())+" Parsing: "+file.getName());
				xmlReader.parse(new InputSource(fr));
				System.out.println("parsing complete");
				fr.close();
			}
		}
		
		stop = System.currentTimeMillis();
		time = (stop-start)/1000;
		
		System.out.println("Indexing completed in "+time+"s!");
	} 
	
	public void indexDir(File xmlfileDirectory) throws IOException, SAXException, XMLStreamException
	{
		//StaX
		double start,stop, time;
		start = System.currentTimeMillis();
		AbstractsParser parser = new AbstractsParser();
		File[] files = xmlfileDirectory.listFiles();
		fileComparator compi = new fileComparator();
		
		Arrays.sort(files,compi);
		
		for(File file : files){	
			//tut mir leid, ich sch�me mich auch. auf jeden fall nur dateien parsen die mit 'xml' enden
			if (file.isFile() && file.getName().substring(file.getName().length()-3).equals("xml")){
				File sqlFile = new File(file.getPath()+".sql");
				if(!sqlFile.exists()){
					sqlFile.createNewFile();
				}
				parser.setSqlFile(sqlFile);
				System.out.println(new Date(System.currentTimeMillis())+" Parsing: "+file.getName());
				parser.parse(file);
				System.out.println("parsing complete");
			}
		}
		
		stop = System.currentTimeMillis();
		time = (stop-start)/1000;
		
		System.out.println("Indexing completed in "+time+"s!");
	}
}
