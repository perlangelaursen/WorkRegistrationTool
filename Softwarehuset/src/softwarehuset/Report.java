package softwarehuset;

import java.util.GregorianCalendar;

public class Report {
	private Project project;
	private GregorianCalendar date;
	private String name;
	private String content;
	
	public Report(Project project, String name, GregorianCalendar date) {
		this.project = project;
		this.name = name;
		this.date = date;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	public String getName() {
		return name;
	}
	public Project getProject(){
		return project;
	}
}
