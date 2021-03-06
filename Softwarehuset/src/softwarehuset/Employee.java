package softwarehuset;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

public class Employee {
	private Company company;
	private String id, password;
	private String department;
	private HashSet<Project> projects = new HashSet<>();
	private HashMap<Activity, Integer> activities = new HashMap<>();
	private HashMap<Activity, String> calendar = new HashMap<>();
	
	public Employee(String id, String password, Company company, String department) {
		this.id = id;
		this.password = password;
		this.company = company;
		this.department = department;
	}
	
	public String getID() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getDepartment() {
		return department;
	}
	
	public HashSet<Project> getProjects() {
		return projects;
	}
	
	public Set<Activity> getActivities() {
		return activities.keySet();
	}
	
	private void addProject(Project p) {
		projects.add(p);
	}
	
	private void addActivity(Activity a) {
		activities.put(a, 0);
	}
	
	//Per Lange Laursen - s144486
	public void assignEmployeeProject(String employee, String project) throws OperationNotAllowedException {
		Employee e = company.getEmployee(employee);
		Project p = company.getProject(project);
		checkIfLoggedInProjectLeader(p);
		if(!p.getEmployees().contains(e)){
			p.addEmployeeToProject(e);
			e.addProject(p);
		}
	}
	
	//Per Lange Laursen - s144486
	public void assignEmployeeActivity(String employee, String activity) throws OperationNotAllowedException {
		Activity a = getActivity(activity);
		Employee e = company.getEmployee(employee);
		Project p = a.getProject();
		checkIfLoggedInProjectLeader(p);
		assignEmployeeProject(e.getID(), p.getName());
		a.addEmployee(e);
		e.addActivity(a);
		e.addProject(p);
	}
	
	//Per Lange Laursen - s144486
	public Activity createActivity(Project project, String activityName, GregorianCalendar start, GregorianCalendar end, int time) throws OperationNotAllowedException {
		checkIfLoggedInProjectLeader(project);
		company.checkDateOrder(start, end);
		company.checkStartDateInFuture(start);
		checkValidTime(time);
		checkValidName(project, activityName);
		Activity a = new Activity(activityName, start, end, time, project);
		project.addActivity(a);
		return a;
	}
	
	//Anna Oelgaard Nielsen - s144437
	private void checkValidName(Project project, String activityName) throws OperationNotAllowedException {
		for(Activity a: project.getActivities()){
			if(a.getName().equals(project.getID()+"-"+activityName)){
				throw new OperationNotAllowedException("The project already has an activity by that name", "Set activity name");
			}
		}
	}

	//Per Lange Laursen - s144486
	private Activity getActivity(String activity) throws OperationNotAllowedException {
		Activity a = null;
		for (Project p: company.getProjects()){
			a = p.getActivity(activity);
			break;
		}
		if (a == null){
			throw new OperationNotAllowedException("Activity does not exist", "Edit activity");
		}
		return a;
	}
	
	//Per Lange Laursen - s144486
	public void relieveEmployeeProject(Employee e, Project project) throws OperationNotAllowedException {
		checkIfLoggedInProjectLeader(project);
		project.relieveEmployee(e);
		e.getProjects().remove(project);
	}

	//Van Anh Thi Trinh - s144449
	public void registerSpentTime(String activity, int time) throws OperationNotAllowedException {
		Activity a = getActivity(activity);
		if(company.getLoggedInEmployee() != this){
			throw new OperationNotAllowedException("Employee is not logged in", "Register spent time");
		}
		if(time<0){
			throw new OperationNotAllowedException("Invalid time", "Register spent time");
		}
		if(!activities.containsKey(a)){
			throw new OperationNotAllowedException("Employee is not assigned to the chosen activity", "Register spent time");
		}
		int oldTime = activities.get(a);
		int newTime = oldTime+time;
		activities.put(a, newTime);
		a.setTime(this, newTime);
	}

