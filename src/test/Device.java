package test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by comkostiuk on 10/04/2017.
 */
public class Device extends Spy {

    public Device() {super("Lecteur PDF");}

    public void avancer() throws NoDevice, NoService, NotLaunched {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("NewTargetValue","DROITE");
        super.launchAction("VisionneuseService","SetTarget",arguments);
    }

    public void reculer() throws NoDevice, NoService, NotLaunched {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        arguments.put("NewTargetValue","GAUCHE");
        super.launchAction("VisionneuseService","SetTarget",arguments);
    }

    public int getNbPages() throws NoDevice, NoService, NotLaunched {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        ArrayList<Object> val = super.launchAction("VisionneuseService","GetNbPages",arguments);
        System.out.println(val);
        return (int) val.get(0);
    }

    public int getNumPage() throws NoDevice, NoService, NotLaunched {
        HashMap<String, Object> arguments = new HashMap<String, Object>();
        ArrayList<Object> val = super.launchAction("VisionneuseService","GetNumPage",arguments);
        System.out.println(val);
        return (int) val.get(0);
    }

}
