import org.junit.*;

import static org.junit.Assert.*;

public class DeviceTest{

    @Before
    public void setUp(){

    }

    @Test
    public void testDevicePolymorphism(){
        Device device = new IPin();
        assertEquals(IPin.class.getName(), device.getClass().getName());
        device = new OPin();
        assertEquals(OPin.class.getName(), device.getClass().getName());
        device = new GateNOT();
        assertEquals(GateNOT.class.getName(), device.getClass().getName());
        device = new GateAND();
        assertEquals(GateAND.class.getName(), device.getClass().getName());
        device = new GateOR();
        assertEquals(GateOR.class.getName(), device.getClass().getName());
    }

    @Test
    public void testIPinAndOPin(){

        // 0 = 0
        IPin iPin = new IPin();
        iPin.setInput(false);
        OPin oPin = new OPin();
        oPin.addInputPin(iPin);
        assertFalse(oPin.getOutput());

        // 1 = 1
        iPin = new IPin();
        iPin.setInput(true);
        oPin = new OPin();
        oPin.addInputPin(iPin);
        assertTrue(oPin.getOutput());
    }

    @Test
    public void testGateNOT()
    {
        // NOT 0 = 1
        IPin iPin = new IPin();
        iPin.setInput(false);
        GateNOT gateNOT = new GateNOT();
        gateNOT.addInputPin(iPin);

        assertTrue( gateNOT.getOutput());

        // NOT 1 = 0
        iPin = new IPin();
        iPin.setInput(true);
        gateNOT = new GateNOT();
        gateNOT.addInputPin(iPin);

        assertFalse( gateNOT.getOutput());
    }

    @Test
    public void testGateAND()
    {
        // 0 AND 0 = 0
        IPin iPin1 = new IPin();
        IPin iPin2 = new IPin();
        iPin1.setInput(false);
        iPin2.setInput(false);

        GateAND gateAND = new GateAND();
        gateAND.addInputPin(iPin1);
        gateAND.addInputPin(iPin2);

        assertFalse( gateAND.getOutput());

        // 0 AND 1 = 0
        iPin1 = new IPin();
        iPin2 = new IPin();
        iPin1.setInput(false);
        iPin2.setInput(true);

        gateAND = new GateAND();
        gateAND.addInputPin(iPin1);
        gateAND.addInputPin(iPin2);

        assertFalse(gateAND.getOutput());
        // 1 AND 0 = 0
        iPin1 = new IPin();
        iPin2 = new IPin();
        iPin1.setInput(true);
        iPin2.setInput(false);

        gateAND = new GateAND();
        gateAND.addInputPin(iPin1);
        gateAND.addInputPin(iPin2);

        assertFalse(gateAND.getOutput());

        // 1 AND 1 = 1
        iPin1 = new IPin();
        iPin2 = new IPin();
        iPin1.setInput(true);
        iPin2.setInput(true);

        gateAND = new GateAND();
        gateAND.addInputPin(iPin1);
        gateAND.addInputPin(iPin2);

        assertTrue(gateAND.getOutput());
    }

    @Test
    public void testGateOR()
    {
        // 0 OR 0 = 0
        IPin iPin1 = new IPin();
        IPin iPin2 = new IPin();
        iPin1.setInput(false);
        iPin2.setInput(false);

        GateOR gateOR = new GateOR();
        gateOR.addInputPin(iPin1);
        gateOR.addInputPin(iPin2);

        assertFalse(gateOR.getOutput());
        // 0 OR 1 = 1
        iPin1 = new IPin();
        iPin2 = new IPin();
        iPin1.setInput(true);
        iPin2.setInput(false);

        gateOR = new GateOR();
        gateOR.addInputPin(iPin1);
        gateOR.addInputPin(iPin2);

        assertTrue(gateOR.getOutput());

        // 1 OR 0 = 1
        iPin1 = new IPin();
        iPin2 = new IPin();
        iPin1.setInput(true);
        iPin2.setInput(true);

        gateOR = new GateOR();
        gateOR.addInputPin(iPin1);
        gateOR.addInputPin(iPin2);

        assertTrue(gateOR.getOutput());

        // 1 OR 1 = 1
        iPin1 = new IPin();
        iPin2 = new IPin();
        iPin1.setInput(true);
        iPin2.setInput(true);

        gateOR = new GateOR();
        gateOR.addInputPin(iPin1);
        gateOR.addInputPin(iPin2);

        assertTrue(gateOR.getOutput());

    }

    @After
    public void tearDown(){

    }
}
