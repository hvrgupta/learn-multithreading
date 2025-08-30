/*
methods like wait, notify, notifyAll()

they all should be called from inside synchronized method or block
 */

public class Locks {

    private final Object lock = new Object();
    private boolean conditionMet = false;

    public void doWait() {
        synchronized (lock) {
            while (!conditionMet) {
                try {
                    System.out.println(Thread.currentThread().getName() + ": Waiting for condition");
                    lock.wait();
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(Thread.currentThread().getName() + ": Interrupted while waiting for condition");
                }
            }
            System.out.println(Thread.currentThread().getName() + ": conditionMet");
        }
    }

//    sets the condition to true and calls notify() so that one waiting thread is awakened
    public void doNotify() {
        synchronized (lock) {
            conditionMet = true;
            System.out.println(Thread.currentThread().getName() + ": called Notify");
            lock.notify();
        }
    }

    //  sets the condition to true and calls notifyAll() so that all waiting threads are awakened
    public void doNotifyAll() {
        synchronized (lock) {
            conditionMet = true;
            System.out.println(Thread.currentThread().getName() + ": called NotifyAll");
            lock.notifyAll(); // Wakes up all waiting threads
        }
    }

    public static void main(String[] args) {
        System.out.println("Demonstrating DoNotifyAll()");
        Locks locks = new Locks();

        Thread t1 = new Thread(locks::doWait,"Waiter-1");
        Thread t2 = new Thread(locks::doWait,"Waiter-2");
        Thread t3 = new Thread(locks::doWait,"Waiter-3");

        t1.start();
        t2.start();
        t3.start();

        try {
            Thread.sleep(2000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread notifierAll = new Thread(() -> locks.doNotifyAll(),"Notifier-All");
        notifierAll.start();

        try{
            t1.join();
            t2.join();
            t3.join();
            notifierAll.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
