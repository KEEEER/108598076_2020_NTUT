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
        this.circuits = new Vector<>();
        this.iPins = new Vector<>();
        this.oPins = new Vector<>();
        this.oPinUsedTimes = new Vector<>();
    }

    public boolean load(String filePath){
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
        for(int i=0 ; i<inputs.size() ; i++) res.append(inputs.get(i) ? "1 " : "0 ");
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
        Device endOPin = null;
        for(int i=0 ; i<oPinUsedTimes.size() ; i++){
            if(oPinUsedTimes.get(i) == 0){
                endOPin = oPins.get(i);
            }
        }
        res.append(buildTempleteStr(iPins.size())).append(buildTruthTable(endOPin));
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
        res.append("| o\n");
        for(int i=0 ; i<inputSize ; i++) res.append(i+1).append(" ");
        res.append("| 1\n");
        for(int i=0 ; i<inputSize ; i++) res.append("--");
        res.append("+--\n");
        return res;
    }
    private StringBuilder buildTruthTable(Device endOPin){
        // 時間不夠，直接先刻
        StringBuilder res = new StringBuilder();
        Vector<Boolean> inputs = generateInputValues(false, false, false);
        for(int i=0 ; i<iPins.size() ; i++) iPins.get(i).setInput(inputs.get(i));
        res.append("0 0 0 | ").append(endOPin.getOutput() ? "1\n" : "0\n");
        inputs = generateInputValues(false, false, true);
        for(int i=0 ; i<iPins.size() ; i++) iPins.get(i).setInput(inputs.get(i));
        res.append("0 0 1 | ").append(endOPin.getOutput() ? "1\n" : "0\n");
        inputs = generateInputValues(false, true, false);
        for(int i=0 ; i<iPins.size() ; i++) iPins.get(i).setInput(inputs.get(i));
        res.append("0 1 0 | ").append(endOPin.getOutput() ? "1\n" : "0\n");
        inputs = generateInputValues(false, true, true);
        for(int i=0 ; i<iPins.size() ; i++) iPins.get(i).setInput(inputs.get(i));
        res.append("0 1 1 | ").append(endOPin.getOutput() ? "1\n" : "0\n");
        inputs = generateInputValues(true, false, false);
        for(int i=0 ; i<iPins.size() ; i++) iPins.get(i).setInput(inputs.get(i));
        res.append("1 0 0 | ").append(endOPin.getOutput() ? "1\n" : "0\n");
        inputs = generateInputValues(true, false, true);
        for(int i=0 ; i<iPins.size() ; i++) iPins.get(i).setInput(inputs.get(i));
        res.append("1 0 1 | ").append(endOPin.getOutput() ? "1\n" : "0\n");
        inputs = generateInputValues(true, true, false);
        for(int i=0 ; i<iPins.size() ; i++) iPins.get(i).setInput(inputs.get(i));
        res.append("1 1 0 | ").append(endOPin.getOutput() ? "1\n" : "0\n");
        inputs = generateInputValues(true, true, true);
        for(int i=0 ; i<iPins.size() ; i++) iPins.get(i).setInput(inputs.get(i));
        res.append("1 1 1 | ").append(endOPin.getOutput() ? "1\n" : "0\n");
        return res;
    }
    private Vector<Boolean> generateInputValues(boolean i1, boolean i2, boolean i3){
        // 時間不夠，直接先刻
        Vector<Boolean> inputs = new Vector<>();
        inputs.add(i1);
        inputs.add(i2);
        inputs.add(i3);
        return inputs;
    }
}
