package put.poznan.EZI_Search.model;

public class SearchReport {

	private String report;

	public SearchReport() {
		// TODO Auto-generated constructor stub
	}

	public SearchReport(String report) {
		super();
		this.report = report;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public void addReportLine(String line) {
		this.report += line + "\n";
	}
	
	public void printReport(){
		System.out.println(this.report);
	}

}
