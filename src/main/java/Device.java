import java.util.Vector;

public class Device {
    protected Vector<Device> iPins;

    public Device(){
        iPins = new Vector<>();
    }

    public void addInputPin(Device iPin){
        this.iPins.add(iPin);
    }

    public void setInput(boolean value){
        throw new RuntimeException("u cant use this without override");
    }

    public boolean getOutput(){
        throw new RuntimeException("u cant use this without override");
    }
}
