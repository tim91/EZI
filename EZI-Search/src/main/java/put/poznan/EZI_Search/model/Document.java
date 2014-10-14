package put.poznan.EZI_Search.model;

import java.util.StringTokenizer;

import put.poznan.EZI_Search.reader.StemmerStringTokenizer;

public class Document {

	private int id;
	
	private String titile;
	private String orginalTitle;
	private String content="";
	
	public Document() {
		// TODO Auto-generated constructor stub
	}
	
	public Document(int id, String titile, String content) {
		super();
		this.id = id;
		this.titile = titile;
		this.content = content;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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
	
	public StringTokenizer tokenize(){
		
		return new StemmerStringTokenizer(this.titile + " " + this.content, " ");
	}
	
	@Override
	public String toString() {
		return this.titile;
	}

    public String getOrginalTitle()
    {
        return orginalTitle;
    }

    public void setOrginalTitle( String orginalTitle )
    {
        this.orginalTitle = orginalTitle;
    }
	
}
