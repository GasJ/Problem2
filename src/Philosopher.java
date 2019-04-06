public class Philosopher implements Runnable {
	// define and initialize myId, eat times, monitor and threads
    private int myId;
    private int timesToEat;
    private Monitor monitor;
    public Thread t;
    Philosopher(int myId, int timeToEat, Monitor m){
        this.myId = myId;
        this.timesToEat = timeToEat;
        this.monitor = m;
        t = new Thread(this);
        t.start();
    }
    @Override
    public void run() {
        
        for(int i = 1; i < timesToEat; i++) {
        	// get forks first
        	// eat food second
        	// put down forks third
            monitor.getForks(myId);
            eat(i);
            monitor.relForks(myId);
        }
    }

    public void eat(int count){
        System.out.format("The philosopher %d eat (%d times)\n\r", myId+1, count);
        try {
            Thread.sleep(10);
        }
        catch (InterruptedException e) {}
    }


}