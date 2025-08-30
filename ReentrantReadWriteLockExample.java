/*
ReadWriteLock divides the lock in 2 parts - a read lock and write lock
useful when:
    Multiple threads need to read (can read concurrently if there's no write happening)
    Exclusive writing (When a thread is updating data, no other thread (reader or writer) is allowed to access the resource.

Used in the scenarios where there are many more read operations that write operations
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockExample {

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private int logVal = 0;

//  Simulate processing work using dummy loop
    private void simulateWork() {
        long sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
    }

    private void writeValue(String taskName, int value) {
        rwLock.writeLock().lock();
        try {
            System.out.println(taskName + " (write): acquired lock");
            simulateWork();
            logVal = value;
            System.out.println("Updated value: " + logVal);
        }finally {
            System.out.println(taskName + " (write): released lock");
            rwLock.writeLock().unlock();
        }
    }

    private void readValue(String taskName) {
        rwLock.readLock().lock();
        try {
            System.out.println(taskName + " (read): acquired lock. Reading logVal: " + logVal);
            simulateWork();
            System.out.println(taskName + " (read): finished reading");
        } finally {
            System.out.println(taskName + " (read): released lock");
            rwLock.readLock().unlock();
        }
    }


    public static void main(String[] args) {
        ReentrantReadWriteLockExample example = new ReentrantReadWriteLockExample();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        executor.submit(() -> example.readValue("Reader-1"));
        executor.submit(() -> example.readValue("Reader-2"));
        executor.submit(() -> example.readValue("Reader-3"));

//      Submit a writer task
        executor.submit(() -> example.writeValue("Writer-1", 100));

        executor.submit(() -> example.readValue("Reader-4"));
        executor.submit(() -> example.readValue("Reader-5"));

        executor.submit(() -> example.writeValue("Writer-2", 200));

        executor.submit(() -> example.readValue("Reader-6"));

        executor.shutdown();

        try {
            if(!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Timeout waiting for tasks to finish");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
