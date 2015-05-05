package softwarehuset;

public class Executive {
	private Company company;
	private String name, department, password;
	private boolean executiveLoggedIn = false;

	public Executive(String name, String department, Company company, String password) {
		this.name = name;
		this.department = department;
		this.company = company;
		company.setExecutive(this);
		this.password = password;
	}

	public void setLoginStatus(boolean b){
		executiveLoggedIn = b;
	}
	public String getPassword(){
		return password;
	}
	public void assignProjectLeader(Employee employee, Project specificProject) throws OperationNotAllowedException {
		if(employee == null){
			throw new OperationNotAllowedException("Employee not found", "Employee not found");
		}
		
		if(!executiveLoggedIn){
			throw new OperationNotAllowedException("Assign project leader is not allowed if not executive.", "Assign project leader");
		}
		specificProject.assignProjectLeader(employee);
	}
}
