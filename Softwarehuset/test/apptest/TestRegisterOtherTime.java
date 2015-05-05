//Van Anh Thi Trinh - s144449
package apptest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.Before;
import org.junit.Test;
import softwarehuset.*;



public class TestRegisterOtherTime {
	private Company company;
	private Employee employee;

	@Before
	public void setUp() throws OperationNotAllowedException {
		// Create company, executive, project leader for a project and employee assigned to the project
		Address address = new Address("City", "Street", 1);
		company = new Company("Softwarehuset", address);
		employee = company.createEmployee("LINK", "empassword",	"Department1");
	}

	/**
	 * Tests the scenario where an employee successfully registers sick days,
	 * vacation and course attendance
	 * <ol>
	 * <li>The employee is logged in
	 * <li>The employee registers his spent time on a chosen activity that he is
	 * assigned to
	 * </ol>
	 * 
	 */
	
	@Test
	public void testRegisterOtherTime() throws OperationNotAllowedException {
		company.employeeLogin("LINK", "empassword");

		// Register vacation, sick time and course attendance (all days are included)
		employee.registerVacationTime(2015, 12, 23, 2016, 1, 3);
		assertEquals(12 * 24, employee.getTimeForPersonalActivity("Vacation"));

		employee.registerSickTime(2015, 3, 25, 2015, 3, 26);
		assertEquals(2 * 24, employee.getTimeForPersonalActivity("Sick"));

		employee.registerCourseTime(2016, 5, 1, 2016, 5, 1);
		assertEquals(24, employee.getTimeForPersonalActivity("Course"));
	}

	/**
	 * Tests the scenario where an employee registers invalid times
	 * <ol>
	 * <li>The employee is logged in
	 * <li>The employee registers invalid times
	 * <li>Exceptions are thrown
	 * </ol>
	 * 
	 */
	
