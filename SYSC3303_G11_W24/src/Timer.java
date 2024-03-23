/**
 * The Timer class represents a simple timer that can be used to manage timeouts in a state machine.
 * It extends the Thread class to run as a separate thread.
 * 
 * @author Zakariya Khan 101186641
 */
public class Timer extends Thread {

    private Thread  wrapper; // Reference to the state machine
    private boolean isSet; // Flag indicating whether the timer is set
    private int setTime; // Time duration for the timer in milliseconds

    /**
     * Constructs a new Timer object with a reference to the context wrapper.
     *
     * @param wrapper The context wrapper for the state machine.
     */
    public Timer(Thread wrapper){
        this.wrapper = wrapper;
        this.isSet = false;
    }

    /**
     * Sets the timer duration in seconds.
     *
     * @param seconds The time duration for the timer in miliseconds.
     */
    public void set(int ms){
        setTime = ms;
        isSet = true;
    }

    /**
     * clears the timer.
     */
    public void clear(){
        isSet = false;
        setTime = 0;
    }
    
    /**
     * The run method of the Timer thread.
     * It continuously checks whether the timer is set and triggers the timeout event in the context wrapper
     * after the specified time duration elapses.
     */
    public void run(){
        while(true){
            if (isSet) {
                try {
                    Thread.sleep(setTime); 
                    wrapper.interrupt(); // Interrupt the thread to indicate timeout 
                    isSet = false; // Reset the timer flag
                    
                } catch (InterruptedException e){
                    clear();
                }
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e){
                    clear();
                }
            }
        }
    }
}
