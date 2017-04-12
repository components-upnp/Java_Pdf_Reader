package com.examples.upnp;
import java.io.IOException;

import javax.xml.bind.ValidationException;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

import com.examples.swing.controller.MainController;
import com.examples.swing.view.MainView;

public class VisionneuseServer implements Runnable{

    private MainController control;

	public void run() {
        try {

            final UpnpService upnpService = new UpnpServiceImpl();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    upnpService.shutdown();
                }
            });

            // Add the bound local device to the registry
            upnpService.getRegistry().addDevice(
                    createDevice()
            );

        } catch (Exception ex) {
            System.err.println("Exception occured: " + ex);
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
	
	public LocalDevice createDevice()
            throws  LocalServiceBindingException, IOException, org.fourthline.cling.model.ValidationException {

        /**
         * Description du Device
         */
        DeviceIdentity identity =
                new DeviceIdentity(
                        UDN.uniqueSystemIdentifier("PDF Viewer")
                );

        DeviceType type =
                new UDADeviceType("PdfViewer", 1);

        DeviceDetails details =
                new DeviceDetails(
                        "Lecteur PDF",					// Friendly Name
                        new ManufacturerDetails(
                                "UPS-IRIT & CreaTech",								// Manufacturer
                                ""),								// Manufacturer URL
                        new ModelDetails(
                                "Lecteur Pdf",						// Model Name
                                "un lecteur de fichier PDF",	// Model Description
                                "v1" 								// Model Number
                        )
                );
        LocalService<VisionneuseService> selectionService =
                new AnnotationLocalServiceBinder().read(VisionneuseService.class);
        selectionService.setManager(
                new DefaultServiceManager(selectionService, VisionneuseService.class)
        );
        control = new MainController(new MainView(), selectionService);
        // retour en cas de 1 service
        return new LocalDevice(identity, type, details, selectionService);

    }

    public MainController getControl() {
	    return this.control;
    }

    

}
