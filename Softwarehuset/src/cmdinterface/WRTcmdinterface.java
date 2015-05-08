package cmdinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import softwarehuset.*;

/**
 * @author Gruppe 25
 *
 */
public class WRTcmdinterface {

	/**
	 * @param args
	 * @throws IOException
	 * @throws OperationNotAllowedException
	 */

	private static Address address = new Address("Kongens Lyngby","Anker Engelunds Vej", 101);
	private static Company company = new Company("Softwarehuset A/S", address);
	private static Executive executive = new Executive("ex01", "Executive",	company, "password");
	private static Employee e1, e2;
	private static Project p1, p2;
	static BufferedReader input = new BufferedReader(new InputStreamReader(
			System.in));

	public static void main(String[] args) throws IOException,
			OperationNotAllowedException {
		// Setup: PRLR is project leader of Project 3 (150001)
		// AKMU is assigned to Project 3's activity 150001-Testing
		company.executiveLogin("password");
		e1 = company.createEmployee("PRLR", "password", "Software");
		e2 = company.createEmployee("AKMU", "password", "Software");
		p1 = company.createProject("Project 3");
		p2 = company.createProject("Project 4");
		executive.assignProjectLeader("PRLR", 150001);
		company.employeeLogout();
		company.employeeLogin("PRLR", "password");
		e1.assignEmployeeProject("AKMU", "Project 3");
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		e1.createActivity(p1, "Testing", start, end, 3);
		e1.assignEmployeeActivity("AKMU", "150001-Testing");
		company.employeeLogout();

		// Print initial UI
		new WRTcmdinterface().initialScreen();
	}

	private void executiveScreen() throws IOException,
			OperationNotAllowedException {
		System.out.println("[Executive logged in]");
		System.out.println("Executive options");
		System.out.println("- Add Employee (Company Database)");
		System.out.println("- Create Project");
		System.out.println("- Assign Project Leader");
		System.out.println("- Log out");
		System.out.println("- Shut down the system");
		System.out.println();
		String inputString = input.readLine();
		String[] commands = inputString.split(" ");
		if (commands[0].toLowerCase().equals("add")	&& commands[1].toLowerCase().equals("employee")) {
			addEmployee();
		} else if (commands[0].toLowerCase().equals("create") && commands[1].toLowerCase().equals("project")) {
			createProject();
		} else if (commands[0].toLowerCase().equals("assign")&& commands[1].toLowerCase().equals("project")	&& commands[2].toLowerCase().equals("leader")) {
			assignProjectLeader();
		} else if (commands[0].toLowerCase().equals("log")	&& commands[1].toLowerCase().equals("out")) {
			company.employeeLogout();
			initialScreen();
		} else if (commands[0].toLowerCase().equals("shut")	&& commands[1].toLowerCase().equals("down")	&& commands[2].toLowerCase().equals("the")&& commands[3].toLowerCase().equals("system")) {
			System.exit(0);
		} else {
			System.out.println("Wrong Command");
			System.out.println();
			executiveScreen();
		}

	}

	private void assignProjectLeader() throws IOException,OperationNotAllowedException {
		Employee projectLeader = findEmployee();
		Project currentProject = findProject();
		executive.assignProjectLeader(projectLeader.getID(),currentProject.getID());
		System.out.println(projectLeader.getID()+ " is now project leader for project "+ currentProject.getID());
		System.out.println();
		executiveScreen();
	}

	private void createProject() throws IOException,
			OperationNotAllowedException {
		String projectName = "";
		while (projectName.equals("")) {
			System.out.print("Enter project name: ");
			projectName = input.readLine();

			if (projectName.equals("")) {
				System.out.println("Missing information: Name");
				System.out.println();
			}
		}
		boolean repeat = true;
		while (repeat) {
			System.out
					.println("Do you want to add dates to project (Yes or No)?");
			String choice = input.readLine();

			if (choice.toLowerCase().equals("yes")) {
				repeat = false;
				GregorianCalendar start = getStartDate();
				GregorianCalendar end = getEndDate();

				try {
					Project p = company.createProject(projectName, start, end);
					System.out.println("Project created: " + p.getID());
					System.out.println();
					executiveScreen();
				} catch (Exception e) {
					System.out.println("" + e.getMessage());
					System.out.println("Project could not be created");
					System.out.println();
					createProject();
				}
			} else if (choice.toLowerCase().equals("no")) {
				try {
					Project p = company.createProject(projectName);
					System.out.println("Project created: " + p.getID());
					System.out.println();
					repeat = false;
					executiveScreen();
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println();
					createProject();
				}
			}
		}
	}

