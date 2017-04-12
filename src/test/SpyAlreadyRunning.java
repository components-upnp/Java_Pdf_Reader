package test;

/**
 * Exception when the spy is already running.
 * @author tbille
 *
 */
public class SpyAlreadyRunning extends Exception {
	private static final long serialVersionUID = 1L;

	public SpyAlreadyRunning(){
		super("Spy already running");
	}
}
