//Van Anh Thi Trinh - s144449

package apptest;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;
import softwarehuset.*;

public class TestRegisterSpentTime {
	Company company;
	Employee projectLeader, employee;
	Executive executive;
	Project project;
	Activity activity;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		// Create company, executive, project leader for a project and employee assigned to the project
		Address address = new Address("City", "Street", 1);
		company = new Company("Softwarehuset", address);
		executive = new Executive("Name", "Department", company, "password");
		company.executiveLogin("password");
		projectLeader = company.createEmployee("LAND", "empassword1", "Department1");
		employee = company.createEmployee("KANO", "empassword2", "Department1");

		// Create project and assign project leader
		project = company.createProject("Project01");
		executive.assignProjectLeader("LAND", project.getID());

		// Create activity
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2015, Calendar.JANUARY, 23);
		end.set(2015, Calendar.FEBRUARY, 23);
		activity = project.createActivity("Designing", start, end,	project);
	}

	/**
	 * Tests the scenario where an employee successfully registers his spent
	 * time on an activity
	 * <ol>
	 * <li>The employee is logged in
	 * <li>The employee registers his spent time on a chosen activity that he is assigned to
	 * </ol>
	 * 
	 */
	
	@Test
	public void testRegisterSpentTime() throws OperationNotAllowedException {
		company.employeeLogin("LAND", "empassword1");
		// Add employee to project and activity
		projectLeader.assignEmployeeProject("LAND", "Project01");
		projectLeader.assignEmployeeActivity(employee.getID(), activity.getName());

		// Register spent time
		company.employeeLogin("KANO", "empassword2");
		employee.registerSpentTime(project.getID()+"-Designing", 100);

		// See spent time
		assertEquals(100, activity.getSpentTime(employee));
	}
	
	/**
	 * Tests the scenario where an employee registers negative time for an activity
	 * <ol>
	 * <li>The employee is logged in
	 * <li>The employee registers negative time on a chosen activity that he is assigned to
	 * <li>An exception is thrown
	 * </ol>
	 * 
	 */
	
	@Test
	public void testRegisterNegativeTime() throws OperationNotAllowedException {
		company.employeeLogin("LAND", "empassword1");
		// Add employee to project and activity
		projectLeader.assignEmployeeProject("LAND", "Project01");
		projectLeader.assignEmployeeActivity(employee.getID(), activity.getName());

		// Try to register spent time
		company.employeeLogin("KANO", "empassword2");
		try {
			employee.registerSpentTime(project.getID()+"-Designing", -1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time", e.getMessage());
			assertEquals("Register spent time", e.getOperation());
		}

		// See spent time
		assertEquals(0, activity.getSpentTime(employee));
	}

	/**
	 * Tests the scenario where an employee tries to register time without being signed in
	 * <ol>
	 * <li>The employee is not logged in
	 * <li>The employee tries to register spent time on a chosen activity
	 * <li>An exception is thrown
	 * </ol>
	 * 
	 */
	
	@Test
	public void testRegisterWithoutLoggingIn() throws Exception {
		try {
			employee.registerSpentTime(project.getID()+"-Designing", 100);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Employee is not logged in", e.getMessage());
			assertEquals("Register spent time", e.getOperation());
		}
	}

	/**
	 * Tests the scenario where an employee tries to register time for an activity that he is not assigned to
	 * <ol>
	 * <li>The employee is logged in
	 * <li>The employee registers his spent time on a chosen activity that he is not assigned to
	 * <li>An exception is thrown
	 * </ol>
	 * 
	 */
	
	@Test
	public void testRegisterTimeforWrongActivity() throws Exception {
		company.employeeLogin("KANO", "empassword2");
		
		try {
			employee.registerSpentTime(project.getID()+"-Designing", 100);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Employee is not assigned to the chosen activity", e.getMessage());
			assertEquals("Register spent time", e.getOperation());
		}
	}
}