	private void addEmployee() throws IOException, OperationNotAllowedException {
		boolean isRunning = true;
		while (isRunning) {
			try {
				System.out.print("Enter Employee ID: ");
				String id = input.readLine();
				System.out.print("Enter Deparment: ");
				String department = input.readLine();
				company.createEmployee(id, "password", department);
				System.out.println("An employee with the ID \"" + id
						+ "\" was created");
				System.out.println("Default password for " + id
						+ " is \"password\"");
				System.out.println();
				isRunning = false;
			} catch (Exception e) {
				System.out.println("The ID must be 4 letters long");
				System.out.println();
			}
		}
		executiveScreen();
	}

	private void initialScreen() throws IOException, OperationNotAllowedException {
		System.out.println("Work Registration Tool 0.1");
		System.out.println("Softwarehuset A/S 2015");
		System.out.println("To login type \"Login\" with the username and a password (Regular user)");
		System.out.println("To login as executive type \"Login\" with just a password");
		System.out.println();
		String inputString = input.readLine();
		String[] commands = inputString.split(" ");

		if (commands[0].toLowerCase().equals("login") && commands.length == 3) {
			company.employeeLogin(commands[1], commands[2]);
			if (company.getLoggedInEmployee() != null) {
				employeeScreen();
			}
			System.out.println("Incorrect ID or password");
			System.out.println();
			initialScreen();
		}
		if (commands[0].toLowerCase().equals("login") && commands.length == 2) {
			company.executiveLogin(commands[1]);
			if (company.executiveIsLoggedIn()) {
				executiveScreen();
			}
			System.out.println("Incorrect password");
			System.out.println();
			initialScreen();
		}
	}

	private void employeeScreen() throws IOException, OperationNotAllowedException {
		System.out.println("______________");
		System.out.println("[User: " + company.getLoggedInEmployee().getID()+ " " + company.getLoggedInEmployee().getDepartment() + "]");
		System.out.println("Employee options"+"\n");
		System.out.println("- See projects");
		System.out.println("- See activities"+"\n");
		
		System.out.println("- Register spent time");
		System.out.println("- See registered spent time");
		System.out.println("- Register other activities"+"\n");
		
		System.out.println("- Ask colleague for assistance");
		System.out.println("- Remove assisting colleague"+"\n");
		
		System.out.println("- Manage projects"+"\n");
		System.out.println();
		System.out.println("Log out");
		System.out.println("______________");
		
		String userChoice = input.readLine();
		if (userChoice.toLowerCase().equals("register spent time")) {
			registerSpentTime();
		} else if (userChoice.toLowerCase().equals("ask colleague for assistance")) {
			askColleagueForAssistance();
		} else if (userChoice.toLowerCase().equals("remove assisting colleague")) {
			removeAssistingColleague();
		} else if (userChoice.toLowerCase().equals("register other activities")) {
			registerVSC();
		} else if (userChoice.toLowerCase().equals("see registered spent time")) {
			registeredSpentTime();
		} else if (userChoice.toLowerCase().equals("manage projects")) {
			manageProject();
		} else if (userChoice.toLowerCase().equals("see projects")) {
			seeProjects();
		} else if (userChoice.toLowerCase().equals("see activities")) {
			seeActivities();
		} else if (userChoice.toLowerCase().equals("log out")) {
			company.employeeLogout();
			initialScreen();
		} else {
			System.out.println("Incorrect command. Try Again.\n");
			employeeScreen();
		}
	}

	private void manageProject() throws IOException, OperationNotAllowedException {
		Project project = findProject();
		manageProjectScreen(project);
	}

