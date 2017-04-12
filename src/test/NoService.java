package test;

/**
 * Exception if a service isn't existing
 * @author tbille
 *
 */
public class NoService extends Exception {
	private static final long serialVersionUID = 1L;

	public NoService(String _serviceId, String _deviceFriendlyName){
		super("The service : "+_serviceId+" of the device " + _deviceFriendlyName +  " in not specified.");
	}
}
