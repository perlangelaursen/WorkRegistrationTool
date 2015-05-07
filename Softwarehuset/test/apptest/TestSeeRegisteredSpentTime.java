//Van Anh Thi Trinh - s144449
package apptest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.*;
import org.junit.*;
import softwarehuset.*;

public class TestSeeRegisteredSpentTime {
	Company company;
	Employee projectLeader, employee;
	Project project;
	Executive executive;
	Activity activity, activity2, activity3;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		// Create company, executive, project leader for a project and an employee
		// assigned to the project
		Address address = new Address("City", "Street", 1);
		company = new Company("Softwarehuset", address);
		executive = new Executive("Name", "Department", company, "password");
		company.executiveLogin("password");
		projectLeader = company.createEmployee("HABU", "empassword1", "Department1");
		employee = company.createEmployee("JANK", "empassword2", "Department1");

		// Create project and assign project leader
		project = company.createProject("Project01");
		executive.assignProjectLeader("HABU", project.getID());

		// Create activity
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		activity = projectLeader.createActivity(project, "Designing", start, end,3 );
		activity2 = projectLeader.createActivity(project, "Refactoring", start, end, 3);
		activity3 = projectLeader.createActivity(project, "Programming", start, end, 3);
		
		//Log in as project leader
		company.employeeLogin("HABU", "empassword1");
		
		// Add employee to project and activity
		projectLeader.assignEmployeeProject(employee.getID(), project.getName());
		projectLeader.assignEmployeeActivity(employee.getID(), activity.getName());
		projectLeader.assignEmployeeActivity(employee.getID(), activity2.getName());
		projectLeader.assignEmployeeActivity(employee.getID(), activity3.getName());

		// Register spent time
		company.employeeLogin("JANK", "empassword2");
		employee.registerSpentTime(project.getID()+"-Designing", 100);
		employee.registerSpentTime(project.getID()+"-Refactoring", 50);
		
		//Log out
		company.employeeLogout();
	}

	/**
	 * Tests the scenario where a logged in employee successfully sees spent time on an activity
	 * <ol>
	 * <li>The employee is logged in
	 * <li>The employee see registered time on two activities
	 * </ol>
	 * 
	 */
	
	@Test
	public void testSeeRegisteredSpentTime() throws OperationNotAllowedException {
		//Login
		company.employeeLogin("JANK", "empassword2");

		// See spent time
		assertEquals(100, employee.getSpentTime(project.getID() + "-Designing"));
		assertEquals(50, employee.getSpentTime(project.getID() + "-Refactoring"));
		assertEquals(0, employee.getSpentTime(project.getID() + "-Programming"));
	}
	
	/**
	 * Tests the scenario where an employee tries to see spent time on an activity he is not assigned to
	 * <ol>
	 * <li>The employee is logged in
	 * <li>The employee tries to get spent time on an activity he is not assigned to
	 * <li>An exception is thrown
	 * </ol>
	 * 
	 */
	
	@Test
	public void testSeeRegisteredSpentTimeOnWrongActivity() throws OperationNotAllowedException {
		//Login
		company.employeeLogin("JANK", "empassword2");

		try {
			assertEquals(100, employee.getSpentTime("150309-Designing"));
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Employee is not assigned to the activity", e.getMessage());
			assertEquals("See registered spent time", e.getOperation());
		}
	}
}