	private void manageProjectScreen(Project project) throws IOException, OperationNotAllowedException {
		System.out.println("______________");
		System.out.println("Manage project: " + project.getID()+" "+ project.getName()+"\n");
		System.out.println("- Create activity");
		System.out.println("- Change project name");
		System.out.println("- Change project dates");
		System.out.println("- Change activity name");
		System.out.println("- Change activity dates");
		System.out.println("- Change expected time on activity"+"\n");
		
		System.out.println("- Assign employee to project");
		System.out.println("- Assign employee to activity");
		System.out.println("- Relieve employee from project"+"\n");
		
		System.out.println("- See spent time on project");
		System.out.println("- See spent time on activity");
		System.out.println("- See expected time on activity");
		System.out.println("- Get project statistics");
		System.out.println("- Create reports on project meetings");
		System.out.println("- View report from project meeting");
		System.out.println("- See available employees"+"\n");		
		
		System.out.println("- Return to employee screen");
		System.out.println("______________");
		System.out.println();

		String userChoice = input.readLine();
		if (userChoice.toLowerCase().equals("assign employee to project")) {
			assignEmployeeProject(project);
		} else if (userChoice.toLowerCase().equals("assign employee to activity")) {
			assignEmployeeActivity(project);
		} else if (userChoice.toLowerCase().equals("create activity")) {
			createActivity(project);
		} else if (userChoice.toLowerCase().equals("get project statistics")) {
			getStatistics(project);
		} else if (userChoice.toLowerCase().equals("change project dates")) {
			changeProjectDates(project);
		} else if (userChoice.toLowerCase().equals("change project name")) {
			changeProjectName(project);
		} else if (userChoice.toLowerCase().equals("change activity dates")) {
			changeActivityDates(project);
		} else if (userChoice.toLowerCase().equals("change activity name")) {
			changeActivityName(project);
		} else if (userChoice.toLowerCase().equals("change expected time on activity")) {
			changeExpectedTime(project);
		} else if (userChoice.toLowerCase().equals("relieve employee from project")) {
			relieveEmployeeProject(project);
		}else if (userChoice.toLowerCase().equals("see spent time on project")) {
			seeSpentTimeOnProject(project);
		}else if (userChoice.toLowerCase().equals("see spent time on activity")) {
			seeSpentTimeOnActivity(project);
		}else if (userChoice.toLowerCase().equals("see expected time on activity")) {
			seeExpectedTime(project);
		} else if (userChoice.toLowerCase().equals("see available employees")) {
			seeAvailableEmployees(project);
		} else if (userChoice.toLowerCase().equals("create reports on project meetings")) {
			reportsOnProjectMeetings(project);
		} else if (userChoice.toLowerCase().equals("view report from project meeting")) {
			viewReport(project);
		} else if (userChoice.toLowerCase().equals("return to employee screen")) {
			employeeScreen();
		} else {
			System.out.println("Incorrect command. Try Again.\n");
			manageProjectScreen(project);
		}
	}


	private void seeProjects() throws IOException, OperationNotAllowedException {
		HashSet<Project> projects = company.getLoggedInEmployee().getProjects();
		for (Project p : projects) {
			System.out.println("- " + p.getID() + ": " + p.getName());
		}
		System.out.println();
		employeeScreen();
	}

	private void seeActivities() throws IOException, OperationNotAllowedException {
		Set<Activity> activities = company.getLoggedInEmployee().getActivities();
		for (Activity s : activities) {
			System.out.println("- " + s.getName());
		}
		System.out.println();
		employeeScreen();
	}
	

	private void seeSpentTimeOnActivity(Project p) throws IOException, OperationNotAllowedException {
		Activity a = findActivity(p);
		System.out.println("Time: "+a.getAllSpentTime()+" half hour(s)"+"\n");
		manageProjectScreen(p);
	}

	private void seeSpentTimeOnProject(Project p) throws IOException, OperationNotAllowedException {
		System.out.println("Time: "+p.getTotalSpentTime()+" half hour(s)"+"\n");
		manageProjectScreen(p);
	}
	
	private void changeActivityName(Project p) throws IOException, OperationNotAllowedException {
		boolean repeat = true;
		Activity activity;
		while(repeat){
			System.out.print("Enter activity ID: ");
			String activityName = input.readLine();
			
			System.out.print("Enter new name: ");
			String newName = input.readLine();
			try{
				activity = p.getActivity(activityName);
				company.getLoggedInEmployee().changeActivityName(activity, newName);
				System.out.println("The activity has successfully been updated");
				System.out.println();
				repeat = false;
			} catch(Exception e){
				System.out.println(e.getMessage());
				System.out.println();
			}
		}

		manageProjectScreen(p);
	}

	
	private void changeProjectName(Project p) throws IOException, OperationNotAllowedException {
		boolean repeat = true;
		while(repeat){
			System.out.print("Enter new project name: ");
			String newName = input.readLine();
			
			try{
				company.getLoggedInEmployee().changeProjectName(p, newName);
				System.out.println("The project has successfully been updated");
				System.out.println();
				repeat = false;
			} catch(Exception e){
				System.out.println(e.getMessage());
				System.out.println();
			}
		}

		manageProjectScreen(p);
	}
	
