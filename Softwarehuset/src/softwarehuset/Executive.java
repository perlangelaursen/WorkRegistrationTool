package softwarehuset;

public class Executive {
	private Company company;
	private String name, department, password;

	public Executive(String name, String department, Company company, String password) {
		this.name = name;
		this.department = department;
		this.company = company;
		company.setExecutive(this);
		this.password = password;
	}

	public String getPassword(){
		return password;
	}
	public void assignProjectLeader(String employee, int project) throws OperationNotAllowedException {
		Employee e = company.getEmployee(employee);
		Project p = company.getProject(project);
		
		if(!company.executiveIsLoggedIn()){
			throw new OperationNotAllowedException("Assign project leader is not allowed if not executive", "Assign project leader");
		}
		e.assignEmployeeProject(employee, company.getProject(project).getName());
		p.assignProjectLeader(e);
	}
}
