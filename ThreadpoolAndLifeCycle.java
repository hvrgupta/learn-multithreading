/*
Thread states:

1. New: A thread that has been created but not yet started
    The thread object exists but its start() method hasn't been called
    No System resources have been allocated yet

    Thread t1 = new Thread(() -> sout("Hello"));
    Thread in new state here

2. Runnable: Thread is ready for execution and waiting for CPU Allocation.
    Once start() is called, the thread moves to this state.

    t1.start() --> moves to runnable state

3. Running: Thread is currently executing its taks on the CPU
    The CPU scheduler has allocated processing time to its thread
    --> run() method in thread

4. Blocked: Thread is temporarily inactive while waiting to acquire a lock
    Typically occurs when trying to enter a synchronized block/method already locked by another thread'

    synchronized(lockobj) {
        IF another thread holds lockobj's monitor,
        this thread will be blocked until lock is available
    }

5. Waiting: Thread is waiting indefinitely for another thread to perform a specific action (notify or notifyAll)

6. Timed Waiting: we'll define some timeout,
                  after this it will automatically return to runnable after timeout expires or upon receiving a notification

7. Terminated: Thread has completed its execution or was stopped
                The run() method has exited, either normally or due to an exception
                Thread object still exists but cannot be restarted
              After thread run() method completes, thread is in terminated state
*/


/*
Thread pools are a managed collection of reusable threads designed to execute tasks concurrently
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WorkerThread implements Runnable{

    private final int taskId;

    public WorkerThread(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is processing task " + taskId);
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e) {
            System.out.println("Task interrupted: " + e.getMessage());
        }
        System.out.println(Thread.currentThread().getName() + " is finished processing task " + taskId);
    }
}

public class ThreadpoolAndLifeCycle {
    public static void main(String[] args) {

        /*
        Order of thread execution may vary due to cpu scheduling

        Benefits of thread pools:
            Resource management
            Performance management
            Predictability of creating thread
            Task management
         */

        /*
        Thread pool life cycle
        1. Pool creation: When a thread pool is create, it may pre-create threads(core threads) in the NEW state
            These threads are immediately started to enter the RUNNABLE state

        2. Task execution:
            When a task is submitted:
                An idle thread in the pool executes teh task
                The thread state changes according to task operations
                After task completion, thread returns to the pool (RUNNABLE state waiting for the next task)

        3. Pool shutdown
            During shutdown, threads complete current tasks and are eventually terminated.
         */

        ExecutorService exec = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 5; i++) {
            exec.execute(new WorkerThread(i));
        }

//      Shutdown the service
        exec.shutdown();
    }
}

/*
Thread pool usage
1. Choose the right pool type for specific workload characteristics
    ex - CPU Intensive tasks  - newFixedThreadPool

2. Short-lived tasks - Executors.newCachedThreadPool()
    I/O Bound tasks
    Threads created dynamically as needed
    Idle threads are used to minimize overload
 */
