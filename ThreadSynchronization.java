/*
The synchronized keyword is used to control access to critical sections of code so that one thread can
execute the synchronized code at a time.

1. Synchronized method
    when you declare entire method as synchronized, the lock is acquired on:
    The object instance for non static methods
    Or on the class object for static methods

    lock is acquired before the method is executed and released after it finishes.

    Useful when the entire method represents a critical section where no concurrent execution is desired.

2. Synhronized Block
    Allows you to specify a particular block of code to be synchronized, along with the object on which to acquire the lock (often called a monitor)
    more fine grained component
 */

/*
Volatile keyword
ensures that changes to variable are immediately visible to all threads

Atomic Variable
    designed to support lock free, thread safe operations on single variables (incrementing, dec, updating) on shared variables in a
    multithreaded environment


 */

public class ThreadSynchronization {

    private int cnt = 0;
//    synchronized method
//    public synchronized void increment() {

    private final Object lock = new Object();

    public void increment() {
        System.out.println("Synchronization method - start increment: " + Thread.currentThread().getName());

        synchronized (lock) {
            System.out.println("Inside block: " + Thread.currentThread().getName());
            cnt++;
        }
//        cnt++;
        System.out.println("Synchronization method - counter: " + cnt);
        System.out.println("Synchronization method - end increment: " + Thread.currentThread().getName());
    }

    public int getCount() {
        return cnt;
    }

    public static void main(String[] args) {
        ThreadSynchronization ts = new ThreadSynchronization();
        int noOfThreads = 5;
        Thread[] threads = new Thread[noOfThreads];

        for (int i = 0; i < noOfThreads; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    ts.increment();
                }
            },"Thread-" + (i+1));
            threads[i].start();
        }

        for (int i = 0; i < noOfThreads; i++) {
            try {
                threads[i].join();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("final counter value " + ts.getCount());
    }
}
