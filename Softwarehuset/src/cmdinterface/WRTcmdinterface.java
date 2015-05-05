package cmdinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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

	private static Address address = new Address("Kongens Lyngby",
			"Anker Engelunds Vej", 101);
	private static Company company = new Company("Softwarehuset A/S", address);
	private static Executive executive = new Executive("ex01", "Executive",
			company, "password");
	private static Employee e, e2;
	private static Project p, p2;
	static BufferedReader input = new BufferedReader(new InputStreamReader(
			System.in));

	public static void main(String[] args) throws IOException,
			OperationNotAllowedException {
		// Setup
		company.executiveLogin("password");
		e = company.createEmployee("PRLR", "password", "Software");
		e2 = company.createEmployee("AKMU", "password", "Software");
		p = company.createProject("Project 3");
		p2 = company.createProject("Project 4");
		p.assignProjectLeader(e);
		company.employeeLogout();
		company.employeeLogin("PRLR", "password");
		e.assignEmployeeProject("AKMU", "Project 3");
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
		if (commands[0].toLowerCase().equals("add")
				&& commands[1].toLowerCase().equals("employee")) {
			addEmployee();
		}

		if (commands[0].toLowerCase().equals("create")
				&& commands[1].toLowerCase().equals("project")) {
			createProject();
		}

		if (commands[0].toLowerCase().equals("assign")
				&& commands[1].toLowerCase().equals("project")
				&& commands[2].toLowerCase().equals("leader")) {
			assignProjectLeader();
		}

		if (commands[0].toLowerCase().equals("log")
				&& commands[1].toLowerCase().equals("out")) {
			company.employeeLogout();
			initialScreen();
		}
		if (commands[0].toLowerCase().equals("shut")
				&& commands[1].toLowerCase().equals("down")
				&& commands[2].toLowerCase().equals("the")
				&& commands[3].toLowerCase().equals("system")) {
			System.exit(0);
		}

	}

	private void assignProjectLeader() throws IOException,OperationNotAllowedException {
		Employee projectLeader = findEmployee();
		Project currentProject = findProject();

		executive.assignProjectLeader(projectLeader.getID(), currentProject.getID());
		System.out.println(projectLeader.getID()
				+ " is now project leader for project "
				+ currentProject.getID());
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
		while(repeat){
		System.out.println("Do you want to add dates to project (Yes or No)?");
		String choice = input.readLine();

		if (choice.toLowerCase().equals("yes")) {
			repeat = false;
			GregorianCalendar start = checkStartDate();
			GregorianCalendar end = checkEndDate();

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
			repeat = false;
			Project p = company.createProject(projectName);
			System.out.println("Project created: " + p.getID());
			System.out.println();
			executiveScreen();
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

	private void initialScreen() throws IOException,
			OperationNotAllowedException {
		System.out.println("Work Registration Tool 0.1");
		System.out.println("Softwarehuset A/S 2015");
		System.out
				.println("To login type \"Login\" with the username and a password (Regular user)");
		System.out
				.println("To login as executive type \"Login\" with just a password");
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

	private void employeeScreen() throws IOException,OperationNotAllowedException {

		System.out.println("[User: " + company.getLoggedInEmployee().getID()+ " " + company.getLoggedInEmployee().getDepartment() + "]");
		System.out.println("Employee options");
		System.out.println("- Ask colleague for assistance");
		System.out.println("- Remove assisting colleague");
		System.out.println("- Register spent time");
		System.out.println("- Register vacation, sick-days and course attendance");
		System.out.println("- See registered spent time");
		System.out.println("- Manage projects");
		System.out.println();
		System.out.println("Log out");

		String userChoice = input.readLine();
		if (userChoice.toLowerCase().equals("register spent time")) {
			registerSpentTime();
		} else if (userChoice.toLowerCase().equals(
				"ask colleague for assistance")) {
			askColleagueForAssistance();
		} else if (userChoice.toLowerCase()
				.equals("remove assisting colleague")) {
			removeAssistingColleague();
		} else if (userChoice.toLowerCase().equals(
				"register vacation, sick-days and course attendance")) {
			registerVSC();
		} else if (userChoice.toLowerCase().equals("see registered spent time")) {
			registeredSpentTime();
		} else if (userChoice.toLowerCase().equals("manage projects")) {
			manageProject();
		} else if (userChoice.toLowerCase().equals("log out")) {
			company.employeeLogout();
			initialScreen();
		} else {
			System.out.println("Incorrect command. Try Again.\n");
			employeeScreen();
		}
	}

	private void manageProject() throws IOException,
			OperationNotAllowedException {
		Project project = findProject();
		manageProjectScreen(project);
	}

	private void manageProjectScreen(Project project) throws IOException,
			OperationNotAllowedException {
		System.out.println("Manage project: " + project.getID());
		System.out.println("- Assign employee to project");
		System.out.println("- Assign employee to activity");
		System.out.println("- Create an activity");
		System.out.println("- Get Project Statistics");
		System.out.println("- Change Project Dates");
		System.out.println("- Relieve employee from project");
		System.out.println("- See available employees");
		System.out.println("- Create reports on project meetings");
		System.out.println("- View report from project meeting");
		System.out.println("- Return to employee screen");
		System.out.println();

		String userChoice = input.readLine();
		if (userChoice.toLowerCase().equals("assign employee to project")) {
			assignEmployeeProject(project);
		} else if (userChoice.toLowerCase().equals(
				"assign employee to activity")) {
			assignEmployeeActivity();
		} else if (userChoice.toLowerCase().equals("create an activity")) {
			createActivity(project);
		} else if (userChoice.toLowerCase().equals("get Project Statistics")) {
			getStatistics(project);
		} else if (userChoice.toLowerCase().equals("change Project Dates")) {
			changeProjectDates(project);
		} else if (userChoice.toLowerCase().equals(
				"relieve employee from project")) {
			relieveEmployeeProject(project);
		} else if (userChoice.toLowerCase().equals("see available employees")) {
			seeAvailableEmployees();
		} else if (userChoice.toLowerCase().equals(
				"create reports on project meetings")) {
			reportsOnProjectMeetings(project);
		} else if (userChoice.toLowerCase().equals(
				"view report from project meeting")) {
			viewReport(project);
		} else if (userChoice.toLowerCase().equals("return to employee screen")) {
			employeeScreen();
		} else {
			System.out.println("Incorrect command. Try Again.\n");
			employeeScreen();
		}
	}

	private void changeProjectDates(Project p) throws IOException,
			OperationNotAllowedException {
		String project = p.getName();

		GregorianCalendar start = checkStartDate();
		GregorianCalendar end = checkEndDate();

		company.getProject(project).setStart(start);
		company.getProject(project).setEnd(end);

		manageProjectScreen(p);
	}

	private void viewReport(Project p) throws IOException,
			OperationNotAllowedException {
		System.out.print("Enter Report Name: ");
		String report = input.readLine();

		System.out.println(p.getReport(report).getContent());
		manageProjectScreen(p);
	}

	private void reportsOnProjectMeetings(Project p) throws IOException,
			OperationNotAllowedException {
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

		company.getLoggedInEmployee().editReport(p.getReport(report),
				content);

		manageProjectScreen(p);
	}

	private void seeAvailableEmployees() throws IOException,
			OperationNotAllowedException {
		System.out.print("Enter Period Start Date: ");
		int startDate = Integer.parseInt(input.readLine());

		System.out.print("Enter Period Start Month: ");
		int startMonth = Integer.parseInt(input.readLine());

		System.out.print("Enter Period Start Year: ");
		int startYear = Integer.parseInt(input.readLine());

		GregorianCalendar start = new GregorianCalendar(startYear, startMonth,
				startDate);

		System.out.print("Enter Period End Date: ");
		int endDate = Integer.parseInt(input.readLine());

		System.out.print("Enter Period End Month: ");
		int endMonth = Integer.parseInt(input.readLine());

		System.out.print("Enter Period End Year: ");
		int endYear = Integer.parseInt(input.readLine());

		GregorianCalendar end = new GregorianCalendar(endYear, endMonth,
				endDate);

		List<Employee> employees = company.getAvailableEmployees(start, end);

		System.out.println("Available Employees with period:" + startDate + "/"
				+ startMonth + "/" + startYear + " " + endDate + "/" + endMonth
				+ "/" + endYear);
		for (Employee e : employees) {
			System.out.println("ID: " + e.getID() + " Department: "
					+ e.getDepartment());
		}

		manageProjectScreen(p);
	}

	private void registeredSpentTime() throws IOException,
			OperationNotAllowedException {
		System.out.print("Enter Activity ID: ");
		String activity = input.readLine();

		try {
			int time = company.getLoggedInEmployee().getSpentTime(activity);
			System.out.println(activity + ": "
					+ company.getLoggedInEmployee().getSpentTime(activity)+ " half hour(s)");
			System.out.println();
		} catch (Exception e) {
			System.out.println("" + e.getMessage());
			System.out.println();
			registeredSpentTime();
		}
		employeeScreen();
	}

	private void removeAssistingColleague() throws IOException,
			OperationNotAllowedException {
		System.out.print("Enter Project Name: ");
		String project = input.readLine();

		System.out.print("Enter Activity Name: ");
		String activity = input.readLine();

		System.out.print("Enter Employee ID: ");
		String id = input.readLine();
		Employee em = company.getEmployee(id);

		if (em != null) {
			company.getLoggedInEmployee().removeAssistingEmployee(em,
					company.getProject(project).getActivity(activity));
			System.out.println("Assisting Employee Removed");
		}

		employeeScreen();
	}

	private void askColleagueForAssistance() throws IOException,
			OperationNotAllowedException {
		System.out.print("Enter Project Name: ");
		String project = input.readLine();

		System.out.print("Enter Activity Name: ");
		String activity = input.readLine();

		System.out.print("Enter Employee ID: ");
		String id = input.readLine();
		Employee em = company.getEmployee(id);

		if (em != null) {
			company.getLoggedInEmployee().requestAssistance(em,
					company.getProject(project).getActivity(activity));
			System.out.println("Employee added to assist.");
		}

		employeeScreen();
	}

	private void registerVSC() throws IOException, OperationNotAllowedException {
		System.out.println("Avaliable types of Activity:");
		System.out.println("- Vacation");
		System.out.println("- Sick");
		System.out.println("- Course");
		System.out.println();
		System.out.print("Enter type of Activity: ");

		String type = input.readLine();

		System.out.print("Enter Start Date: ");
		int startDate = Integer.parseInt(input.readLine());

		System.out.print("Enter Start Month: ");
		int startMonth = Integer.parseInt(input.readLine());

		System.out.print("Enter Start Year: ");
		int startYear = Integer.parseInt(input.readLine());

		System.out.print("Enter End Date: ");
		int endDate = Integer.parseInt(input.readLine());

		System.out.print("Enter End Month: ");
		int endMonth = Integer.parseInt(input.readLine());

		System.out.print("Enter End Year: ");
		int endYear = Integer.parseInt(input.readLine());

		if (type.toLowerCase().equals("vacation")) {
			company.getLoggedInEmployee().registerVacationTime(startYear,
					startMonth, startDate, endYear, endMonth, endDate);
		}

		if (type.toLowerCase().equals("sick")) {
			company.getLoggedInEmployee().registerSickTime(startYear,
					startMonth, startDate, endYear, endMonth, endDate);
		}

		if (type.toLowerCase().equals("course")) {
			company.getLoggedInEmployee().registerCourseTime(startYear,
					startMonth, startDate, endYear, endMonth, endDate);
		}

		employeeScreen();
	}

	private void registerSpentTime() throws IOException,
			OperationNotAllowedException {
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
				System.out.println("The spent time on the activity \""
						+ activity + "\" has been set to " + time);
				System.out.println();
				repeat = false;
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
			}
		}
		employeeScreen();
	}

	private void relieveEmployeeProject(Project p) throws IOException,
			OperationNotAllowedException {
		System.out.print("Enter Employee ID: ");
		String id = input.readLine();
		Employee em = company.getEmployee(id);

		if (em != null) {
			company.getLoggedInEmployee().relieveEmployeeProject(em, p);
			System.out.println("Employee relieved form project");
		}
		manageProjectScreen(p);
	}

	private void getStatistics(Project p) throws OperationNotAllowedException, IOException {
		List<String> statistics = company.getLoggedInEmployee().getStatisticsProject(company.getProject(p.getID()));

		for (String s : statistics) {
			System.out.println(s);
		}
		manageProjectScreen(p);
	}

	private void createActivity(Project p) throws NumberFormatException,
			IOException, OperationNotAllowedException {
		boolean repeat = true;
		while (repeat) {
			System.out.print("Enter activity title: ");
			String activity = input.readLine();

			GregorianCalendar start = checkStartDate();
			GregorianCalendar end = checkEndDate();

			try {
				company.getLoggedInEmployee().createActivity(p, activity,
						start, end);
				System.out.println("The activity " + p.getID() + "-" + activity
						+ " has been created");
				System.out.println();
				repeat = false;
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
				System.out.println();
			}
		}
		manageProjectScreen(p);
	}

	private void assignEmployeeActivity() throws IOException,OperationNotAllowedException {
		boolean repeat = true;
		while(repeat){
			System.out.print("Enter Employee ID: ");
			String id = input.readLine();

			System.out.print("Enter Activity ID: ");
			String activity = input.readLine();

			try {
				company.getLoggedInEmployee().assignEmployeeActivity(id, activity);
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
			System.out.println(employee.getID()
					+ " has been assigned to project " + p.getID());
			System.out.println();
		} catch (Exception e) {
			System.out.println("" + e.getMessage());
			System.out.println();
		}

		manageProjectScreen(p);
	}

	private Employee findEmployee() throws IOException,	OperationNotAllowedException {
		while(true){
			System.out.print("Enter Employee ID: ");
			String id = input.readLine();
			try {
				Employee projectLeader = company.getEmployee(id);
				return projectLeader;
			} catch (Exception e){
				System.out.println(""+e.getMessage());
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
				int ID = Integer.parseInt(projectInput);
				project = company.getProject(ID);
			} else {
				try{
					project = company.getProject(projectInput);
				} catch (Exception e){
					System.out.println(""+e.getMessage());
					System.out.println();
				}
			}
			if (project != null) {
				if (project.getProjectLeader() == company.getLoggedInEmployee()	|| company.executiveIsLoggedIn()) {
					return project;
				}
				System.out.println("You are not allowed to manage the chosen project");
				System.out.println();
			}
		}
	}

	private GregorianCalendar checkEndDate() throws IOException {
		int endYear = 0;
		int endWeek = 0; 
		int endDate = 0;
		int endMonth = 0;
		boolean repeat = true;
		while (repeat) {
			System.out.print("Enter end year: ");
			String year = input.readLine();
			while (!year.matches("[0-9]+")) {
				System.out.println("Year must be a number");
				System.out.print("Enter start year: ");
				year = input.readLine();
			}
			endYear = Integer.parseInt(year);

			System.out.print("Enter end week: ");
			String week = input.readLine();
			while (!week.matches("[0-9]+")) {
				System.out.println("Week must be a number");
				System.out.print("Enter start week: ");
				week = input.readLine();
			}
			endWeek = Integer.parseInt(week);

			GregorianCalendar g = new GregorianCalendar();
			g.clear();
			g.set(Calendar.YEAR, endYear);
			g.set(Calendar.WEEK_OF_YEAR, endWeek);
			g.add(Calendar.DAY_OF_YEAR, 6);
			;
			endMonth = g.get(Calendar.MONTH);
			endDate = g.get(Calendar.DAY_OF_MONTH);

			try {
				company.checkForInvalidDate(endYear, endMonth, endDate);
				System.out.println();
				repeat = false;
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
				System.out.println();
			}
		}
		GregorianCalendar end = new GregorianCalendar(endYear, endMonth, endDate, 0, 0, 0);
		return end;
	}

	private GregorianCalendar checkStartDate() throws IOException {
		int startYear = 0;
		int startWeek = 0;
		int startDate = 0;
		int startMonth = 0;
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

			GregorianCalendar g = new GregorianCalendar();
			g.clear();
			g.set(Calendar.YEAR, startYear);
			g.set(Calendar.WEEK_OF_YEAR, startWeek);
			startMonth = g.get(Calendar.MONTH);
			startDate = g.get(Calendar.DAY_OF_MONTH);

			try {
				company.checkForInvalidDate(startYear, startMonth, startDate);
				System.out.println();
				repeat = false;
			} catch (Exception e) {
				System.out.println("" + e.getMessage());
				System.out.println();
			}
		}
		GregorianCalendar start = new GregorianCalendar(startYear, startMonth, startDate, 0, 0, 0);
		return start;
	}
}
