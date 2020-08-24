import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class LogicSimulator {
    private Vector<Device> circuits;
    private Vector<Device> iPins;
    private Vector<Device> oPins;

    public LogicSimulator(){
        this.circuits = new Vector<>();
        this.iPins = new Vector<>();
        this.oPins = new Vector<>();
    }

    public boolean load(String filePath){
        boolean isLoad = false;
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            String iPinNum = myReader.nextLine();
            String gateNum = myReader.nextLine();

            createPins(Integer.parseInt(gateNum));

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] splitData = data.split(" ");
                for(int i = 0; i<Integer.parseInt(gateNum) ; i++){
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
                            this.iPins.get(i).addInputPin(this.oPins.get(op-1));
                        }
                        else if(splitData[j].contains("-")){
                            this.iPins.get(i).addInputPin(new IPin());
                        }
                    }
                }
            }
            myReader.close();
            isLoad = true;
        } catch (FileNotFoundException e) {
            System.out.println("byebye.");
        }
        return isLoad;
    }
    public String getSimulationResult(Vector<Boolean> inputs){
        StringBuilder res = new StringBuilder();
        res.append("Simulation Result:\n");
        for(int i=0 ; i<inputs.size() ; i++){
            res.append("i ");
        }
        res.append("| o\n");
        for(int i=0 ; i<inputs.size() ; i++) {
            res.append(i+1).append(" ");
        }
        res.append("| 1\n");
        for(int i=0 ; i<inputs.size() ; i++) {
            res.append(i+1).append(" ");
        }
        res.append("| 1\n");
        for(int i=0 ; i<inputs.size() ; i++) {
            res.append("--");
        }
        res.append("+--\n");
        for(int i=0 ; i<inputs.size() ; i++) {
            res.append(inputs.get(i) ? "1 " : "0 ");
        }
        res.append("| ");
        int iUsedNum = 0;
        for(Device device : this.iPins){
            for(Device pins : device.getIPins()){
                System.out.println(device.getClass().getName());
                if(device.getClass().getName().equals(IPin.class.getName())){
                    device.setInput(inputs.get(iUsedNum));
                    //if(iUsedNum++ >= inputs.size()) break;
                }
            }

        }
        res.append(oPins.get(oPins.size()-1).getOutput() ? "1" : "0");
        return res.toString();
    }
    public String getTruthTable(){
        String res = "";

        return res;
    }

    private void createPins(int cirNum){
        for(int i=0 ; i<cirNum ; i++){
            this.oPins.add(new OPin());
        }
    }
}
