import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;

public class ElevatorData implements Serializable {
    public LocalTime time; // Time
    public int floor; // Floor
    public boolean up_button; // True if button is up, false if button is down
    public int car_button;

    public ElevatorData(){
        this.time = LocalTime.of(14,05,05, 0);
        this.floor = 2;
        this.up_button = true;
        this.car_button = 4;
    }

}

