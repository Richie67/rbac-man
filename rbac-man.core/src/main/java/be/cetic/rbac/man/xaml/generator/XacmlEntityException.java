package be.cetic.rbac.man.xaml.generator;

public class XacmlEntityException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XacmlEntityException(){
		super();
	}
	
	public XacmlEntityException(String message){
		super(message);
	}
	
	public XacmlEntityException(Throwable t){
		super(t);
	}
}
