package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.application.subscribers.Intelligence;

public class services {
    //json services fields
    private int M;
    private int Moneypenny;
    private Intelligence[] intelligence;
    private int time;

    //fields getters
    public int getM(){ return M; }
    public int getMoneypenny(){ return Moneypenny; }
    public Intelligence[] getIntelligences() { return intelligence;}
    public int getTime() {return time;}
}