	private void changeActivityDates(Project p) throws IOException,OperationNotAllowedException {
		boolean repeat = true;
		String activity="";
		while(repeat){
			activity = findActivity(p).getName();
			GregorianCalendar start = getStartDate();
			GregorianCalendar end = getEndDate();
			try{
				company.getLoggedInEmployee().changeActivityStart(activity, start);
				company.getLoggedInEmployee().changeActivityEnd(activity, end);
				System.out.println("The activity has successfully been updated");
				System.out.println();
				repeat = false;
			} catch (Exception e){
				System.out.println(e.getMessage());
				System.out.println();
			}
		}

		manageProjectScreen(p);
	}


	private void changeProjectDates(Project p) throws IOException, OperationNotAllowedException {
		String project = p.getName();
		boolean repeat = true;
		while(repeat){
			GregorianCalendar start = getStartDate();
			GregorianCalendar end = getEndDate();
			try{
				company.getLoggedInEmployee().changeProjectStart(project, start);
				company.getLoggedInEmployee().changeProjectEnd(project, end);
				System.out.println("The project has successfully been updated");
				System.out.println();
				repeat = false;
			} catch (Exception e){
				System.out.println(e.getMessage());
				System.out.println();
			}
		}

		manageProjectScreen(p);
	}
	

	private void changeExpectedTime(Project p) throws IOException, OperationNotAllowedException {
		Activity a = findActivity(p);
		
		System.out.print("Enter expected time (in weeks): ");
		String timeInput = input.readLine();
		while (!timeInput.matches("[0-9]+")) {
			System.out.println("Expected time must be a positive number");
			System.out.print("Enter expected time (in weeks): ");
			timeInput = input.readLine();
		}
		int time = Integer.parseInt(timeInput);
		try{
			company.getLoggedInEmployee().changeActivityExpectedTime(a.getName(), time);
			System.out.println("Expected time has been changed");
		} catch (Exception e){
			System.out.println(""+e.getMessage());
			System.out.println();
		}

		manageProjectScreen(p);
	}
	
	private void seeExpectedTime(Project p) throws IOException, OperationNotAllowedException{
		Activity a = findActivity(p);
		System.out.println("Expected time: "+a.getExpectedTime()+" weeks");
		manageProjectScreen(p);
	}

	private void viewReport(Project p) throws IOException,OperationNotAllowedException {
		System.out.print("Enter Report Name: ");
		String report = input.readLine();

		System.out.println(p.getReport(report).getContent());
		manageProjectScreen(p);
	}

	private void reportsOnProjectMeetings(Project p) throws IOException, OperationNotAllowedException {
		System.out.print("Enter Report Name: ");
		String report = input.readLine();

		System.out.print("Enter Report Date: ");
		int date = Integer.parseInt(input.readLine());

		System.out.print("Enter Report Month: ");
		int month = Integer.parseInt(input.readLine());

		System.out.print("Enter Report Year: ");
		int year = Integer.parseInt(input.readLine());

		company.getLoggedInEmployee().writeReport(p, report, year, month, date);
		System.out.println("Report created");

		System.out.println("Enter Report Content:");
		String content = input.readLine();

		company.getLoggedInEmployee().editReport(p.getReport(report), content);
		
		manageProjectScreen(p);
	}

	private void seeAvailableEmployees(Project p) throws IOException, OperationNotAllowedException {
		GregorianCalendar start = getStartDate();
		GregorianCalendar end = getEndDate();

		List<Employee> employees = company.getAvailableEmployees(start, end);

		System.out.println("Available Employees within the given period:");
		for (Employee e : employees) {
			System.out.println("- ID: " + e.getID() + " Department: "	+ e.getDepartment());
		}
		System.out.println();
		manageProjectScreen(p);
	}

	private void registeredSpentTime() throws IOException, OperationNotAllowedException {
		Set<Activity> activities = company.getLoggedInEmployee().getActivities();

		for (Activity s : activities) {
			System.out.println(s.getName() + ": " + s.getSpentTime(company.getLoggedInEmployee()) + " half hour(s)");
		}
		System.out.println();
		employeeScreen();
	}

