package Gui;


import javax.swing.*;
import java.awt.*;

// @author Riya Arora (101190033)

public class ElevatorGUI extends JFrame {

    private static JLabel[] elevatorCurrentFloor;
    // private static JLabel[] elevatorDestinationFloor;
    private static JLabel[] elevatorCurrentStatus;
    private static JLabel[] elevatorPassengers;
    private static ElevatorPanel[] elevatorPanels;

    public ElevatorGUI() {
        // Set up the main window
        super("Elevator GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new GridLayout(2, 4));// 2 rows 4 elevators
        setLocationRelativeTo(null);

        // Create the elevator status panels
        elevatorCurrentFloor = new JLabel[4];
        // elevatorDestinationFloor = new JLabel[3];
        elevatorCurrentStatus = new JLabel[4];
        elevatorPassengers = new JLabel[4]; // 4 elevators
        JPanel[] elevatorStatusPanels = new JPanel[4];
        for (int i = 0; i < 4; i++) {
            elevatorCurrentFloor[i] = new JLabel("Elevator" + (i + 1) + " current floor: 0");
            //elevatorDestinationFloor[i] = new JLabel("Elevator" + (i + 1) + " destination floor: 0");
            elevatorCurrentStatus[i] = new JLabel("Elevator" + (i + 1)  + " state: IDLE");
            elevatorPassengers[i] = new JLabel("Elevator" + (i + 1)  + " has 0 passengers");
            elevatorStatusPanels[i] = new JPanel();
            elevatorStatusPanels[i].setLayout(new GridLayout(3, 1));
            elevatorStatusPanels[i].setBackground(Color.LIGHT_GRAY);
            elevatorStatusPanels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            elevatorStatusPanels[i].add(elevatorCurrentFloor[i]);
            //elevatorStatusPanels[i].add(elevatorDestinationFloor[i]);
            elevatorStatusPanels[i].add(elevatorCurrentStatus[i]);
            elevatorStatusPanels[i].add(elevatorPassengers[i]);
            add(elevatorStatusPanels[i]);
        }

        // Create the elevator panels
        elevatorPanels = new ElevatorPanel[4]; // 3 elevators
        for (int i = 0; i < 4; i++) { // 3 elevators
            elevatorPanels[i] = new ElevatorPanel();
            add(elevatorPanels[i]);
        }

        // Show the GUI
        setVisible(true);
    }

    public void updateStatus(int elevatorId, int currentFloor, String state, int numPassengers, int destinationFloor) {
        if (elevatorId >= 0 && elevatorId < 4) {
            elevatorCurrentFloor[elevatorId].setText("Elevator" + (elevatorId + 1) + " current floor: " + currentFloor);
            //elevatorDestinationFloor[elevatorId].setText("Elevator" + (elevatorId + 1) + " destination floor: " + destinationFloor);
            elevatorCurrentStatus[elevatorId].setText("Elevator" + (elevatorId + 1) + " state: " + state);
            elevatorPassengers[elevatorId].setText("Elevator" + (elevatorId + 1) + " has " + numPassengers + " passengers");
            elevatorPanels[elevatorId].setCurrentFloor(currentFloor);
            elevatorPanels[elevatorId].setDestinationFloor(destinationFloor);
            elevatorPanels[elevatorId].setNumPassengers(numPassengers);
            elevatorPanels[elevatorId].repaint();
        } else {
            System.err.println("Invalid elevator ID: " + elevatorId);
        }
        System.out.println(elevatorId + " " + currentFloor + " " + state + " " + numPassengers + " " + destinationFloor
        );
    }

    public int getCurrentFloor(int elevatorId) {
        //elevator logic to get the current floor of the elevator with ID elevatorId
        return elevatorPanels[elevatorId].currentFloor;
    }

    public int getDestinationFloor(int elevatorId) {
        return elevatorPanels[elevatorId].destinationFloor;
    }

    public static void main(String[] args) {
        // Create and show the GUI with 3 elevators and 5 floors
        SwingUtilities.invokeLater(ElevatorGUI::new);
    }

    public class ElevatorPanel extends JPanel {
        private int currentFloor = 0;
        private int destinationFloor = 5;

        public void setCurrentFloor(int currentFloor) {
            this.currentFloor = currentFloor;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the elevator cabin
            g.setColor(Color.MAGENTA); // Elevator color
            int elevatorWidth = 100;
            int elevatorHeight = 20;
            int elevatorX = 50 + 200 * (getWidth() / 1200);
            int elevatorY = getHeight() - currentFloor * (getHeight() / 22) - elevatorHeight / 2;
            g.fill3DRect(elevatorX, elevatorY, elevatorWidth, elevatorHeight, true); // Fill with 3D effect
            g.setColor(Color.BLACK);
            g.drawRect(elevatorX, elevatorY, elevatorWidth, elevatorHeight); // Draw outline

            // Draw the rope
            g.setColor(Color.GRAY);
            int ropeX = elevatorX + elevatorWidth / 2;
            int ropeY = 0;
            int ropeEndY = elevatorY;
            g.drawLine(ropeX, ropeY, ropeX, ropeEndY); // Draw rope

            // Draw the floors
            g.setColor(Color.BLACK); // Floor color
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            for (int i = 0; i <= 22; i++) { // 5 floors
                g.drawString(Integer.toString(i), 5 + 200 * (getWidth() / 1200), getHeight() - i * (getHeight() / 22) + 5);
                g.drawLine(50 + 200 * (getWidth() / 1200), getHeight() - i * (getHeight() / 22), 150 + 200 * (getWidth() / 1200), getHeight() - i * (getHeight() / 22));
            }
        }

        public void setDestinationFloor(int destinationFloor) {
            this.destinationFloor = destinationFloor;
        }

        public void setNumPassengers(int numPassengers) {
        }
    }
}
