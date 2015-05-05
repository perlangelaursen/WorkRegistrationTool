package softwarehuset;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class Activity {
	private String activityName, type, description;
	private GregorianCalendar start, end;
	private Project project;
	private HashMap<Employee, Integer> employees = new HashMap<>();
	private List<Employee> assignedEmployees = new ArrayList<Employee>();
	private List<Employee> assistingEmployees = new ArrayList<Employee>();
	
	public Activity(GregorianCalendar start, GregorianCalendar end, String type) {
		this.start = start;
		this.end = end;
		this.type = type;
	}
	
	public Activity(String activityName, GregorianCalendar start, GregorianCalendar end, Project project) {
		this(start, end, "Work");
		this.activityName = project.getID()+"-"+activityName;
		this.project = project;
	}

	public void addEmployeeToActivity(Employee e) {
		employees.put(e, 0);
		assignedEmployees.add(e);
	}

	public String getName() {
		return activityName;
	}

	public GregorianCalendar getStart() {
		return start;
	}

	public GregorianCalendar getEnd() {
		return end;
	}

	public List<Employee> getEmployees() {
		return assignedEmployees;
	}
	
	public Project getProject() {
		return project;
	}

	public int getSpentTime(Employee e) {
		return employees.get(e);
	}
	public int getAllSpentTime() {
		int sum = 0;
			for(Employee e : assignedEmployees){
				sum += employees.get(e);
			}
		return sum;
	}
	public void setTime(Employee e, int time) {
		employees.put(e, time);
	}
	public boolean isOverlapping(Activity activity) {
		return (activity.getStart().after(start) && activity.getEnd().before(end)||
				activity.getStart().before(start) && activity.getEnd().after(end)||
				activity.getStart().before(start) && activity.getEnd().after(start)||
				activity.getStart().before(end) && activity.getEnd().after(end)||
				activity.getStart().getTime().equals(start.getTime()));
	}

	public int getTimeSpan() {
		double minutes = ((end.getTime().getTime() - start.getTime().getTime()) / (1000 * 60))+24*60; //include last day
		double hours = (int)Math.round(minutes/60);
		return (int) hours; 
	}

	public String getType() {
		return type;
	}

	public void setEnd(GregorianCalendar newDate) {
		end = newDate;		
	}

	public void setStart(GregorianCalendar newDate) {
		start = newDate;		
	}

	public void assignAssistingEmployee(Employee selected) {
		assistingEmployees.add(selected);
	}

	public List<Employee> getAssistingEmployees() {
		return assistingEmployees;
	}

	public Employee getAssistingEmployee(Employee selected) {
		for(Employee e: assistingEmployees) {
			if(e.getID().equals(selected.getID())) {
				return e;
			}
		}
		return null;
	}

	public void removeAssistingEmployee(Employee selected) {
		assistingEmployees.remove(selected);
		
	}

	public void assignedEmployeesInActivity(List<String> statistics) {
		for(Employee e : assignedEmployees) {
			statistics.add("ID: " + e.getID() + " Department: " + e.getDepartment());
		}
	}
}
