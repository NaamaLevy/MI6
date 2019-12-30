package bgu.spl.mics.application;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Scanner;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.application.passiveObjects.*;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.*;

import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
/** This is the Main class of the application. You should parse the input file, 
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        String jsonInput = args[0];
//        String inventoryOutput = args[1];
//        String diaryOutput = args[2];

        String input = null;
        try{
            input = new Scanner(new File(jsonInput)).useDelimiter("\\Z").next();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        // deserializes json file
        Gson gson = new Gson();
        InputData InputData = gson.fromJson(input,bgu.spl.mics.application.passiveObjects.InputData.class);

        MessageBroker messageBroker = MessageBrokerImpl.getInstance();
        //create passive objects
        Inventory inventory = Inventory.getInstance();
        Squad squad = Squad.getInstance();
        Diary diary = Diary.getInstance();

        //load passive objects with json input
        inventory.load(InputData.getInventoryData());
        squad.load(InputData.getSquad());

        Thread[] threads = new Thread[InputData.getServices().getM()+InputData.getServices().getMoneypenny()+InputData.getServices().getIntelligences().length+2];

        //create and initialize services objects
        TimeService timeService = new TimeService(InputData.getServices().getTime());
        Thread timeThread = new Thread(timeService);
        threads[0]= timeThread;

        Q q = new Q();
        Thread qThread = new Thread(q);
        qThread.start();
        try{
            timeThread.join();
            System.out.println("check joinQ");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        threads[1]= qThread;

        for( int i = 0; i < InputData.getServices().getM(); i++){
            M m = new M(Integer.toString(i));
            Thread mThread = new Thread(m);
            mThread.start();
            System.out.println("check startM");
            try{
                timeThread.join();
                System.out.println("check joinm");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            threads[1+i]= mThread;
        }
        for( int i = 0; i < InputData.getServices().getMoneypenny(); i++) {
            Moneypenny moneypenny = new Moneypenny(Integer.toString(i));
            Thread mpThread = new Thread(moneypenny);
            mpThread.start();
            try{
                timeThread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            threads[2+InputData.getServices().getM()+i]= mpThread;
        }
        for( int i = 0; i < InputData.getServices().getIntelligences().length; i++) {
            InputData.getServices().getIntelligences()[i].setId(i);
            Thread inThread = new Thread(InputData.getServices().getIntelligences()[i]);
            inThread.start();
            try{
                timeThread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            threads[3+InputData.getServices().getMoneypenny()+InputData.getServices().getM()+i]= inThread;
        }
//        TimeService timeService = new TimeService(InputData.getServices().getTime());
//        Thread timeThread = new Thread(timeService);
        timeThread.start();
//        try{
//            timeThread.join();
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
    }
}
