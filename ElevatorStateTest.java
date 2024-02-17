import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;
public class ElevatorStateTest {
    public static Elevator elevator;

    /**
     * Initialize the test harness.
     */
    @BeforeEach
    public void setup() {
        //create an elevator which has 5 floors
        elevator = new Elevator(5);
    }

    /**
     * Tests elevator state machine.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void testElevatorStateMachine(){
        assertTrue(elevator.state == ElevatorStates.IDLE);
    }

}


