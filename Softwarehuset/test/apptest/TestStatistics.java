//Per Lange Laursen - s144486
package apptest;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestStatistics {
	/**
	 * Tests the scenario where statistics for project is requested
	 * <ol>
	 * <li>Projectleader is logged in and request statistics
	 * <li>Projectleader is not logged in and request statistics
	 * <li>Employee is logged in and request statistics
	 * </ol>
	 * @throws OperationNotAllowedException 
	 */
	
	Employee projectLeader;
	Employee test1, test2;
	Company company;
	Project p1, p2;
	Activity a1, a2;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		// Create company and executive
		Address address = new Address("City", "Street", 1);
		company = new Company("Company", address);
		Executive executive = new Executive("Name", "Department1", company, "password");
		
		// Log in as executive
		assertFalse(company.executiveIsLoggedIn());
		company.executiveLogin("password");
		assertTrue(company.executiveIsLoggedIn());
		
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		p1 = company.createProject("Project01", start, end);
		p2 = company.createProject("Project02");
		
		projectLeader = company.createEmployee("LISN", "password", "RandD");
		
		executive.assignProjectLeader("LISN", p1.getID());
		test1 = company.createEmployee("MADR", "password", "RandD");
		test2 = company.createEmployee("FAML", "password", "RandD");
		
		company.employeeLogin(projectLeader.getID(), "password");
		
		projectLeader.assignEmployeeProject(test1.getID(), "Project01");
		projectLeader.assignEmployeeProject(test2.getID(), "Project01");
		
		projectLeader.createActivity(company.getProject("Project01"), "AO1", start, end);
		projectLeader.createActivity(company.getProject("Project01"), "AO2", start, end);
		
		a1 = company.getProject("Project01").getActivity(p1.getID()+"-AO1");
		a2 = company.getProject("Project01").getActivity(p1.getID()+"-AO2");
		projectLeader.assignEmployeeActivity(test1.getID(), a1.getName());
		projectLeader.assignEmployeeActivity(test2.getID(), a2.getName());
	}
	
	@Test
	public void testgetStatisticsPL() throws OperationNotAllowedException {
		List<String> statistics = projectLeader.getStatisticsProject(company.getProject("Project01"));
		assertEquals("Project Name: " + company.getProject("Project01").getName(), statistics.get(0));
		assertEquals("Project Leader ID: " + company.getProject("Project01").getProjectLeader().getID() +
				" Department " + company.getProject("Project01").getProjectLeader().getDepartment(), 
				statistics.get(1));
		assertEquals("No. of employees assigned: " + company.getProject("Project01").getEmployees().size(), statistics.get(2));
		assertEquals("ID: " + test1.getID() + " Department: " + test1.getDepartment(), statistics.get(3));
		assertEquals("ID: " + test2.getID() + " Department: " + test2.getDepartment(), statistics.get(4));
		assertEquals("No. of activities: " + company.getProject("Project01").getActivities().size(), statistics.get(5));
		assertEquals("Activity name: " + a1.getName() +
				" No. of employees: " + a1.getEmployees().size(),
				statistics.get(6));
		assertEquals("ID: " + test1.getID() + " Department: " + test1.getDepartment(), statistics.get(7));
		assertEquals("Activity name: " + a2.getName() +
				" No. of employees: " + a2.getEmployees().size(),
				statistics.get(8));
		assertEquals("ID: " + test2.getID() + " Department: " + test2.getDepartment(), statistics.get(9));
	}
	
	@Test
	public void testgetStatisticsNotLoggedIn() throws OperationNotAllowedException {
		company.employeeLogout();
		
		try {
			projectLeader.getStatisticsProject(company.getProject("Project01"));
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader",e.getMessage());
			assertEquals("Project leader operation",e.getOperation());
		}
	}
	
	@Test
	public void testgetStatisticsNotPL() throws OperationNotAllowedException {
		Employee test3 = company.createEmployee("HAMB", "password", "RandD");
		company.employeeLogin(test3.getID(), "password");
		try {
			test3.getStatisticsProject(company.getProject("Project01"));
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader",e.getMessage());
			assertEquals("Project leader operation",e.getOperation());
		}
	}
}
