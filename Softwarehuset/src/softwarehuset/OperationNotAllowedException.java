package softwarehuset;


@SuppressWarnings("serial")
public class OperationNotAllowedException extends Exception {

	private String errorOpr;
	public OperationNotAllowedException(String errorMsg, String errorOpr){
		super(errorMsg);
		this.errorOpr = errorOpr;
	}
	public Object getOperation() {
		return errorOpr;
	}


}