	private void removeAssistingColleague() throws IOException, OperationNotAllowedException {
		boolean repeat = true;
		while(repeat){
			System.out.print("Enter Project Name: ");
			String project = input.readLine();

			System.out.print("Enter Activity Name: ");
			String activity = input.readLine();

			System.out.print("Enter Employee ID: ");
			String id = input.readLine();
			Employee em = company.getEmployee(id);
		
			try{
				company.getLoggedInEmployee().removeAssistingEmployee(em,company.getProject(project).getActivity(activity));
				System.out.println("Assisting Employee Removed");
				repeat = false;
			} catch (Exception e){
				System.out.println(""+e.getMessage());
				System.out.println();
			}
		}

		employeeScreen();
	}

	private void askColleagueForAssistance() throws IOException,OperationNotAllowedException {
		System.out.print("Enter Project Name: ");
		String project = input.readLine();

		System.out.print("Enter Activity Name: ");
		String activity = input.readLine();

		System.out.print("Enter Employee ID: ");
		String id = input.readLine();
		Employee em = null;
		try {
			em = company.getEmployee(id);
		} catch (Exception e) {
			System.out.println("" + e.getMessage());
			System.out.println();
		}

		if (em != null) {
			try {
				company.getLoggedInEmployee().requestAssistance(em,
						company.getProject(project).getActivity(activity));
				System.out.println("Employee added to assist.");
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
				System.out.println();
			}
		}

		employeeScreen();
	}

	private void registerVSC() throws IOException, OperationNotAllowedException {
		System.out.println("Avaliable types of Activity:");
		System.out.println("- Vacation");
		System.out.println("- Sick");
		System.out.println("- Course");
		System.out.println();
		boolean repeat = true;
		
		while(repeat){
			System.out.print("Enter type of Activity: ");
			String type = input.readLine();
		
			int startDate, startMonth, startYear, endDate, endMonth, endYear;
		
			if (type.toLowerCase().equals("vacation")) {
				startDate = getSpecificStartDate();
				startMonth = getSpecificStartMonth();
				startYear = getSpecificStartYear();
				endDate = getSpecificEndDate();
				endMonth = getSpecificEndMonth();
				endYear = getSpecificEndYear();
				
				try{
				company.getLoggedInEmployee().registerVacationTime(startYear, startMonth, startDate, endYear, endMonth, endDate);
				System.out.println("Vacation has successfully been registered");
				System.out.println();
				repeat = false;
				} catch(Exception e){
					System.out.println(""+e.getMessage());
					System.out.println();
				}
			} else if (type.toLowerCase().equals("sick")) {
				startDate = getSpecificStartDate();
				startMonth = getSpecificStartMonth();
				startYear = getSpecificStartYear();
				endDate = getSpecificEndDate();
				endMonth = getSpecificEndMonth();
				endYear = getSpecificEndYear();
				try {
				company.getLoggedInEmployee().registerSickTime(startYear,startMonth, startDate, endYear, endMonth, endDate);
				System.out.println("Sick days have successfully been registered");
				System.out.println();
				repeat = false;
				} catch(Exception e){
					System.out.println(""+e.getMessage());
					System.out.println();
				}
			} else if (type.toLowerCase().equals("course")) {
				startDate = getSpecificStartDate();
				startMonth = getSpecificStartMonth();
				startYear = getSpecificStartYear();
				endDate = getSpecificEndDate();
				endMonth = getSpecificEndMonth();
				endYear = getSpecificEndYear();
				try {
				company.getLoggedInEmployee().registerCourseTime(startYear,startMonth, startDate, endYear, endMonth, endDate);
				System.out.println("Course attendance has successfully been registered");
				System.out.println();
				repeat = false;
				} catch(Exception e){
					System.out.println(""+e.getMessage());
					System.out.println();
				}
			}
		}
		employeeScreen();
	}
	
	private int getSpecificStartDate() throws NumberFormatException, IOException{
		System.out.print("Enter start date: ");
		String inputString = input.readLine();
		while (!inputString.matches("[0-9]+")) {
			System.out.println("Date must be a number");
			System.out.print("Enter start date: ");
			inputString = input.readLine();
		}
		int date = Integer.parseInt(inputString);
		return date;
	}
	
