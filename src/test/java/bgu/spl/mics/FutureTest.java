package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class FutureTest {
    private Future<Integer> testFuture;
    private Integer input = 6;
    private long timeOut = 3;
    private TimeUnit unit = TimeUnit.SECONDS;

    @BeforeEach
    public void setUp() throws Exception{
        testFuture = new Future<>();
    }

    @Test
    public void test_Resolve() throws Exception{
        testFuture.resolve(input);
        assertEquals((Integer)6, testFuture.get());
        assertTrue(testFuture.isDone());
    }

    @Test
    public void test_Get(){
        testFuture.resolve(input);
        assertEquals(testFuture.get(), input);
        assertTrue(testFuture.isDone());
    }
    @Test
    public void test_Get2(){
        testFuture.resolve(input);
        assertEquals(testFuture.get(timeOut, unit), input);
        assertTrue(testFuture.isDone());
    }
    @Test
    public void test_isDone(){
        assertFalse(testFuture.isDone());
        testFuture.resolve(input);
        assertTrue(testFuture.isDone());
        assertTrue(Objects.equals(testFuture.get(), input));
    }
}

