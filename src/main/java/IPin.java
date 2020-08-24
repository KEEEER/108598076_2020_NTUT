public class IPin extends Device{
    private boolean status = true;

    @Override
    public void setInput(boolean value){
        this.status = value;
    }
    @Override
    public boolean getOutput(){
        return this.status;
    }
}
