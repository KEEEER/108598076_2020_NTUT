import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class LogicSimulator {
    private Vector<Device> circuits;
    private Vector<Device> iPins;
    private Vector<Device> oPins;
    private Vector<Integer> oPinUsedTimes;

    public LogicSimulator(){
        clearAll();
    }

    public boolean load(String filePath){
        clearAll();

        boolean isLoad = false;
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            String iPinNum = myReader.nextLine();
            String gateNum = myReader.nextLine();

            createPins(Integer.parseInt(gateNum), Integer.parseInt(iPinNum));
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitData = data.split(" ");
                // 1 for and
                if(splitData[0].equals("1")){
                    circuits.add(new GateAND());
                }
                // 2 for or
                else if(splitData[0].equals("2")){
                    circuits.add(new GateOR());
                }
                // 3 for not
                else{
                    circuits.add(new GateNOT());
                }

                for (int j=1 ; j<splitData.length ; j++){

                    if(splitData[j].contains(".")){
                        int op = (int)Double.parseDouble(splitData[j]);
                        circuits.get(circuits.size()-1).addInputPin(oPins.get(op-1));
                        oPinUsedTimes.set(op-1, oPinUsedTimes.get(op-1)+1);
                    }
                    else if(splitData[j].contains("-")){
                        int op = (-1) * Integer.parseInt(splitData[j]);
                        circuits.get(circuits.size()-1).addInputPin(iPins.get(op-1));
                    }
                }
            }

            for(int i=0 ; i<oPins.size() ; i++){
                oPins.get(i).addInputPin(circuits.get(i));
            }

            myReader.close();
            isLoad = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return isLoad;
    }
    public String getSimulationResult(Vector<Boolean> inputs){

        for(int i=0 ; i<inputs.size() ; i++){
            iPins.get(i).setInput(inputs.get(i));
        }

        StringBuilder res = new StringBuilder();
        res.append("Simulation Result:\n");
        res.append(buildTempleteStr(inputs.size()));
        for (Boolean input : inputs) res.append(input ? "1 " : "0 ");
        res.append("| ");

        for(Integer i :oPinUsedTimes){
            if(i == 0){
                res.append(oPins.get(i).getOutput() ? "1\n" : "0\n");
            }
        }
        return res.toString();
    }
    public String getTruthTable(){
        StringBuilder res = new StringBuilder();
        res.append("Truth table:\n");
        res.append(buildTempleteStr(iPins.size())).append(buildTruthTable());
        return res.toString();
    }

    private void createPins(int cirNum, int iPinNum){
        for(int i=0 ; i<iPinNum ; i++){
            this.iPins.add(new IPin());
        }
        for(int i=0 ; i<cirNum ; i++){
            this.oPins.add(new OPin());
            this.oPinUsedTimes.add(0);
        }
    }
    private StringBuilder buildTempleteStr(int inputSize){
        StringBuilder res = new StringBuilder();
        for(int i=0 ; i<inputSize ; i++) res.append("i ");
        res.append("|");
        for(int i=0 ; i<getEndOPinNum() ; i++) res.append(" o");
        res.append("\n");
        for(int i=0 ; i<inputSize ; i++) res.append(i+1).append(" ");
        res.append("|");
        for(int i=0 ; i<getEndOPinNum() ; i++) res.append(" ").append(i+1);
        res.append("\n");
        for(int i=0 ; i<inputSize ; i++) res.append("--");
        res.append("+");
        for(int i=0 ; i<getEndOPinNum() ; i++) res.append("--");
        res.append("\n");
        return res;
    }
    private StringBuilder buildTruthTable(){

        StringBuilder res = new StringBuilder();
        String binStr = null;
        for(int i=0 ;i<Math.pow(2.0, iPins.size()) ; i++){
            binStr = Integer.toBinaryString(i);
            String paddingZeroStr = "%0" + String.valueOf(iPins.size()) + "d";
            binStr = String.format(paddingZeroStr, Integer.parseInt(binStr));
            int startIndex = binStr.length() - iPins.size();
            for(int j=startIndex ; j<binStr.length() ; j++){
                iPins.get(j-startIndex).setInput(binStr.charAt(j) == '1');
                res.append(binStr.charAt(j)).append(" ");
            }
            res.append("|");
            for (Device device : getEndOPins()){
                res.append(device.getOutput() ? " 1" : " 0");
            }
            res.append("\n");
        }
        return res;
    }
    private int getEndOPinNum(){
        int count = 0;
        for(int usedTimes :oPinUsedTimes){
            count += usedTimes == 0 ? 1 : 0;
        }
        return count;
    }
    private Vector<Device> getEndOPins(){
        Vector<Device> devices = new Vector<>();
        for(int i=0 ; i<oPinUsedTimes.size() ; i++){
            if(oPinUsedTimes.get(i) == 0){
                devices.add(oPins.get(i));
            }
        }
        return  devices;
    }
    private void clearAll(){
        this.circuits = new Vector<>();
        this.iPins = new Vector<>();
        this.oPins = new Vector<>();
        this.oPinUsedTimes = new Vector<>();
    }
}