	//Mathias Enggrob Boon - s144484
	public Object viewProgress(String p, String a) throws OperationNotAllowedException {
		Project project = company.getProject(p);
		Activity activity = company.getProject(p).getActivity(a);
		checkIfLoggedInProjectLeader(project);
		return activity.getAllSpentTime();
	}
	
	//Mathias Enggrob Boon - s144484
	public int viewProgress(String p) throws OperationNotAllowedException {
		Project project = company.getProject(p);
		checkIfLoggedInProjectLeader(project);
		return project.getTotalSpentTime();
	}
	
	//Mathias Enggrob Boon - s144484
	public void writeReport(Project project, String name, int year, int month, int date) throws OperationNotAllowedException{
		checkIfLoggedInProjectLeader(project);
		company.checkForInvalidDate(year, month-1, date);
		GregorianCalendar day = new GregorianCalendar();
		day.set(year, month, date, 0, 0, 0);
		Report report = new Report(project, name, day);
		project.addReport(report);
	}
	
	//Per Lange Laursen - s144486
	public List<String> getStatisticsProject(Project project) throws OperationNotAllowedException {
		checkIfLoggedInProjectLeader(project);
		List<String> statistics = new ArrayList<String>();
		project.getProjectDetails(statistics);
		return statistics;
	}
	
	//Van Anh Thi Trinh - s144449
	public void registerVacationTime(int year, int month, int date, int year2, int month2, int date2) throws OperationNotAllowedException {
		company.checkForInvalidDate(year, month-1, date);
		company.checkForInvalidDate(year2, month2-1, date2);
		Activity vacation = createPersonalActivity(year, month-1, date, year2, month2-1, date2, "Vacation");
		
		if(!vacation.getStart().after(company.getCurrentTime())){
			throw new OperationNotAllowedException("Cannot register vacation in the past", "Register other time");
		}

        addActivityToCalendar(vacation);
		calendar.put(vacation, "Vacation");
	}

	//Van Anh Thi Trinh - s144449
	public void registerSickTime(int year, int month, int date, int year2, int month2, int date2) throws OperationNotAllowedException {
		company.checkForInvalidDate(year, month-1, date);
		company.checkForInvalidDate(year2, month2-1, date2);
		Activity sick = createPersonalActivity(year, month-1, date, year2, month2-1, date2, "Sick");
		
		if(sick.getEnd().after(company.getCurrentTime())){
			throw new OperationNotAllowedException("Cannot register sick days in the future", "Register other time");
		}
		
        addActivityToCalendar(sick);
		calendar.put(sick, "Sick");
	}
	
	//Van Anh Thi Trinh - s144449
	public void registerCourseTime(int year, int month, int date, int year2, int month2, int date2) throws OperationNotAllowedException {
		company.checkForInvalidDate(year, month-1, date);
		company.checkForInvalidDate(year2, month2-1, date2);
		Activity course = createPersonalActivity(year, month-1, date, year2, month2-1, date2, "Course");
		
		if(!course.getStart().after(company.getCurrentTime())){
			throw new OperationNotAllowedException("Cannot register course attendance in the past", "Register other time");
		}
		
		addActivityToCalendar(course);
		calendar.put(course, "Course");
	}

	//Van Anh Thi Trinh - s144449
	private void addActivityToCalendar(Activity a) {
		ArrayList<Activity> removes = getOverlappingActivities(a);
		reAddOverlappingActivities(a, removes);
	}

	//Van Anh Thi Trinh - s144449
	private void reAddOverlappingActivities(Activity a,ArrayList<Activity> removes) {
		for(Activity activity: removes){
			calendar.remove(activity);
			addNewActivity(a, activity);
		}
	}

	//Van Anh Thi Trinh - s144449
	private ArrayList<Activity> getOverlappingActivities(Activity a) {
		ArrayList<Activity> removes = new ArrayList<>();
		for(Activity activity: calendar.keySet()){
			if(a.isOverlapping(activity)){
            	removes.add(activity);
            }
		}
		return removes;
	}

