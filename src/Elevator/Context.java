package Elevator;

public class Context {

	private StateService ss;
	
	public Context() {
		ss = null;
	}
	
	public void setState(StateService state) {
		this.ss = state;
	}
	
	public StateService getState(){
		return ss;
	}
	
}