	@Test
	public void testRegisterInvalidTimes() throws OperationNotAllowedException {
		company.employeeLogin("LINK", "empassword");

		// Vacation on days that have passed
		try {
			employee.registerVacationTime(2014, 12, 23, 2015, 1, 3);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Cannot register vacation in the past", e.getMessage());
			assertEquals("Register other time", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Vacation"));

		// Course attendance on days that have passed
		try {
			employee.registerCourseTime(2014, 5, 1, 2015, 5, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Cannot register course attendance in the past", e.getMessage());
			assertEquals("Register other time", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));

		// Sick in the future
		try {
			employee.registerSickTime(2016, 3, 25, 2016, 3, 26);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Cannot register sick days in the future", e.getMessage());
			assertEquals("Register other time", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Sick"));

		// Start date after end date
		try {
			employee.registerCourseTime(2016, 5, 2, 2016, 5, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("End date cannot be before start date", e.getMessage());
			assertEquals("Register other time", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		//Non-existing date input

	}

	/**
	 * Tests all possible ways to type in non-existing dates
	 * <ol>
	 * <li>The employee is logged in
	 * <li>The employee registers dates that do not exist
	 * <li>Exceptions are thrown
	 * </ol>
	 * 
	 */
	
	@Test
	public void testRegisterNonExistingDates() throws OperationNotAllowedException {
		company.employeeLogin("LINK", "empassword");
		employee.registerVacationTime(2016, 5, 1, 2016, 5, 10);
		
		try {
			employee.registerVacationTime(1800, 5, 2, 2016, 5, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		try {
			employee.registerVacationTime(2030, 5, 2, 2016, 5, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		try {
			employee.registerVacationTime(2016, -5, 2, 2016, 5, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		try {
			employee.registerVacationTime(2016, 50, 2, 2016, 5, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		try {
			employee.registerVacationTime(2016, 5, 0, 2016, 5, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		try {
			employee.registerVacationTime(2016, 5, 32, 2016, 5, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		try {
			employee.registerVacationTime(2016, 5, 2, 1800, 5, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		try {
			employee.registerVacationTime(2016, 5, 2, 2030, 5, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		try {
			employee.registerVacationTime(2016, 5, 2, 2016, 0, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		try {
			employee.registerVacationTime(2016, 5, 2, 2016, 13, 1);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		try {
			employee.registerVacationTime(2016, 5, 2, 2016, 5, 0);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		
		try {
			employee.registerVacationTime(2016, 5, 2, 2016, 5, 40);
			fail("OperationNotAllowedException exception should have been thrown");
		} catch (OperationNotAllowedException e) {
			assertEquals("Invalid time input", e.getMessage());
			assertEquals("Choose date", e.getOperation());
		}
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
	}
	
	/**
	 * Tests the scenario where an employee registers vacation or course
	 * attendance on occupied dates
	 * <ol>
	 * <li>The employee is logged in
	 * <li>The employee registers invalid times
	 * <li>Exceptions are thrown
	 * </ol>
	 * 
	 */
	
	@Test
	public void testRegisteronOccupiedDates() throws OperationNotAllowedException {
		company.employeeLogin("LINK", "empassword");
		employee.registerVacationTime(2016, 5, 1, 2016, 5, 10);
		
		//Vacation now: 1/5/2016 - 10/5/2016
		assertEquals(10 * 24, employee.getTimeForPersonalActivity("Vacation"));
		
		//Set current date to May 6th 2016
		DateServer dateServer = mock(DateServer.class);
		company.setDateServer(dateServer);
		Calendar newDate = new GregorianCalendar();
		newDate.set(2016, Calendar.MAY, 6);
		when(dateServer.getCurrentDate()).thenReturn(newDate);
		
		//Register sick days on vacation days starting before the vacation and ending in the vacation
		employee.registerSickTime(2016, 4, 28, 2016, 5, 5);
		assertEquals(8 * 24, employee.getTimeForPersonalActivity("Sick"));
		//Vacation now: 6/5/2016 - 10/5/2016
		assertEquals(5 * 24, employee.getTimeForPersonalActivity("Vacation"));
		
		//Register vacation days on vacation days starting in the vacation period and ending after (extending the vacation)
		employee.registerVacationTime(2016, 5, 8, 2016, 5, 20);
		//Vacation now: 6/5/2016 - 20/5/2016
		assertEquals(15 * 24, employee.getTimeForPersonalActivity("Vacation"));
		
		//Register course attendance starting and ending in a vacation (dividing the vacation into two parts)
		employee.registerCourseTime(2016, 5, 10, 2016, 5, 15);
		assertEquals(6 * 24, employee.getTimeForPersonalActivity("Course"));
		//Vacation now: 6/5/2016 - 9/5/2016 and 16/5/2016 - 20/5/2016  
		assertEquals(4 * 24 + 5 * 24, employee.getTimeForPersonalActivity("Vacation"));
	}
	
	@Test
	public void testRegisteronOccupiedDates2() throws OperationNotAllowedException {
		company.employeeLogin("LINK", "empassword");
		employee.registerVacationTime(2016, 5, 1, 2016, 5, 10);
		
		//Vacation now: 1/5/2016 - 10/5/2016
		assertEquals(10 * 24, employee.getTimeForPersonalActivity("Vacation"));
		
		//Register course attendance starting before the vacation and ending after the vacation (overwriting the vacation completely)
		employee.registerCourseTime(2016, 4, 28, 2016, 5, 15);
		assertEquals(18 * 24, employee.getTimeForPersonalActivity("Course"));  
		assertEquals(0, employee.getTimeForPersonalActivity("Vacation"));
	
		//Register vacation exactly in the course period
		employee.registerVacationTime(2016, 4, 28, 2016, 5, 15);
		assertEquals(0, employee.getTimeForPersonalActivity("Course"));
		assertEquals(18 * 24, employee.getTimeForPersonalActivity("Vacation"));
	}
	
	@Test
	public void testRegisteronOccupiedDates3() throws OperationNotAllowedException {
		company.employeeLogin("LINK", "empassword");
		employee.registerVacationTime(2016, 5, 1, 2016, 5, 10);
		
		//Vacation now: 1/5/2016 - 10/5/2016
		assertEquals(10 * 24, employee.getTimeForPersonalActivity("Vacation"));
		
		//Register course attendance starting on the same date as the vacation
		employee.registerCourseTime(2016, 5, 1, 2016, 5, 1);  
		assertEquals(24, employee.getTimeForPersonalActivity("Course"));
		assertEquals(9 * 24, employee.getTimeForPersonalActivity("Vacation"));
	}
	@Test
	public void testRegisteronOccupiedDates4() throws OperationNotAllowedException {
		company.employeeLogin("LINK", "empassword");
		employee.registerVacationTime(2016, 5, 1, 2016, 5, 10);
		
		//Vacation now: 1/5/2016 - 10/5/2016
		assertEquals(10 * 24, employee.getTimeForPersonalActivity("Vacation"));
		
		//Register course attendance ending on the same date as the vacation
		employee.registerCourseTime(2016, 5, 9, 2016, 5, 10);  
		assertEquals(2 * 24, employee.getTimeForPersonalActivity("Course"));
		assertEquals(8 * 24, employee.getTimeForPersonalActivity("Vacation"));
	}
	/**
	 * Tests the scenario where an employee's calendar is updated with no overlapping dates 
	 * (cannot usually happen since the calendar is only updated IF there are overlapping dates)
	 * 
	 */
	
	@Test
	public void testRegisterOtherTime2() throws OperationNotAllowedException {
		Activity vacation = employee.createPersonalActivity(2015, 12, 23, 2016, 1, 3, "Vacation");
		Activity course = employee.createPersonalActivity(2016, 5, 1, 2016, 5, 1, "Course");
		employee.updateOldPlans(course, vacation);
		employee.updateOldPlans(vacation, course);
	}
}
