/*
Thread executors are a high level concurrency frmwk in Java that provides a powerful abstraction over thread management
They simplify the complex task of creating, scheduling and controlling threads allowing developers to focus on business logic
rather than thread life cycle management
 */

import java.util.concurrent.*;

public class ThreadExecutor {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " Running...");
            });
        }

        executor.shutdown();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        scheduledExecutorService.schedule(() -> {
            System.out.println(Thread.currentThread().getName() + " Running...");
        },3, TimeUnit.SECONDS);

        scheduledExecutorService.shutdown();

        // Core executor interface and classes

        /*
        executor execute - Simple fire & forget (no mechanism of obtaining result)
                 submit - returns future that allows you to retrieve the result and check for Exceptions
         */

        ExecutorService executorService2 = Executors.newFixedThreadPool(5);

        Future<String> future = executorService2.submit(() -> "Hello World");
        System.out.println(future.get());

        executorService2.shutdown();

        /*
        executor service allows advanced task handling like submit(), invokeAll(), shutdown() and Future results for
        return values and tracking task completion
         */

        /*
        ScheduledExecutorService -- adds task scheduling capabilities
        Allows you to schedule tasks to run delay or at a fixed rate - ideal for repeated jobs like health chceks
        or polling
        */

        /*
        ThreadPoolExecutor - Primary implementation of ExecutorService
        Gives full control - core/max threads, queue type, keep alive time, rejection policy
        Great for performance tuned, production grade thread pool management
         */

        /*
        ScheduledThreadPoolExecutor - Implementation of ScheduledExecutorService

        A concrete subclass of ScheduledExecutorService, enabling delayed and repeated task scheduling with all thread
        pool tuning options
         */

        /*
        Executors -- factory class to create factory methods like newFixedThreadPool, newCachedThreadPool to easily
        spin up ready to use executors
         */

        /*
        Essential methods of Executor Service

        1. execute(Runnable cmd)
            -- tasks runs async
            -- no result is expected
            -- if task throws exception its lost

        2. submit(Runnable or Callable)
            -- accpets both runnable or callable
            -- Returns a future
            -- you can block and get the result using future.get()
            -- if task fails, future.get() throws ExecutionException
            -- use future.isCancelled() or isDone() to check status

        3. invokeAll(tasks:collections)
            -- Runs all tasks in parallel, waits until all finishes, you get a list of futures.
            -- If one task fails, its future will throw an exception on get(), but others keep running
            -- If the executor shuts down in the middle, only the remaining tasks are interrupted
            -- we also have an overloaded method that allows you to specify a timeout for all tasks
         */

        /*
        Methods in ScheduledExecutorService

        1. schedule -
            schedules a task to run once after delay
            if the task throws an exception, it wont run again.
            Lost completely if the system crashes before the delay elapses
            no built in retry logic

        2. scheduleAtFixedRate - runs repeatedly with a fixed rate between task starts(not finishes)
            if the task throws an exception it stops future executions
         */


    }
}
