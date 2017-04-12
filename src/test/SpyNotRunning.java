package test;

/**
 * Exception when the spy is not running.
 * @author tbille
 *
 */
public class SpyNotRunning extends Exception {
	private static final long serialVersionUID = 1L;

	public SpyNotRunning(){
		super("Spy already running");
	}
}
