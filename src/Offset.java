
public class Offset {
	private int pmid;
	private int startoffset;
	private int endoffset;
	private String title;
	private String jtitle;
	
	public Offset(int pmid, int startoffset, int endoffset, String title, String journalTitle){
		this.pmid=pmid;
		this.startoffset=startoffset;
		this.endoffset=endoffset;
		this.title=title;
		this.jtitle=journalTitle;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getJtitle(){
		return this.jtitle;
	}
	
	public int getPmid() {
		return pmid;
	}

	public void setPmid(int pmid) {
		this.pmid = pmid;
	}

	public int getStartoffset() {
		return startoffset;
	}

	public void setStartoffset(int startoffset) {
		this.startoffset = startoffset;
	}

	public int getEndoffset() {
		return endoffset;
	}

	public void setEndoffset(int endoffset) {
		this.endoffset = endoffset;
	}


	
}
