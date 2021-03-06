//Anna Oelgaard Nielsen - s144437

package apptest;

import static org.junit.Assert.*;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;

public class TestGetAvailableEmployees {
	private Address address;
	private Company company;
	private Executive executive;
	private GregorianCalendar date1, date2, date3, date4, date5, date6;
	private Employee employee1, employee2, employee3, employee4;
	private Project project;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		address = new Address("City", "Street", 1);
		company = new Company("SoftwareHuset", address);
		executive = new Executive("name","Department1", company, "password");
		employee1 = company.createEmployee("ANDS", "password", "Project Department");
		employee2 = company.createEmployee("HENR", "password", "Project Department");
		employee3 = company.createEmployee("KLIS", "password", "Project Department");
		employee4 = company.createEmployee("HSNF", "password", "Project Department");
		
		date1 = new GregorianCalendar();
		date2 = new GregorianCalendar();
		date3 = new GregorianCalendar();
		date4 = new GregorianCalendar();
		date5 = new GregorianCalendar();
		date6 = new GregorianCalendar();
		
		company.executiveLogin(executive.getPassword());
		project = company.createProject("Project 1");

		//company.employeeLogin(employee1.getID(),employee1.getPassword());
		company.employeeLogout();
	}
	
	@Test
	public void testAvaiableEmployeesOnePerson() throws OperationNotAllowedException {
		//Executive assigns employee1 as project leader
		company.executiveLogin("password");
		executive.assignProjectLeader("ANDS", project.getID());
		assertEquals(project.getProjectLeader(), employee1);
		
		//dates initializes
		date1.set(2016, 3, 1);
		date2.set(2016, 4, 1);
		date3.set(2016, 5, 1);
		date4.set(2016, 6, 1);
		
		//employee 1 and 2 assigns on different activities in Project 1
		company.employeeLogin(employee1.getID(), employee1.getPassword());
		employee1.createActivity(project, "activity1", date1, date2, 3);
		employee1.createActivity(project, "activity2", date3, date4, 3);
		employee1.assignEmployeeActivity(employee2.getID(), project.getID() + "-activity1");
		employee1.assignEmployeeActivity(employee2.getID(), project.getID() + "-activity2");
		
		//time span for check initializes
		date5.set(2000, 1, 1);
		date6.set(2000, 2, 1);
		
		//Check if employee 2 is available in time span
		assertTrue(company.getAvailableEmployees(date5, date6).contains(employee2));
		
	}
	
	@Test
	public void testAvailableEmployeesThreePersons() throws OperationNotAllowedException {
		//Executive assigns employee1 as project leader
		company.executiveLogin("password");
		executive.assignProjectLeader(employee1.getID(), project.getID());
		assertEquals(project.getProjectLeader(), employee1);
		
		//dates initializes
		date1.set(2016, 3, 1);
		date2.set(2016, 4, 1);
		date3.set(2016, 5, 1);
		date4.set(2016, 6, 1);
		
		//project leader creates two activities and assigns employees
		employee1.createActivity(project, "activity1", date1, date2,3);
		employee1.createActivity(project, "activity2", date3, date4,3);
		employee1.assignEmployeeActivity(employee2.getID(), project.getID() + "-activity1");
		employee1.assignEmployeeActivity(employee3.getID(), project.getID() + "-activity2");
		employee1.assignEmployeeActivity(employee4.getID(), project.getID() + "-activity2");
		
		date5.set(2000, 1, 1);
		date6.set(2000, 2, 1);
		
		//Check if all employees is available in a time span before activities
		assertTrue(company.getAvailableEmployees(date5, date6).contains(employee2));
		assertTrue(company.getAvailableEmployees(date5, date6).contains(employee3));
		assertTrue(company.getAvailableEmployees(date5, date6).contains(employee4));
	}
	
	@Test
	public void testAvailableEmployeesOverlap() throws OperationNotAllowedException {
		//Executive assigns employee1 as project leader
		company.executiveLogin("password");
		executive.assignProjectLeader("ANDS", project.getID());
		assertEquals(project.getProjectLeader(), employee1);
		
		//dates initializes
		date1.set(2016, 3, 1);
		date2.set(2016, 4, 1);
		date3.set(2016, 5, 1);
		date4.set(2016, 6, 1);
		
		//Create 20 activities
		for(int i = 1; i<=20; i++){
			employee1.createActivity(project, "activity"+i, date1, date2,3);
		}
		assertEquals(20, project.getActivities().size());
		
		//Check employee2 is available
		assertTrue(company.getAvailableEmployees(date1, date2).contains(employee2));
		
		//Assign employee2 to 20 activities
		for(Activity a: project.getActivities()){
			employee1.assignEmployeeActivity(employee2.getID(), a.getName());
		}
		assertEquals(20, employee2.getActivities().size());
		
		//Check if employee2 is not available in time span
		assertFalse(company.getAvailableEmployees(date1, date2).contains(employee2));
	}

}
