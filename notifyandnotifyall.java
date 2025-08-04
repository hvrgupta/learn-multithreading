class SharedResource {
    synchronized void waitExample() {
        System.out.println(Thread.currentThread().getName() + ": is waiting..");
        try {
            wait(); // Releases the lock (so that other thread can acquire and do the task above)
                    // and waits (the thread which releases the lock waits)
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": resumed after notify");
    }
    /*
    When notify() or notifyAll() is called, the waiting thread doesn't immediately start running.
    Instead it follows these steps:
        1. When another thread calls notify(), one waiting thread is moved to the blocked (or runnable) state.
        but it does not start execution immediately
        2. The notified thread cannot resume execution until it successfully acquires the lock on the sync. object.
        3. If multiple threads are waiting:
             notify() wakes up one waiting thread
             notifyAll() wakes up all waiting threads, but they still compete for the lock
        4. Once the thread reacquires the lock, it continues execution from where it called the wait()
     */

    synchronized void notifyExample() {
        System.out.println("Notifying a waiting thread");
        notify(); // wakes up one waiting thread
//        notifyAll(); // wakes up all (waiting) threads
    }
}

public class notifyandnotifyall {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();
//      Thread 1 waits
        Thread t1 = new Thread(() -> resource.waitExample(),"thread1");
        Thread t2 = new Thread(() -> resource.waitExample(),"thread2");

//      Notifier notifies after 2 seconds
        Thread notifier = new Thread(() -> {
            try {
//      Ensure thread1 goes to wait state
                Thread.sleep(2000);
                resource.notifyExample();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                Thread.sleep(1000);
                resource.notifyExample();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"notifier");

        t1.start();
        t2.start();
        notifier.start();
    }
}
