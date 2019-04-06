import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

enum State {hungry, thinking, eating};

public class Monitor {

	//define and initialize the monitor's lock, state and condition
    final Lock lock;
    private State [] state;
    final Condition[] condition;
    

    public Monitor(){
        lock = new ReentrantLock();
        state = new State[5];
        condition = new Condition[5];
        for(int i = 0; i < 5; i++){
            state[i] = State.thinking;
            condition[i] = lock.newCondition();
        }
    }
    
    
    // build getForks method
    public void getForks(int i){
        lock.lock();
        int resultGet = 0;
        try{
            // Indicate that I want to take forks.
            state[i] = State.hungry;
            // Pick up forks if both neighbors are not eating.
            if( ( state[(i-1+5)%5] != State.eating ) &&
                    (state[(i+1)%5] != State.eating) ){
            	resultGet = i + 1;
                System.out.println("The philosopher " + resultGet +" pick up forks\n");
                state[i] = State.eating;
            }
            else {	// If at least one neighbor is eating, then wait.
                try {
                    condition[i].await();
                    // Eat after waiting.
                    resultGet = i + 1;
                    System.out.println("The philosopher " + resultGet +" pick up forks\n");
                    state[i] = State.eating;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        finally{
            lock.unlock();
        }
    }
    // build relForks method
    public void relForks(int i){
        lock.lock();
        int resultRel = 0;
        try{
        	resultRel = i + 1;
            System.out.println("The philosopher " + resultRel +" put down forks\n");
            state[i] = State.thinking;
            int left = (i - 1 + 5)%5;
            int left2 = (i - 2 +5)%5;
            if( (state[left] == State.hungry) &&
                    (state[left2] != State.eating) ){
                condition[left].signal();
            }
            if( (state[(i+1)%5] == State.hungry) &&
                    (state[(i+2)%5] != State.eating) ){
                condition[(i+1)%5].signal();
            }
        }
        finally {
            lock.unlock();
        }
    }
}
