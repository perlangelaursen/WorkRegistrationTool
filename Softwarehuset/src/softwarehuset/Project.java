package softwarehuset;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.GregorianCalendar;

public class Project {
	private String name;
	private int ID;
	private GregorianCalendar start, end;
	private Employee projectLeader;
	private List<Employee> assignedEmployees = new ArrayList<Employee>();
	private List<Activity> activities = new ArrayList<Activity>();
	private List<Report> reports = new ArrayList<Report>();
	private Company com;
	
	public Project(String name, Company com) {
		this.name = name;
		this.com = com;
		setID();
	}
	
	public Project(String name, GregorianCalendar start, GregorianCalendar end, Company com) {
		this(name, com);
		this.start = start;
		this.end = end;
	}
	
	public String getName() {
		return name;
	}

	protected void setName(String newName) {
		name = newName;
	}
	
	private void setID() {
		int year = com.getCurrentTime().get(Calendar.YEAR) % 100 * 10000;
		this.ID = year + com.getProjectCounter();
	}
	
	public int getID() {
		return ID;
	}
	
	protected void assignProjectLeader(Employee e) {
		this.projectLeader = e;
	}
	
	public void addEmployeeToProject(Employee e) {
		assignedEmployees.add(e);
	}
	
	public Employee getProjectLeader() {
		return projectLeader;
	}
	
	public List<Activity> getActivities() {
		return activities;
	}

	public List<Employee> getEmployees() {
		return assignedEmployees;
	}

	public void relieveEmployee(Employee e) {
		assignedEmployees.remove(e);
	}

	public Employee getEmployee(String id) {
		for(Employee e : assignedEmployees) {
			if(e.getID().equals(id)) {
				return e;
			}
		}
		return null;
	}
	public int getTotalSpentTime() {
		int sum = 0;
		for(Activity a : activities) {
			sum+=a.getAllSpentTime();
		}
		return sum;
	}

	public Activity getActivity(String activityName) throws OperationNotAllowedException {
		for (Activity a: activities){
			if(a.getName().equals(activityName)){
				return a;
			}
		}
		throw new OperationNotAllowedException("Activity could not be found", "Edit activity");
	}

	public void addReport(Report report){
		reports.add(report);
	}
	
	public void getProjectDetails(List<String> statistics) {
		statistics.add("Project Name: " + name);
		statistics.add("Project Leader ID: " + projectLeader.getID() +" Department " + projectLeader.getDepartment());
		statistics.add("No. of employees assigned: " + assignedEmployees.size());
		assignedEmployeesInProject(statistics);
		activitiesInProject(statistics);
	}

	public void assignedEmployeesInProject(List<String> statistics) {
		for(Employee e : assignedEmployees) {
			statistics.add("ID: " + e.getID() + " Department: " + e.getDepartment());
		}
	}

	public void activitiesInProject(List<String> statistics) {
		statistics.add("No. of activities: "+ activities.size());
		for(Activity a : activities) {
			statistics.add("Activity name: " + a.getName() + " No. of employees: " + a.getEmployees().size());
			a.assignedEmployeesInActivity(statistics);
		}
	}

	public Report getReport(String name) throws OperationNotAllowedException {
		if(com.getLoggedInEmployee() == null){
			throw new OperationNotAllowedException("Get report is not allowed if not logged in", "Get report"); 
		}
		for(Report r : reports) {
			if(r.getName().equals(name)) return r;
		}
		return null;
	}

	public void setStart(GregorianCalendar start2) {
		start = start2;
	}
	
	public GregorianCalendar getStart() {
		return start;
	}

	public void setEnd(GregorianCalendar end2) {
			end = end2; 
	}
	
	public GregorianCalendar getEnd() {
		return end;
	}

	public void addActivity(Activity a) {
		activities.add(a);
	}

}