	//Van Anh Thi Trinh - s144449
	public Activity createPersonalActivity(int year, int month, int date, int year2, int month2, int date2, String type)throws OperationNotAllowedException {
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(year, month, date, 0,0,0);
		end.set(year2, month2, date2, 0,0,0);
		company.checkDateOrder(start, end);
		Activity activity = new Activity(start, end, type);
		return activity;
	}
	
	//Van Anh Thi Trinh - s144449
	public void addNewActivity(Activity newActivity, Activity oldActivity) {
		GregorianCalendar newStart = new GregorianCalendar();
		GregorianCalendar newEnd = new GregorianCalendar();
		GregorianCalendar start = newActivity.getStart();
		GregorianCalendar end = newActivity.getEnd();
		newEnd.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH)-1,0,0,0);
		newStart.set(end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH)+1,0,0,0);
		
		//Divide old activity into two parts with new activity between
		if (oldActivity.getStart().before(start) && oldActivity.getEnd().after(end)){
			Activity oldActivityNewFirstPart = new Activity(oldActivity.getStart(), newEnd, oldActivity.getType());
			Activity oldActivityNewSecondPart = new Activity(newStart, oldActivity.getEnd(), oldActivity.getType());
						
			calendar.put(oldActivityNewFirstPart, oldActivityNewFirstPart.getType());
			calendar.put(oldActivityNewSecondPart, oldActivityNewSecondPart.getType());
			
			//Remove first part of old activity
		} else if (oldActivity.getStart().before(start) && !oldActivity.getEnd().before(start)){
			Activity oldActivityNew = new Activity(oldActivity.getStart(), newEnd, oldActivity.getType());
			calendar.put(oldActivityNew, oldActivityNew.getType());
			
			//Remove last part of old activity
		} else if (!oldActivity.getStart().after(end) && oldActivity.getEnd().after(end)){
			Activity oldActivityNew = new Activity(newStart, oldActivity.getEnd(), oldActivity.getType());
			calendar.put(oldActivityNew, oldActivityNew.getType());
		}
	}
	
	//Van Anh Thi Trinh - s144449
	public int getTimeForPersonalActivity(String type) {
		int time = 0;
		for(Activity activity: calendar.keySet()){
			if (activity.getType().equals(type)){
				time += activity.getTimeSpan();
			}
		}
		return time;
	}

	//Anna Oelgaard Nielsen - s144437
	public boolean isAvailable(GregorianCalendar start, GregorianCalendar end) throws OperationNotAllowedException {
		int overlaps = 0;
		company.checkDateOrder(start, end);
		Activity act = new Activity(start, end, "work");
		for (Activity a : activities.keySet()) {
			if (a.isOverlapping(act)) {				
				overlaps++; 
			}
		}
		if (overlaps >= 20){
			return false;
		}
		return true;
	}
	
	//Per Lange Laursen - s144486
	public void requestAssistance(Employee selected, Activity specificActivity) throws OperationNotAllowedException {
		if(company.getLoggedInEmployee() != this){
			throw new OperationNotAllowedException("User not logged in", "Need For Assistance");
		}
		if(specificActivity == null){
			throw new OperationNotAllowedException("Activity not found", "Need for Assistance");
		}
		specificActivity.assignAssistingEmployee(selected);
	}

	//Per Lange Laursen - s144486
	public void removeAssistingEmployee(Employee selected, Activity a) throws OperationNotAllowedException {
		if(company.getLoggedInEmployee() == this) {
			a.removeAssistingEmployee(selected);
		} else {
			throw new OperationNotAllowedException("User not logged in", "Remove Assisting Employee from activity");
		}
	}

	//Van Anh Thi Trinh - s144449
	public int getSpentTime(String activityName) throws OperationNotAllowedException {
		int time = -1;
		for(Activity a: activities.keySet()){
			if (a.getName().equals(activityName)){
				time = activities.get(a);
			}
		}
		if(time == -1){
			throw new OperationNotAllowedException("Employee is not assigned to the activity", "See registered spent time");
		}
		return time;
		}

	//Mathias Enggrob Boon - s144484
	public void editReport(Report report, String content) throws OperationNotAllowedException {
		if(report==null){
			throw new OperationNotAllowedException("Report does not exist", "Edit report");
		}
		checkIfLoggedInProjectLeader(report.getProject());
		report.setContent(content);
	}
	

	//Anna Oelgaard Nielsen - s144437
	public void changeActivityStart(String activity, GregorianCalendar start) throws OperationNotAllowedException {
		Activity a = getActivity(activity);
		checkIfLoggedInProjectLeader(a.getProject());
		company.checkDateOrder(start, a.getEnd());
		company.checkStartDateInFuture(start);
		a.setStart(start);
	}

	//Anna Oelgaard Nielsen - s144437
	public void changeActivityEnd(String activity, GregorianCalendar end) throws OperationNotAllowedException {
		Activity a = getActivity(activity);
		checkIfLoggedInProjectLeader(a.getProject());
		company.checkDateOrder(a.getStart(), end);
		a.setEnd(end);
	}
	
	//Van Anh Thi Trinh - s144449
	public void changeActivityExpectedTime(String activity, int time) throws OperationNotAllowedException {
		Activity a = getActivity(activity);
		checkIfLoggedInProjectLeader(a.getProject());
		checkValidTime(time);
		a.setExpectedTime(time);
	}
	
	private void checkValidTime(int time) throws OperationNotAllowedException {
		if(time<1){
			throw new OperationNotAllowedException("The expected time cannot be less than 1 week", "Set expected time");
		}
	}

	//Per Lange Laursen - s144486
	public void changeProjectStart(String project, GregorianCalendar start) throws OperationNotAllowedException {
		Project p = company.getProject(project);
		checkIfLoggedInProjectLeader(p);
		company.checkDateOrder(start, p.getEnd());
		company.checkStartDateInFuture(start);
		p.setStart(start);
		
	}

	//Per Lange Laursen - s144486
	public void changeProjectEnd(String project, GregorianCalendar end) throws OperationNotAllowedException {
		Project p = company.getProject(project);
		checkIfLoggedInProjectLeader(p);
		company.checkDateOrder(p.getStart(), end);
		p.setEnd(end);
	}
	
	//Van Anh Thi Trinh - s144449
	private void checkIfLoggedInProjectLeader(Project project) throws OperationNotAllowedException {
		if(company.executiveIsLoggedIn()){
			return;
		}
		if(project.getProjectLeader() != this){
			throw new OperationNotAllowedException("Operation is not allowed if not project leader", "Project leader operation");
		}
		if(company.getLoggedInEmployee() != this ){
			throw new OperationNotAllowedException("Operation is not allowed if not project leader", "Project leader operation");
		}
	}

	//Van Anh Thi Trinh - s144449
	public void changeActivityName(Activity a, String name) throws OperationNotAllowedException {
		checkIfLoggedInProjectLeader(a.getProject());
		checkValidName(a.getProject(), name);
		a.setName(name);
	}

	//Van Anh Thi Trinh - s144449
	public void changeProjectName(Project project, String name) throws OperationNotAllowedException {
		checkIfLoggedInProjectLeader(project);
		company.checkIfValidProjectName(name);
		project.setName(name);
	}

	// Per Lange Laursen - s144456
	public void changePassword(String password) throws OperationNotAllowedException {
		if(company.getLoggedInEmployee() == this) {
			this.password = password;
		} else {
			throw new OperationNotAllowedException("Employee Not Logged In", "Employee Change Password");
		}
	}
}
