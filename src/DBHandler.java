import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;



public class DBHandler {


	private static Connection conn = null;

	public DBHandler() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		System.out.println("initializing database connection");
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		String url = "jdbc:mysql://194.95.66.235:13306/biomeddb"; 
		Properties props = new Properties();
		props.setProperty("user","biomed");
		props.setProperty("password","biomeduser!"); 
		conn = DriverManager. getConnection(url, props); 
		System.out.println("done");
	}

	public void closeConnection() throws SQLException {
		if (conn != null){
			conn.close();
		}
	}

	public  ArrayList<Gene> getGenes() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		ArrayList<Gene> genes = new ArrayList<Gene>();
		Statement st;
		st = conn.createStatement();
		//Alle ergebnisse ohne Zahlen.
		ResultSet rs = st.executeQuery("SELECT geneId, name FROM gene_synonym WHERE length(name)>1");

		while (rs.next() ){			
			genes.add(new Gene(rs.getInt(1),rs.getString(2)));
			//System.out.println(rs.getInt(1)+":"+rs.getString(2));
		}
		return genes;
	}

	public  ArrayList<Disease> getDiseases() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		ArrayList<Disease> diseases = new ArrayList<Disease>();
		

		Statement st;
		st = conn.createStatement();

		ResultSet rs = st.executeQuery("SELECT disease.id ,disease_synonym.name FROM disease JOIN disease_synonym where disease.diseaseId=disease_synonym.diseaseId AND length(disease_synonym.name)>1");

		while (rs.next() ){
			diseases.add(new Disease(rs.getInt("id"),rs.getString("name")));
			//System.out.println(rs.getInt(1)+":"+rs.getString(2));
		}
		
		
		return diseases;
	}
}