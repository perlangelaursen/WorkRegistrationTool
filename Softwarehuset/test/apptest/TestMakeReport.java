//Mathias Enggrob Boon - s144484
package apptest;

import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestMakeReport {
	Employee projectLeader;
	Employee employee;
	Company company;
	Project p1, p2;
	
	@Before
	public void setup() throws OperationNotAllowedException {
		// Create company and executive
		Address address = new Address("City", "Street", 1);
		company = new Company("Company", address);
		Executive executive = new Executive("Name", "Department1", company, "password");
		company.setExecutive(executive);
		// Log in as executive
		company.executiveLogin("password");	
		
		GregorianCalendar start = new GregorianCalendar();
		GregorianCalendar end = new GregorianCalendar();
		start.set(2016, Calendar.JANUARY, 23);
		end.set(2016, Calendar.FEBRUARY, 23);
		//Create projects
		p1 = company.createProject("Project01", start, end);
		p2 = company.createProject("Project02", start, end);
		//Create employee and assign as project leader
		projectLeader = company.createEmployee("Test", "password", "RandD");
		executive.assignProjectLeader("Test",p1.getID());
		employee = company.createEmployee("Tess", "password", "RandD");
		company.employeeLogin(projectLeader.getID(), "password");
		p1.createActivity("Activity01", start, end, p1);
		projectLeader.assignEmployeeProject(projectLeader.getID(), p1.getName());
		projectLeader.assignEmployeeActivity(projectLeader.getID(), p1.getID()+"-Activity01");
		projectLeader.registerSpentTime(p1.getID()+"-Activity01", 100);
		projectLeader.writeReport(p1, "Changes to Project", 2016, 1, 23);
	}
	
	@Test
	public void testWriteReport() throws OperationNotAllowedException {
		
		assertEquals("Changes to Project", p1.getReport("Changes to Project").getName());
		assertNull(p1.getReport("Non-existing project"));
	}

	@Test
	public void testWriteReportNotLoggedIn() throws OperationNotAllowedException{
		company.employeeLogout();
		try{
			projectLeader.writeReport(company.getProject("Project02"), "Changes to Project", 2016, 1, 23);
			fail("OperationNotAllowedException expected");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader",e.getMessage());
			assertEquals("Project leader operation",e.getOperation());
		}
	}
	
	@Test
	public void testWriteReportNotProjectLeader() throws OperationNotAllowedException{
		Employee em = company.createEmployee("AAKS", "password", "department");
		company.employeeLogin("AAKS", "password");
		
		try{
			em.writeReport(company.getProject("Project02"), "Changes to Project", 2016,1 ,23);
			fail("OperationNotAllowedException expected");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader",e.getMessage());
			assertEquals("Project leader operation",e.getOperation());
		}
	}

	@Test
	public void testWriteReportInvalidDate(){
		try{
			projectLeader.writeReport(p1, "Changes to Project", 2016, 1, 35);
			fail("OperationNotAllowedException expected");
		} catch(OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
		}
	}
	
	@Test
	public void testReadReportNotLoggedIn(){
		company.employeeLogout();
		try{
			p1.getReport("Changes to Project");
			fail("OperationNotAllowedException expected");
		} catch (OperationNotAllowedException e) {
			assertEquals("Get report is not allowed if not logged in", e.getMessage());
			assertEquals("Get report", e.getOperation());
		}
	}
	
	@Test
	public void testEditReport() throws OperationNotAllowedException{
		projectLeader.editReport(p1.getReport("Changes to Project"), "New content");
		assertEquals("New content", p1.getReport("Changes to Project").getContent());
	}
	
	@Test
	public void testEditReportNotProjectLeader(){
		try{
			employee.editReport(p1.getReport("Changes to Project"), "New content");
			fail("OperationNotAllowedException expected");
		} catch (OperationNotAllowedException e) {
			assertEquals("Operation is not allowed if not project leader", e.getMessage());
			assertEquals("Project leader operation", e.getOperation());
		}
	}
	
	@Test
	public void testEditNullReport() throws OperationNotAllowedException{
		try{
			projectLeader.editReport(p1.getReport("Non-existing report"), "New content");
			fail("OperationNotAllowedException expected");
		} catch (OperationNotAllowedException e) {
			assertEquals("Report does not exist", e.getMessage());
			assertEquals("Edit report", e.getOperation());
		}
	}
}
