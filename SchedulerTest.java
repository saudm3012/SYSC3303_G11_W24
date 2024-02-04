import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {

    private Scheduler scheduler;

    // Redirect standard output for console printing
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() throws SocketException {
        // Redirect standard output for console printing to capture print statements
        System.setOut(new PrintStream(outContent));

        // Create a Scheduler instance for testing
        scheduler = new Scheduler();
    }

    @AfterEach
    void tearDown() {
        // Reset standard output
        System.setOut(originalOut);

        // Close sockets
        if (scheduler.sendSocket != null && !scheduler.sendSocket.isClosed()) {
            scheduler.sendSocket.close();
        }

        if (scheduler.receiveSocket != null && !scheduler.receiveSocket.isClosed()) {
            scheduler.receiveSocket.close();
        }
    }

    @Test
    void testConstructor() {
        assertNotNull(scheduler.sendSocket);
        assertNotNull(scheduler.receiveSocket);
        assertNotNull(scheduler.floorAddress);
        assertNotNull(scheduler.elevatorAddress);
        assertTrue(scheduler.receiveQueue.isEmpty());
    }

    @Test
    void testSleep() {
        assertDoesNotThrow(() -> scheduler.sleep(100));
    }

    /*
    @Test
    void testPrintSendingInfo() {
        scheduler.printSendingInfo("Test DataPacket", true);

        // Check if the correct information is printed to the console
        String expectedOutput = "Scheduler: Sending packet to: Elevator\n" +
                "To host: " + scheduler.sendPacket.getAddress() + "\n" +
                "Destination host port: " + scheduler.sendPacket.getPort() + "\n" +
                "Length: " + scheduler.sendPacket.getLength() + "\n" +
                "Containing: Test DataPacket\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    */

    /*
    @Test
    void testPrintReceivingInfo() {
        scheduler.printReceivingInfo("Test DataPacket", true);

        // Check if the correct information is printed to the console
        String expectedOutput = "Scheduler: Packet received from: Floor Subsystem\n" +
                "From host: " + scheduler.receivePacket.getAddress() + "\n" +
                "Host port: " + scheduler.receivePacket.getPort() + "\n" +
                "Length: " + scheduler.receivePacket.getLength() + "\n" +
                "Containing: Test DataPacket\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    */

    @Test
    void testClose() {
        assertDoesNotThrow(() -> scheduler.close());
        assertTrue(scheduler.sendSocket.isClosed());
        assertTrue(scheduler.receiveSocket.isClosed());
    }

    // Add more tests for other methods as needed
}
