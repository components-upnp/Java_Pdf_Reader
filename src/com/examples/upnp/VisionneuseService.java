package com.examples.upnp;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.examples.xml.LecteurXml;
import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;

import com.examples.swing.controller.Etat;
import org.fourthline.cling.model.types.UDN;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

@UpnpService(
        serviceId = @UpnpServiceId("VisionneuseService"),
        serviceType = @UpnpServiceType(value = "VisionneuseService", version = 1)
)
public class VisionneuseService {
	public  VisionneuseService() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	private  PropertyChangeSupport propertyChangeSupport;
	
	public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
	}
	
	@UpnpStateVariable(defaultValue = "", sendEvents = false)
	private String commande = "";
	
	@UpnpStateVariable(defaultValue = "AUCUNE")
	private Etat status = Etat.AUCUNE;

	@UpnpStateVariable(defaultValue = "0", datatype = "int", allowedValueMinimum = 0)
    private int nbPages = 0;

	@UpnpStateVariable(defaultValue = "0", datatype = "int", allowedValueMinimum = 0)
    private int numPage = 0;

	private String udn; //On sauvegarde ici l'UDN du composant qui intéragit avec le lecteur

    @UpnpStateVariable(sendEvents = false)
    private boolean locked = false;
	
	@UpnpAction(name = "SetCommande")
    public void setCommande(@UpnpInputArgument(name = "Commande") String c) throws IOException, SAXException, ParserConfigurationException {

        LecteurXml lec = new LecteurXml(c);


        Etat statusOldValue = status;
        Etat newActionValue;

        commande = lec.getCommande();
        switch(commande) {
            case("DROITE"):
                newActionValue = Etat.DROITE;
                break;
            case("GAUCHE"):
                newActionValue = Etat.GAUCHE;
                break;
            case("HAUT"):
                newActionValue = Etat.HAUT;
                break;
            case("BAS"):
                newActionValue = Etat.BAS;
                break;
            case("CENTRE"):
                newActionValue = Etat.CENTRE;
                break;
            default:
                newActionValue = Etat.AUCUNE;
        }

        if (locked && udn.equals(lec.getUdn())) {
            status = newActionValue;
            // These have no effect on the UPnP monitoring but it's JavaBean compliant
            getPropertyChangeSupport().firePropertyChange("status", statusOldValue, status);
            // This will send a UPnP event, it's the name of a state variable that sends events
            getPropertyChangeSupport().firePropertyChange("Status", statusOldValue, status);
            status = Etat.AUCUNE;
        } else if (!locked) {
            locked = true;
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    locked = false;
                    System.out.println("Unlocked");
                }
            }, 5000);
            udn = lec.getUdn();
            // These have no effect on the UPnP monitoring but it's JavaBean compliant
            getPropertyChangeSupport().firePropertyChange("status", statusOldValue, status);
            // This will send a UPnP event, it's the name of a state variable that sends events
            getPropertyChangeSupport().firePropertyChange("Status", statusOldValue, status);
            status = Etat.AUCUNE;
        }
    }


    @UpnpAction(out = @UpnpOutputArgument(name = "ResultNbPages"))
    public int getNbPages() {
	    return this.nbPages;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultNumPage"))
    public int getNumPage() {
	    return this.numPage;
    }


    public void setNbPages( @UpnpInputArgument(name = "NewNbPages") int newNbPages) {

	    int oldNbPages = this.nbPages;
	    this.nbPages = newNbPages;

        getPropertyChangeSupport().firePropertyChange("NbPages", oldNbPages, this.nbPages);

    }


    public void setNumPage(@UpnpInputArgument(name = "NewNumPage") int newNumPage) {
	    int oldNumPage = this.numPage;
	    this.numPage = newNumPage;

        getPropertyChangeSupport().firePropertyChange("NumPage", oldNumPage, this.numPage);
    }
}
