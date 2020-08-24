import java.util.Vector;

public class GateAND extends Device{

    @Override
    public boolean getOutput(){
        boolean result = iPins.get(0).getOutput();
        for(Device device : iPins){
            result &= device.getOutput();
        }
        return result;
    }

}
