package info5.sar.EventBasedMessageQueue.Impl;

public class Timer extends Thread {

    private static final long MAX_EXECUTION_TIME_MS = 500; 
    private Thread targetThread;

    public Timer(Thread targetThread) {
        this.targetThread = targetThread;
    }

    @Override
    public void run() {
        try {
            
            Thread.sleep(MAX_EXECUTION_TIME_MS);
            
            if (targetThread.isAlive()) {
                targetThread.interrupt();  // Interruption du thread de l'EventPump
            }
            
        } catch (InterruptedException e) {
        	System.out.println("jebvbueu");
        }
    }
}
