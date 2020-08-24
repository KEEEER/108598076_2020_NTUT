public class GateOR extends Device {


    @Override
    public boolean getOutput(){
        boolean result = iPins.get(0).getOutput();
        for(Device d : iPins){
            result |= d.getOutput();
        }
        return result;
    }

}
