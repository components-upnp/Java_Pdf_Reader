package test;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.InvalidValueException;
import org.fourthline.cling.model.types.ServiceId;
import org.fourthline.cling.model.types.UDAServiceId;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * Class that connects to a Upnp device from his friendly name.
 * @author tbille
 *
 */
public class Spy {
	
	private String friendlyName;
	private RemoteDevice device;
	private UpnpService upnpService;
	
	private ArrayList<Object> resultats;
	private volatile Thread clientThread;
	

	/**
	 * Class that search and connects to a device thanks to his friendly name. 
	 * @param _friendlyName Friendly Name of the container
	 */
	public Spy(String _friendlyName){
		friendlyName=_friendlyName;

		try {
			runSpy();
		} catch (SpyAlreadyRunning e1) {
			e1.printStackTrace();
		}

		while(getDevice()==null){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Method that launches the spy. If the spy is already running : it stops it and launches a new spy.
	 */
	public void runSpy() throws SpyAlreadyRunning{
		if(clientThread==null){
	        clientThread = new Thread(new Runnable() {

				@Override
				public void run() {
			        try {
			            upnpService = new UpnpServiceImpl();

			            // Add a listener for device registration events
			            upnpService.getRegistry().addListener(
			                    createRegistryListener(upnpService)
			            );

			            // Broadcast a search message for all devices
			            upnpService.getControlPoint().search(
			                    new STAllHeader()
			            );

			        } catch (Exception ex) {
			            System.err.println("Exception occured: " + ex);
			            System.exit(1);
			        }

				}
			});
	        clientThread.setDaemon(false);
	        clientThread.start();
		}
		else
			throw new SpyAlreadyRunning();
	}
	
	/**
	 * Set the friendlyName, this method stops the spy, changes the friendlyname and run again the spy.
	 * @param _friendlyName Friendly name of the device
	 */
	public void setFriendlyName(String _friendlyName){
		if(clientThread!=null){
			try {
				stopSpy();
			} catch (SpyNotRunning e) {
				clientThread=null;
			}
		}

		friendlyName=_friendlyName;
		
		try {
			runSpy();
		} catch (SpyAlreadyRunning e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the device.
	 * @return device The connected device
	 */
	public RemoteDevice getDevice() {
		return device;
	}

	/**
	 * Get the UpnpService.
	 * @return upnpService 
	 */
	public UpnpService getUpnpService() {
		return upnpService;
	}
	
	/**
	 * Function that stops the connection to the device.
	 * 
	 */
	public void stopSpy() throws SpyNotRunning{
		if(upnpService!=null && clientThread!=null){
			System.out.println("Stopping Device");
			try{
				upnpService.shutdown();
				upnpService=null;
				if(clientThread.isAlive())
					clientThread.interrupt();
				this.clientThread = null;
			}
			catch(Exception e){
				System.err.println(e.getMessage());
			}
		}
		else
			throw new SpyNotRunning();
	}
	

	/**
	 * Listens the network, when the device is found it is added.
	 * @param upnpService
	 * @return
	 */
	private RegistryListener createRegistryListener(final UpnpService upnpService) {
	    return new DefaultRegistryListener() {
	    		
	        @Override
	        public void remoteDeviceAdded(Registry registry, RemoteDevice _device) {
	        	if(_device.getDetails().getFriendlyName().equals(friendlyName)){
	        		System.out.println("Device "+friendlyName+" found.");
	            	// Sauvegarde du device
	            	device=_device;
	        	}
	
	        }
	
	        @Override
	        public void remoteDeviceRemoved(Registry registry, RemoteDevice _device) {
	            if (_device.getDetails().getFriendlyName().equals(friendlyName)) {
	                System.out.println("Device " + friendlyName + " removed");
	                device = null;
	            }
	        }
	
	    };
	}

	/**
	 * Method that launches a action in a service with his arguments. It returns a arraylist of objects, the result of the upnp method 
	 * @param serviceId
	 * @param actionName
	 * @param arg
	 * @return null if the method has no return/output, an ArrayList<Object> with the return/output result of the method
	 * @throws InvalidValueException
	 * @throws NoDevice
	 * @throws NoService
	 */
	public ArrayList<Object> launchAction(String serviceId, String actionName, HashMap<String, Object> arg) 
			throws InvalidValueException, NoDevice, NoService, NotLaunched{
		System.out.println("------------------START try Action " + actionName);
		
		// Control if the spy is running
		if(upnpService==null)
			throw new NotLaunched();
		// Control if the device is connected
		if(device==null)
			throw new NoDevice(this.friendlyName);
		// Control if the service exists in the device
		Service<?, ?> myService;
		ServiceId serviceIdentificator = new UDAServiceId(serviceId);
		if ((myService = device.findService(serviceIdentificator)) == null)
			throw new NoService(serviceId, friendlyName);
		
		try{
			// Launch action
			Future<?> f = executeAction(upnpService, myService, actionName, arg);
			try {
				// Wait until the action is done
				while(!f.isDone());
			} catch (InvalidValueException e) {
				throw e;
			}
			
			return resultats;
		}
		catch(InvalidValueException ex){
			throw ex;
		}
		
	}

	/**
	 * Execute the action. 
	 * @param upnpService
	 * @param service
	 * @param action
	 * @param arg
	 * @return A future object to check when the computation of the event called is done.
	 * @throws InvalidValueException
	 */
	private Future<?> executeAction(UpnpService upnpService, Service<?, ?> service, String action, HashMap<String, Object> arg)
			throws InvalidValueException{
		try{
	        ActionInvocation<?> myInvocation =
	                new MyActionInvocation(service, action, arg);
	        // Executes asynchronous in the background
	     
		        Future<?> f = upnpService.getControlPoint().execute(
		                new ActionCallback(myInvocation) {
		                    @Override
		                    public void success(ActionInvocation invocation) {
		                        System.out.println("Successfully called action!");
		                        if(invocation.getOutput().length>0){
			                        resultats = new ArrayList<Object>();
			                        for (ActionArgumentValue<?> object : invocation.getOutput()) {
										resultats.add(object.getValue());
									}
		                        }
		                        else
		                        	resultats=null;
		                        
		                    }
		
		                    @Override
		                    public void failure(ActionInvocation invocation,
		                                        UpnpResponse operation,
		                                        String defaultMsg) {
		                        System.err.println(defaultMsg);
		                        resultats=null;
		                    }
		                }
		        );
		        return f;
        }
        catch(InvalidValueException ex){
        	throw ex;
        } 
	}
	
	/**
	 * 
	 * @author tbille
	 *
	 */
	private class MyActionInvocation extends ActionInvocation {
	    @SuppressWarnings("unchecked")
	    public MyActionInvocation(Service<?,?> service, String action, HashMap<String, Object> arg) 
				throws InvalidValueException {
	        super(service.getAction(action));
	        try {
	            // Throws InvalidValueException if the value is of wrong type
	        	if(arg!=null){
	        		// set input for all arguments of the upnp method
		        	for (String mapKey : arg.keySet()) {
		        		System.out.println(mapKey + " "+ arg.get(mapKey));
		        		setInput(mapKey, arg.get(mapKey));
		        	}
	        	}
	        } catch (InvalidValueException ex) {
	            throw ex;
	        }
	    }  
	}
}