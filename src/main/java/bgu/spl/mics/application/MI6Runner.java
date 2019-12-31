package bgu.spl.mics.application;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.*;
import java.io.*;
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

        int mCount = InputData.getServices().getM();
        int mpCount = InputData.getServices().getMoneypenny();
        int intelCount = InputData.getServices().getIntelligences().length;
        int counter = 0;

        Runnable [] runnables = new Runnable[mCount+mpCount+intelCount+2];
        List<Thread> threads = new ArrayList<>();
        //create and initialize services objects
        TimeService timeService = new TimeService(InputData.getServices().getTime());
        runnables[counter] = timeService;
        counter++;

        Q q = new Q();
        runnables[counter] = q;
        messageBroker.register(q);

        counter++;

        for( int i = 0; i < mCount; i++){
            M m = new M(Integer.toString(i));
            runnables[counter] = m;
            messageBroker.register(m);

            counter++;
        }
        for( int i = 0; i < mpCount; i++) {
            Moneypenny moneypenny = new Moneypenny(Integer.toString(i));
            runnables[counter] = moneypenny;
            messageBroker.register(moneypenny);

            counter++;
        }
        for( int i = 0; i < intelCount; i++) {
            Intelligence intelligence = new Intelligence(InputData.getServices().getIntelligences()[i].getMission());
            intelligence.setId(i);
            runnables[counter] = intelligence;
            messageBroker.register(intelligence);

            counter++;
        }
        for (int i=0; i<runnables.length; i++) {
            Thread sub = new Thread(runnables[i]);
            if(i!=0)
            threads.add(sub);
            sub.setDaemon(true);
            sub.start();
        }

            for(Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }
    }
}
