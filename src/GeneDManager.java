import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;


public class GeneDManager {

	/**
	 * @param args
	 * @throws SAXException 
	 * @throws IOException 
	 * @throws XMLStreamException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, SAXException, XMLStreamException {
		
		if (args.length == 1){			
			try{
				File directory = new File(args[0]);
				if (directory.isDirectory()){
					Indexer indexer = new Indexer();
					//indexer.indexDirectory(directory);
					indexer.indexDir(directory);
				}
				else
					System.out.printf("Directory \"%s\" unknown! \n",args[0]);
			} catch(FileNotFoundException e){
				e.printStackTrace();
			}
		}
		else{
			System.out.println("Command unknown. \n" +
					"Please use:\n" +
					"/my/path/to/xmlfiles/\n");
		}
	}

}
