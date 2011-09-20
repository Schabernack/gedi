public class Gene {
	
	private int id;
	private String name;
	
	public Gene(int id, String name){
		this.id=id;
		this.name=name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
}
