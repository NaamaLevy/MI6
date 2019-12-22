package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {


    private Inventory inventory = Inventory.getInstance();
    String[] gadgets;


    @BeforeEach
    public void setUp(){
        gadgets[0] = "g1";
        gadgets[1] = "g2";
        gadgets[2] = "g3";
        gadgets[3] = "g4";

    }

    @Test
    public void test_load(){
        assertTrue(inventory.getGadgetsList().size()==0);
        inventory.load(gadgets);
        assertTrue(inventory.getGadgetsList().size()==4);
        assertTrue(inventory.getGadgetsList().contains("g1"));
    }

    @Test
    public void test_getItem(){
        assertFalse(inventory.getItem("g5"));
        assertTrue(inventory.getItem("g4"));
        assertFalse(inventory.getItem("g4"));
    }







}
