package com.examples.upnp;

import java.beans.PropertyChangeSupport;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;

import com.examples.swing.controller.Etat;

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
	
	@UpnpStateVariable(defaultValue = "AUCUNE", sendEvents = false)
	private Etat target = Etat.AUCUNE;
	
	@UpnpStateVariable(defaultValue = "AUCUNE")
	private Etat status = Etat.AUCUNE;

	@UpnpStateVariable(defaultValue = "0", datatype = "int", allowedValueMinimum = 0)
    private int nbPages = 0;

	@UpnpStateVariable(defaultValue = "0", datatype = "int", allowedValueMinimum = 0)
    private int numPage = 0;
	
	@UpnpAction
    public void setTarget(@UpnpInputArgument(name = "NewTargetValue") String newTargetValue) {
        Etat targetOldValue = target;
        Etat statusOldValue = status;
        Etat newActionValue;
        switch(newTargetValue) {
            case("DROITE"):
                newActionValue = Etat.DROITE;
                break;
            case("GAUCHE"):
                newActionValue = Etat.GAUCHE;
                break;
            default:
                newActionValue = Etat.AUCUNE;
        }
        status = newActionValue;
        target = status;
        // These have no effect on the UPnP monitoring but it's JavaBean compliant
        getPropertyChangeSupport().firePropertyChange("target", targetOldValue, target);
        getPropertyChangeSupport().firePropertyChange("status", statusOldValue, status);
        // This will send a UPnP event, it's the name of a state variable that sends events
        getPropertyChangeSupport().firePropertyChange("Status", statusOldValue, status);
        status = Etat.AUCUNE;
        target = Etat.AUCUNE;
    }

    /**
     * Get Status of the lamp
     * Methode Upnp grace au syst�me d'annotation
     * @return Action
     */
	@UpnpAction(out = @UpnpOutputArgument(name = "ResultStatus"))
    public Etat getStatus() {
        // Pour ajouter des informations suppl�mentaires UPnP en cas d'erreur :
        // throw new ActionException(ErrorCode.ACTION_NOT_AUTHORIZED);
        return status;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultNbPages"))
    public int getNbPages() {
	    return this.nbPages;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "ResultNumPage"))
    public int getNumPage() {
	    return this.numPage;
    }

    public void setNbPages(int num) {
	    this.nbPages = num;
    }

    public void setNumPage(int num) {
	    this.numPage = num;
    }
}
