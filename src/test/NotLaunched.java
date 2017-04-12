package test;

/**
 * Exception if the spy is not running
 * @author tbille
 *
 */
public class NotLaunched extends Exception{
	private static final long serialVersionUID = 1L;

	public NotLaunched(){
		super("The spy isn't running.");
	}
}
