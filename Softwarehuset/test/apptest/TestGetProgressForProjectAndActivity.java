//Mathias Enggrob Boon - s144484
package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestGetProgressForProjectAndActivity {
	Employee projectLeader;
	Employee test1;
	Company company;
	Project p1, p2;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		Address address = new Address("City", "Street", 1);
		company = new Company("Company", address);
		Executive executive = new Executive("Name", "Department1", company, "password");
		company.setExecutive(executive);
		company.executiveLogin("password");
		
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		
		p1 = company.createProject("Project01", start, end);
		p2 = company.createProject("Project02", start, end);
		
		projectLeader = company.createEmployee("EFGH", "password", "RandD");
		executive.assignProjectLeader("EFGH",p1.getID());
		
		test1 = company.createEmployee("ABCD", "password", "RandD");
		company.employeeLogin(projectLeader.getID(), "password");
		
		company.getProject("Project01").createActivity("Activity01", start, end, company.getProject("Project01"));
		projectLeader.assignEmployeeProject(projectLeader.getID(), p1.getName());

		projectLeader.assignEmployeeActivity(projectLeader.getID(), p1.getID()+"-Activity01");
		projectLeader.registerSpentTime(p1.getID()+"-Activity01", 100);
		}
	
	
	@Test
	public void testProgressActivity() throws OperationNotAllowedException {
		
		assertEquals(100, projectLeader.viewProgress("Project01", p1.getID()+"-Activity01"));
	}
	@Test
	public void testProgressProject() throws OperationNotAllowedException {
		assertEquals(100, projectLeader.viewProgress("Project01"));
	}
	
	@Test
	public void testProgressNotLoggedIn() throws OperationNotAllowedException {
		company.employeeLogout();
		
		try{
			projectLeader.viewProgress("Project01", "Activity01");
			fail("OperationNotAllowedException expected");
		} catch (OperationNotAllowedException e){
			assertEquals("Operation is not allowed if not project leader",e.getMessage());
			assertEquals("Project leader operation",e.getOperation());
		}
		try{
			projectLeader.viewProgress("Project01");
			fail("OperationNotAllowedException expected");
		} catch (OperationNotAllowedException e){
			assertEquals("Operation is not allowed if not project leader",e.getMessage());
			assertEquals("Project leader operation",e.getOperation());
		}
	}
	
	@Test
	public void testProgressNotProjectLeader() throws OperationNotAllowedException {
		Employee em = company.createEmployee("JLBD", "password", "Department");
		company.employeeLogin("JLBD", "password");
		
		try{
			em.viewProgress("Project01", "Activity01");
			fail("OperationNotAllowedException expected");
		} catch (OperationNotAllowedException e){
			assertEquals("Operation is not allowed if not project leader",e.getMessage());
			assertEquals("Project leader operation",e.getOperation());
		}
		
		try{
			em.viewProgress("Project01");
			fail("OperationNotAllowedException expected");
		} catch (OperationNotAllowedException e){
			assertEquals("Operation is not allowed if not project leader",e.getMessage());
			assertEquals("Project leader operation",e.getOperation());
		}
	}
}

