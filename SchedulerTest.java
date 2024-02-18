import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SchedulerTest {

    @Test
    void testSchedulerTransition() {
        // Create an instance of the Scheduler
        Scheduler scheduler = new Scheduler();

        // Use reflection to access the private field 'state' in the Scheduler class
        try {
            Field stateField = Scheduler.class.getDeclaredField("state");
            stateField.setAccessible(true);

            // Verify that the initial state is IDLE
            SchedulerState initialState = (SchedulerState) stateField.get(scheduler);
            System.out.println("[TEST]: Initial State: " + initialState);
            assertEquals(SchedulerState.IDLE, initialState);

            // Add a data packet to the receive queue
            DataPacket dataPacket = new DataPacket("14:05:15.0", "2", "Up", "4");
            scheduler.receiveQueueAdd(dataPacket);
            System.out.println("[TEST]: Data Packet added to receive queue: " + dataPacket);

            // Execute scheduler and verify that the state transitions to WAIT_ACK
            scheduler.execute();
            SchedulerState waitAckState = (SchedulerState) stateField.get(scheduler);
            System.out.println("[TEST]: State after execute: " + waitAckState);

            // Assert that the state transitions to WAIT_ACK
            assertEquals(SchedulerState.WAIT_ACK, waitAckState);

            // Add a delay to allow time for the system to progress
            Thread.sleep(2000);  // Adjust the sleep duration as needed

            // Add additional assertions or print statements to check if the packet is sent
            System.out.println("[TEST]: Packet sent to elevator");

            // Simulate the transition from WAIT_ACK to IDLE
            scheduler.execute();
            SchedulerState idleState = (SchedulerState) stateField.get(scheduler);
            System.out.println("[TEST]: State after execute: " + idleState);

            // Assert that the state transitions back to IDLE
            assertEquals(SchedulerState.IDLE, idleState);

        } catch (NoSuchFieldException | IllegalAccessException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

