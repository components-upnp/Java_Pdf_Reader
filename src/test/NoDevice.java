package test;

/**
 * Exception for an unconnected device.
 * @author tbille
 *
 */
public class NoDevice extends Exception {
	private static final long serialVersionUID = 1L;

	public NoDevice(String _deviceFriendlyName){
		super("The Device " + _deviceFriendlyName +  " is not connected.");
	}
}