	private int getSpecificStartMonth() throws NumberFormatException, IOException{
		System.out.print("Enter start month: ");
		String inputString = input.readLine();
		while (!inputString.matches("[0-9]+")) {
			System.out.println("Month must be a number");
			System.out.print("Enter start month: ");
			inputString = input.readLine();
		}
		int month = Integer.parseInt(inputString);
		return month;
	}
	
	private int getSpecificStartYear() throws NumberFormatException, IOException{
		System.out.print("Enter start year: ");
		String inputString = input.readLine();
		while (!inputString.matches("[0-9]+")) {
			System.out.println("Year must be a number");
			System.out.print("Enter start year: ");
			inputString = input.readLine();
		}
		int year = Integer.parseInt(inputString);
		return year;
	}	 

	private int getSpecificEndDate() throws NumberFormatException, IOException{
		System.out.print("Enter end date: ");
		String inputString = input.readLine();
		while (!inputString.matches("[0-9]+")) {
			System.out.println("Date must be a number");
			System.out.print("Enter end date: ");
			inputString = input.readLine();
		}
		int date = Integer.parseInt(inputString);
		return date;
	}

	private int getSpecificEndMonth() throws NumberFormatException, IOException{
		System.out.print("Enter end month: ");
		String inputString = input.readLine();
		while (!inputString.matches("[0-9]+")) {
			System.out.println("Month must be a number");
			System.out.print("Enter end month: ");
			inputString = input.readLine();
		}
		int month = Integer.parseInt(inputString);
		return month;
	}

	private int getSpecificEndYear() throws NumberFormatException, IOException{
		System.out.print("Enter end year: ");
		String inputString = input.readLine();
		while (!inputString.matches("[0-9]+")) {
			System.out.println("Year must be a number");
			System.out.print("Enter end year: ");
			inputString = input.readLine();
		}
		int year = Integer.parseInt(inputString);
		return year;
	}
	
	private void registerSpentTime() throws IOException, OperationNotAllowedException {
		boolean repeat = true;
		while (repeat) {
			System.out.print("Enter Activity ID: ");
			String activity = input.readLine();

			System.out.print("Enter the number of half hours: ");
			String timeInput = input.readLine();
			while (!timeInput.matches("[0-9]+")) {
				System.out.println("Spent time must be a number");
				System.out.print("Enter the number of half hours: ");
				timeInput = input.readLine();
			}
			int time = Integer.parseInt(timeInput);

			try {
				company.getLoggedInEmployee().registerSpentTime(activity, time);
				System.out.println(time+" half hour(s) has been added to the spent time on activity \""+ activity);
				System.out.println();
				repeat = false;
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
			}
		}
		employeeScreen();
	}

	private void relieveEmployeeProject(Project p) throws IOException,	OperationNotAllowedException {
		System.out.print("Enter Employee ID: ");
		String id = input.readLine();
		Employee em = company.getEmployee(id);

		if (em != null) {
			company.getLoggedInEmployee().relieveEmployeeProject(em, p);
			System.out.println("Employee relieved form project");
		}
		manageProjectScreen(p);
	}

	private void getStatistics(Project p) throws OperationNotAllowedException,	IOException {
		List<String> statistics = company.getLoggedInEmployee().getStatisticsProject(company.getProject(p.getID()));

		for (String s : statistics) {
			System.out.println(s);
		}
		System.out.println();
		manageProjectScreen(p);
	}

	private void createActivity(Project p) throws NumberFormatException, IOException, OperationNotAllowedException {
		boolean repeat = true;
		while (repeat) {
			System.out.print("Enter activity title: ");
			String activity = input.readLine();

			GregorianCalendar start = getStartDate();
			GregorianCalendar end = getEndDate();

			System.out.print("Enter expected time (in weeks): ");
			String timeInput = input.readLine();
			while (!timeInput.matches("[0-9]+")) {
				System.out.println("Expected time must be a positive number");
				System.out.print("Enter expected time (in weeks): ");
				timeInput = input.readLine();
			}
			int time = Integer.parseInt(timeInput);
			try {
				company.getLoggedInEmployee().createActivity(p, activity,start, end, time);
				System.out.println("The activity " + p.getID() + "-" + activity+ " has been created");
				System.out.println();
				repeat = false;
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
				System.out.println();
			}
		}
		manageProjectScreen(p);
	}

