package put.poznan.EZI_Search.model;

public class Document {

	private long id;
	
	private String titile;
	private String content;
	
	public Document() {
		// TODO Auto-generated constructor stub
	}
	
	public Document(long id, String titile, String content) {
		super();
		this.id = id;
		this.titile = titile;
		this.content = content;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitile() {
		return titile;
	}
	public void setTitile(String titile) {
		this.titile = titile;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public void addLineContent(String line){
		this.content += " " + line;
	}
	
}
