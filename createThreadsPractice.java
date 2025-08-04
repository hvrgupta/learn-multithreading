
// This prevents us from extending other Thread class

import java.util.concurrent.*;

class MyThread extends Thread{
    @Override
    public void run() {
        for(int i=0;i<10;i++){
            System.out.println(Thread.currentThread().getName() + " " + i);
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }
}

// This class can be extended and can implement other interfaces as well
class MyRunnable implements Runnable{
    @Override
    public void run() {
        for(int i=0;i<10;i++){
            System.out.println(Thread.currentThread().getName() + " " + i);
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }
}

/*
Callable is more powerful and return the result, it works with future objects and also throw checked exceptions
 */

class MyCallable implements Callable<String> {
    private final String name;

    public MyCallable(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        StringBuilder res = new StringBuilder();
        for(int i=0;i<10;i++){
            res.append(name)
                    .append(" is running ").append(i).append("\n");
            Thread.sleep(500);
        }
        return res.toString();
    }
}

class practice1 {
    public static void main(String[] args) throws InterruptedException {
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();

        t1.start();
        t2.start();

        MyRunnable r1 = new MyRunnable();
        Thread t3 = new Thread(r1);
        Thread t4 = new Thread(r1);

        t3.start();
        t4.start();

        ExecutorService exec = Executors.newFixedThreadPool(2);
        Callable<String> callable1 = new MyCallable("Callable 1");
        Callable<String> callable2 = new MyCallable("Callable 2");

        try {
//          Submit callable to executor
            Future<String> f1 = exec.submit(callable1);
            Future<String> f2 = exec.submit(callable2);

//          Get results from future objects
            System.out.println("Result from task 1");
            System.out.println(f1.get()); // This blocks until the task completes

            System.out.println("Result from task 2");
            System.out.println(f2.get());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Task was interrupted " + e.getMessage());
        } finally {
            exec.shutdown();
        }
    }
}


