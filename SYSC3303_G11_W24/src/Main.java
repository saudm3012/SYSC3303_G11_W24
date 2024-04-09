import Elevator.ElevatorSubsystem;
import Floor.FloorSubsystem;
import Scheduler.Scheduler;
import Floor.InputReader;
import Gui.ElevatorGUI;

import javax.swing.*;

import java.io.IOException;

// @author Riya Arora for GUI integration (101190033)

public class Main {
    public static void main (String args[]) throws IOException {
        InputReader datafile = new InputReader("data.txt");
        FloorSubsystem floor_sys = new FloorSubsystem();
        Thread floorSubsystem = new Thread(floor_sys);
        ElevatorGUI gui = new ElevatorGUI();
        Thread elevator = new ElevatorSubsystem(22,4, gui);
        Thread scheduler =  new Thread(new Scheduler());

        while(floor_sys.addPacketToQueue(datafile.getNextPacket())){};
        floor_sys.addPacketToQueue(datafile.getNextPacket());

        floorSubsystem.start();
        scheduler.start();
        elevator.start();

        //show the GUI
        SwingUtilities.invokeLater(() -> {
            gui.setVisible(true);
        });
    }
}
