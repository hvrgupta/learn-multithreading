import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    private int cnt = 0;

    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + ": " + "acquired the lock");
            cnt++;
            System.out.println(Thread.currentThread().getName() + " incremented " + cnt);
        }finally {
            System.out.println(Thread.currentThread().getName() + " : released the lock");
            lock.unlock();
        }
    }

    public int getCount() {
        return cnt;
    }

    public static void main(String[] args) {
        ReentrantLockExample reentrantLockExample = new ReentrantLockExample();

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.submit(reentrantLockExample::increment);
        }

        executorService.shutdown();
        try {
            // Wait for all tasks to finish; if not completed within 5 seconds, then exit.
            if (executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Final counter value: " + reentrantLockExample.getCount());
            } else {
                System.out.println("Timeout: Not all tasks finished.");
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting for tasks to finish.");
            Thread.currentThread().interrupt();
        }
    }
}
