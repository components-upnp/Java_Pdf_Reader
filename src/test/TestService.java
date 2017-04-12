package test;
import com.examples.upnp.VisionneuseServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by comkostiuk on 12/04/2017.
 */
public class TestService {
    private volatile Thread server;
    private Device device;


    @Before
    public void setUp() {
        server = new Thread(new Runnable() {
            private volatile Thread visio;

            @Override
            public void run() {
                VisionneuseServer s = new VisionneuseServer();
                s.run();
                System.out.println("LOLOLOLOLOLOLOL");
                s.getControl().openDocTest();
            }
        });
        server.start();
        pause(1000);
        this.device = new Device();
    }

    @After
    public void done() throws SpyNotRunning {
        this.server.interrupt();
        this.device.stopSpy();
    }

    @Test
    public void testNbPages() throws NoDevice, NoService, NotLaunched {
        assertEquals(114,this.device.getNbPages());
    }

    @Test
    public void testSlideSuivanteValide() throws NoDevice, NoService, NotLaunched {
        int numInitial = this.device.getNumPage();
        this.device.avancer();
        this.device.avancer();
        assertEquals(numInitial+2,this.device.getNumPage());
    }

    @Test
    public void testNumPageInitial() throws NoDevice, NoService, NotLaunched {
        assertEquals(1,this.device.getNumPage());
    }

    @Test
    public void testSlidePrecedenteInvalide() throws NoDevice, NoService, NotLaunched {
        int numInitial = this.device.getNumPage();
        this.device.reculer();
        this.device.reculer();
        assertEquals(numInitial,this.device.getNumPage());
    }

    @Test
    public void testSlideSuivanteInvalide() throws NoDevice, NoService, NotLaunched {
        for (int i = 0; i < 114; i++)
            this.device.avancer();

        this.device.avancer();
        this.device.avancer();

        assertEquals(114, this.device.getNbPages());
    }

    public static void pause(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

}