	private void assignEmployeeActivity(Project p) throws IOException,OperationNotAllowedException {
		boolean repeat = true;
		while (repeat) {
			System.out.print("Enter Employee ID: ");
			String id = input.readLine();

			System.out.print("Enter Activity ID: ");
			String activity = input.readLine();

			try {
				company.getLoggedInEmployee().assignEmployeeActivity(id,activity);
				System.out.println(id + " has been assigned to the activity "+ activity);
				System.out.println();
				repeat = false;
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
				System.out.println();
			}
		}
		manageProjectScreen(p);
	}

	private void assignEmployeeProject(Project p)throws OperationNotAllowedException, IOException {
		Employee employee = findEmployee();

		try {
			company.getLoggedInEmployee().assignEmployeeProject(employee.getID(), p.getName());
			System.out.println(employee.getID()+ " has been assigned to project " + p.getID());
			System.out.println();
		} catch (Exception e) {
			System.out.println("" + e.getMessage());
			System.out.println();
		}

		manageProjectScreen(p);
	}

	private Employee findEmployee() throws IOException,
			OperationNotAllowedException {
		while (true) {
			System.out.print("Enter Employee ID: ");
			String id = input.readLine();
			try {
				Employee projectLeader = company.getEmployee(id);
				return projectLeader;
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
				System.out.println();
			}
		}
	}

	private Project findProject() throws IOException, OperationNotAllowedException {
		Project project = null;
		while (true) {
			System.out.print("Enter Project ID: ");
			String projectInput = input.readLine();
			if (projectInput.matches("[0-9]+")) {
				try {
					int ID = Integer.parseInt(projectInput);
					project = company.getProject(ID);
				} catch (Exception e) {
					System.out.println("" + e.getMessage());
					System.out.println();
				}
			} else {
				try {
					project = company.getProject(projectInput);
				} catch (Exception e) {
					System.out.println("" + e.getMessage());
					System.out.println();
					project = null;
				}
			}
			if (project != null) {
				if (project.getProjectLeader() == company.getLoggedInEmployee()
						|| company.executiveIsLoggedIn()) {
					return project;
				}
				System.out.println("You are not allowed to manage the chosen project");
				System.out.println();
			}
		}
	}


	private Activity findActivity(Project p) throws IOException {
		Activity a = null;
		boolean repeat = true;
		while(repeat){
			System.out.print("Enter Activity ID: ");
			String activityName = input.readLine();
			try{
				a = p.getActivity(activityName);
				repeat = false;
			} catch(Exception e){
				System.out.println(e.getMessage());
				System.out.println();
			}
		}
		return a;
	}
	
	private GregorianCalendar getEndDate() throws IOException, OperationNotAllowedException {
		GregorianCalendar end = null;
		int endYear, endWeek;
		boolean repeat = true;
		while (repeat) {
			System.out.print("Enter end year: ");
			String year = input.readLine();
			while (!year.matches("[0-9]+")) {
				System.out.println("Year must be a number");
				System.out.print("Enter end year: ");
				year = input.readLine();
			}
			endYear = Integer.parseInt(year);

			System.out.print("Enter end week: ");
			String week = input.readLine();
			while (!week.matches("[0-9]+")) {
				System.out.println("Week must be a number");
				System.out.print("Enter end week: ");
				week = input.readLine();
			}
			endWeek = Integer.parseInt(week);
			try {
				end = company.convertEndToDate(endYear, endWeek);
				repeat = false;
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
				System.out.println();
			}
		}
		return end;
	}

	private GregorianCalendar getStartDate() throws IOException {
		GregorianCalendar start = null;
		int startYear, startWeek;
		boolean repeat = true;
		while (repeat) {
			System.out.print("Enter start year: ");
			String year = input.readLine();
			while (!year.matches("[0-9]+")) {
				System.out.println("Year must be a number");
				System.out.print("Enter start year: ");
				year = input.readLine();
			}
			startYear = Integer.parseInt(year);

			System.out.print("Enter start week: ");
			String week = input.readLine();
			while (!week.matches("[0-9]+")) {
				System.out.println("Week must be a number");
				System.out.print("Enter start week: ");
				week = input.readLine();
			}
			startWeek = Integer.parseInt(week);
			try {
				start = company.convertStartToDate(startYear, startWeek);
				repeat = false;
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
				System.out.println();
			}
		}
		return start;
	}
}
