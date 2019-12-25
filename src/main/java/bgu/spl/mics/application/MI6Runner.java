package bgu.spl.mics.application;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import bgu.spl.mics.application.passiveObjects.*;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.*;

import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
/** This is the Main class of the application. You should parse the input file, 
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        System.out.println("hello");
        //String jsonInput = args[0];
        String jsonInput = "C:\\Users\\hille\\Desktop\\Uni\\semester c\\System programing\\MI6\\input2012.json";
      /*  String inventoryOutput = args[1];
        String diaryOutput = args[2];*/

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

        //creates passive objects
        Inventory inventory = Inventory.getInstance();
        Squad squad = Squad.getInstance();

        //load objects with json input
        inventory.load(InputData.getInventoryData());
        squad.load(InputData.getSquad());

        //creates services objects
        for( int i = 0; i < InputData.getServices().getM(); i++){
            M m = new M(Integer.toString(i));

            //TODO: push new M's to DataStructure
        }
        for( int i = 0; i < InputData.getServices().getMoneypenny(); i++) {
            Moneypenny moneypenny = new Moneypenny(Integer.toString(i));
            //TODO: push new Moneypenny's to DataStructure

        }
        for( int i = 0; i < InputData.getServices().getIntelligences().length; i++)
            InputData.getServices().getIntelligences()[i].setId(i);

        TimeService timeService = new TimeService(InputData.getServices().getTime());
    }
}
