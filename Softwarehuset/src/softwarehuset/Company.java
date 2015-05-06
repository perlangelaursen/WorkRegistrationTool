package softwarehuset;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Company {
	private String name;
	private Address address;
	private Executive executive;
	private boolean executiveLoggedIn;
	private Employee loggedInEmployee;
	private List<Project> projects = new ArrayList<>();
	private List<Employee> employees = new ArrayList<>();
	private List<Employee> availableEmployees = new ArrayList<>();
	private DateServer dateServer;
	private int counter = 0;

	public Company(String name, Address address) {
		this.name = name;
		this.address = address;
		dateServer = new DateServer();
	}
	public void setExecutive(Executive executive) {
		this.executive = executive;
	}

	public boolean executiveIsLoggedIn() {
		return executiveLoggedIn;
	}

	public void executiveLogin(String password) {
		if (password.equals(executive.getPassword())) {
			executiveLoggedIn = true;
		}
	}

	public Project createProject(String name) throws OperationNotAllowedException {
		if (!executiveIsLoggedIn()) {
			throw new OperationNotAllowedException("Create project operation is not allowed if not executive.", "Create project");
		}
		for (Project p : projects) {
			if (name.equals(p.getName())) {
				throw new OperationNotAllowedException("Name already exists","Create Project");
			}
		}
		counter++;
		Project p = new Project(name, this);
		projects.add(p);
		return p;
	}
	
	public Project createProject(String name, GregorianCalendar start, GregorianCalendar end) throws OperationNotAllowedException {
		if (!executiveIsLoggedIn()) {
			throw new OperationNotAllowedException("Create project operation is not allowed if not executive.", "Create project");
		}
		
		for (Project p : projects) {
			if (name.equals(p.getName())) {
				throw new OperationNotAllowedException("Name already exists","Create Project");
			}
		}
		
		if (!start.after(getCurrentTime())){
			throw new OperationNotAllowedException("The start date has already been passed", "Create project");
		}
		
		if (start.after(end)){
			throw new OperationNotAllowedException("The end date is set before the start date", "Create project");
		}
		counter++;
		Project p = new Project(name, start, end, this);
		projects.add(p);
		return p;
	}
	
	public Employee createEmployee(String id, String password, String department) throws OperationNotAllowedException {
		if (id.length() != 4) {
			throw new OperationNotAllowedException("Employee ID must be the length of 4 letters","Create employee");
		}
		if (id.matches(".*[0-9].*")) {
			throw new OperationNotAllowedException("Employee ID must not contain any numbers","Create employee");
		}
		Employee e = new Employee(id, password, this, department);
		employees.add(e);
		return e;
	}
	
	public List<Project> getProjects() {
		return projects;
	}
	
	public Project getProject(String name) throws OperationNotAllowedException{
		Project project = null;
		int counter = 0;
		for (Project p : projects) {
			if (p.getName().equals(name)) {
				counter++;
				project = p;
			}
		}
		if(counter>1){
			throw new OperationNotAllowedException("Several projects have the requested title. Search by ID instead.","Get project");
		}
		if (project == null){
			throw new OperationNotAllowedException("Project could not be found","Get project"); 
		}
		return project;
	}
	
	public Project getProject(int ID){
		for (Project p : projects) {
			if (p.getID() == ID) {
				return p;
			}
		}
		return null;
	}
	
	public void employeeLogin(String id, String password) {
		for(Employee e: employees){
			if (e.getID().equals(id)){
				if(e.getPassword().equals(password)){
					executiveLoggedIn = false;
					loggedInEmployee = e;
					break;
				}
			}
		}
	}

	public Employee getLoggedInEmployee() {
		return loggedInEmployee;
	}

	public Calendar getCurrentTime() {
		return dateServer.getCurrentDate();
	}
	
	public void setDateServer(DateServer dateServer) {
		this.dateServer = dateServer;
	}

	public List<Employee> getAvailableEmployees(GregorianCalendar d1, GregorianCalendar d2) {
		for (Employee e : employees) {
			if (e.isAvailable(d1,d2)) {
				availableEmployees.add(e);
			}
		}
		return availableEmployees;
	}

	public void employeeLogout() {
		executiveLoggedIn = false;
		loggedInEmployee = null;
	}

	public int getProjectCounter() {
		return counter;
	} 
	
	public Employee getEmployee(String id) throws OperationNotAllowedException {
		Employee employee = null;
		for(Employee e : employees) {
			if(e.getID().equals(id)) {
				employee = e;
			}
		}
		if (employee == null){
			throw new OperationNotAllowedException("Employee not found", "Get employee");
		}
		return employee;
	}

	public void clearProjects() {
		projects.clear();
	}
	
	public void checkForInvalidDate(int year, int month, int date) throws OperationNotAllowedException {
		//Find max year
		GregorianCalendar newYear = new GregorianCalendar();
		newYear.setTime(getCurrentTime().getTime());
		newYear.add(Calendar.YEAR, 5);
		int maxYear = newYear.get(Calendar.YEAR);
		
		//Check if valid date
		if(year<1980 || year > maxYear || month< 0 || month > 11){
			throw new OperationNotAllowedException("Invalid time input", "Choose date");
		}
		
		//Check if date exists in the chosen month
		GregorianCalendar cal = new GregorianCalendar(year, month, 1,0,0,0);
		int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		if (date < 1 || date > daysInMonth){
			throw new OperationNotAllowedException("Invalid time input", "Choose date");
		}
	}
	
	public void checkForInvalidWeek(int week) throws OperationNotAllowedException {
		if (week > 52 || week < 1) {
			throw new OperationNotAllowedException("Invalid week", "Choose week");
		}
	}
}
