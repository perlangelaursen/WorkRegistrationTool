//Per Lange Laursen - s144486

package apptest;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import softwarehuset.Address;
import softwarehuset.Company;
import softwarehuset.Employee;
import softwarehuset.Executive;
import softwarehuset.OperationNotAllowedException;

public class TestChangePassword {
	Employee test1;
	Executive executive;
	Company company;
	
	@Before
	public void setUp() throws OperationNotAllowedException {
		// Create company and executive
		Address address = new Address("City", "Street", 1);
		company = new Company("Company", address);
		executive = new Executive("Name", "Department1", company,
				"password");
		
		test1 = company.createEmployee("HFBJ", "password", "Department1");
	}
	
	@Test
	public void testExecutiveChangePassword() throws OperationNotAllowedException {
		// Log in as executive
		assertFalse(company.executiveIsLoggedIn());
		company.executiveLogin("password");
		assertTrue(company.executiveIsLoggedIn());
		
		executive.changePassword("testing");
		assertEquals(executive.getPassword(), "testing");
	}
	
	@Test
	public void testExecutiveNotLoggedInChangePassword() {
		assertFalse(company.executiveIsLoggedIn());
		
		try {
			executive.changePassword("testing");
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Executive Not Logged In",
					e.getMessage());
			assertEquals("Executive Change Password", e.getOperation());
		}
	}

	@Test
	public void testEmployeeChangePassword() throws OperationNotAllowedException {
		// Log in as employee
		company.employeeLogin(test1.getID(), "password");
		
		test1.changePassword("testing");
		assertEquals(test1.getPassword(), "testing");
	}
	
	@Test
	public void testEmployeeNotLoggedInChangePassword() {		
		try {
			test1.changePassword("testing");
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Employee Not Logged In",
					e.getMessage());
			assertEquals("Employee Change Password", e.getOperation());
		}
	}
}
